package app.vercel.bloodshare.backend.repository;
import app.vercel.bloodshare.backend.dto.DonorMatchDTO;
import app.vercel.bloodshare.backend.entity.Donor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class DonorRepository {

    private static final Logger logger = LoggerFactory.getLogger(DonorRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public DonorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Donor> donorRowMapper = (rs, rowNum) -> new Donor(
            rs.getLong("id"),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("blood_group"),
            rs.getString("city"),
            rs.getString("address"),
            rs.getInt("age"),
            rs.getDouble("weight_kg"),
            rs.getDate("last_donation_date") != null
                    ? rs.getDate("last_donation_date").toLocalDate() : null,
            rs.getBoolean("is_available")
    );

    public Donor save(Donor donor) {
        logger.info("Saving new donor: {}", donor.getEmail());

        String sql = """
            INSERT INTO donors (full_name, email, phone, blood_group, city, 
                               address, age, weight_kg, last_donation_date, is_available)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, donor.getFullName());
            ps.setString(2, donor.getEmail());
            ps.setString(3, donor.getPhone());
            ps.setString(4, donor.getBloodGroup());
            ps.setString(5, donor.getCity());
            ps.setString(6, donor.getAddress());
            ps.setObject(7, donor.getAge());
            ps.setObject(8, donor.getWeightKg());
            ps.setObject(9, donor.getLastDonationDate());
            ps.setBoolean(10, donor.getIsAvailable());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            donor.setId(key.longValue());
        }

        logger.info("Donor saved with ID: {}", donor.getId());
        return donor;
    }

    public Optional<Donor> findById(Long id) {
        logger.debug("Finding donor by ID: {}", id);
        String sql = "SELECT * FROM donors WHERE id = ?";
        List<Donor> donors = jdbcTemplate.query(sql, donorRowMapper, id);
        return donors.isEmpty() ? Optional.empty() : Optional.of(donors.get(0));
    }

    public List<Donor> findAll() {
        logger.debug("Fetching all donors");
        String sql = "SELECT * FROM donors ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, donorRowMapper);
    }

    public List<Donor> findByBloodGroup(String bloodGroup) {
        logger.debug("Finding donors by blood group: {}", bloodGroup);
        String sql = "SELECT * FROM donors WHERE blood_group = ? AND is_available = TRUE";
        return jdbcTemplate.query(sql, donorRowMapper, bloodGroup);
    }

    public List<Donor> findByCity(String city) {
        logger.debug("Finding donors by city: {}", city);
        String sql = "SELECT * FROM donors WHERE city = ? AND is_available = TRUE";
        return jdbcTemplate.query(sql, donorRowMapper, city);
    }

    public List<Donor> findByBloodGroupAndCity(String bloodGroup, String city) {
        logger.debug("Finding donors by blood group {} and city {}", bloodGroup, city);
        String sql = "SELECT * FROM donors WHERE blood_group = ? AND city = ? AND is_available = TRUE";
        return jdbcTemplate.query(sql, donorRowMapper, bloodGroup, city);
    }

    public List<DonorMatchDTO> findMatchingDonors(String bloodGroup, String city) {
        logger.info("Finding matching donors for blood group {} in city {}", bloodGroup, city);

        String sql = """
            SELECT 
                id as donor_id,
                full_name,
                phone,
                email,
                blood_group,
                city,
                age,
                weight_kg,
                last_donation_date,
                CASE 
                    WHEN city = ? THEN 100
                    ELSE 50
                END as match_score
            FROM donors 
            WHERE blood_group = ? AND is_available = TRUE
            ORDER BY match_score DESC, last_donation_date ASC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DonorMatchDTO dto = new DonorMatchDTO();
            dto.setDonorId(rs.getLong("donor_id"));
            dto.setFullName(rs.getString("full_name"));
            dto.setPhone(rs.getString("phone"));
            dto.setEmail(rs.getString("email"));
            dto.setBloodGroup(rs.getString("blood_group"));
            dto.setCity(rs.getString("city"));
            dto.setAge(rs.getInt("age"));
            dto.setWeightKg(rs.getDouble("weight_kg"));
            if (rs.getDate("last_donation_date") != null) {
                dto.setLastDonationDate(rs.getDate("last_donation_date").toString());
            }
            dto.setMatchScore(rs.getDouble("match_score"));
            return dto;
        }, city, bloodGroup);
    }

    public Donor update(Donor donor) {
        logger.info("Updating donor with ID: {}", donor.getId());

        String sql = """
            UPDATE donors 
            SET full_name = ?, email = ?, phone = ?, blood_group = ?, 
                city = ?, address = ?, age = ?, weight_kg = ?, 
                last_donation_date = ?, is_available = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                donor.getFullName(),
                donor.getEmail(),
                donor.getPhone(),
                donor.getBloodGroup(),
                donor.getCity(),
                donor.getAddress(),
                donor.getAge(),
                donor.getWeightKg(),
                donor.getLastDonationDate(),
                donor.getIsAvailable(),
                donor.getId()
        );

        return donor;
    }

    public void deleteById(Long id) {
        logger.info("Deleting donor with ID: {}", id);
        String sql = "DELETE FROM donors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM donors WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM donors WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM donors";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public long countByBloodGroup(String bloodGroup) {
        String sql = "SELECT COUNT(*) FROM donors WHERE blood_group = ? AND is_available = TRUE";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, bloodGroup);
        return count != null ? count : 0;
    }
}
