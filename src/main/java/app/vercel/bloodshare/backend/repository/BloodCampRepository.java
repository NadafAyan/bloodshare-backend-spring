package app.vercel.bloodshare.backend.repository;
import app.vercel.bloodshare.backend.entity.BloodCamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BloodCampRepository {

    private static final Logger logger = LoggerFactory.getLogger(BloodCampRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public BloodCampRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BloodCamp> campRowMapper = (rs, rowNum) -> {
        BloodCamp camp = new BloodCamp();
        camp.setId(rs.getLong("id"));
        camp.setCampName(rs.getString("camp_name"));
        camp.setOrganizer(rs.getString("organizer"));
        camp.setCity(rs.getString("city"));
        camp.setAddress(rs.getString("address"));
        camp.setStartDate(rs.getDate("start_date").toLocalDate());
        if (rs.getDate("end_date") != null) {
            camp.setEndDate(rs.getDate("end_date").toLocalDate());
        }
        if (rs.getTime("start_time") != null) {
            camp.setStartTime(rs.getTime("start_time").toLocalTime());
        }
        if (rs.getTime("end_time") != null) {
            camp.setEndTime(rs.getTime("end_time").toLocalTime());
        }
        camp.setContactPhone(rs.getString("contact_phone"));
        camp.setDescription(rs.getString("description"));
        camp.setIsActive(rs.getBoolean("is_active"));
        return camp;
    };

    public BloodCamp save(BloodCamp camp) {
        logger.info("Saving blood camp: {}", camp.getCampName());

        String sql = """
            INSERT INTO blood_camps (camp_name, organizer, city, address, 
                start_date, end_date, start_time, end_time, contact_phone, 
                description, is_active)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, camp.getCampName());
            ps.setString(2, camp.getOrganizer());
            ps.setString(3, camp.getCity());
            ps.setString(4, camp.getAddress());
            ps.setObject(5, camp.getStartDate());
            ps.setObject(6, camp.getEndDate());
            ps.setObject(7, camp.getStartTime());
            ps.setObject(8, camp.getEndTime());
            ps.setString(9, camp.getContactPhone());
            ps.setString(10, camp.getDescription());
            ps.setBoolean(11, camp.getIsActive());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            camp.setId(key.longValue());
        }

        return camp;
    }

    public Optional<BloodCamp> findById(Long id) {
        String sql = "SELECT * FROM blood_camps WHERE id = ?";
        List<BloodCamp> camps = jdbcTemplate.query(sql, campRowMapper, id);
        return camps.isEmpty() ? Optional.empty() : Optional.of(camps.get(0));
    }

    public List<BloodCamp> findAll() {
        String sql = "SELECT * FROM blood_camps ORDER BY start_date DESC";
        return jdbcTemplate.query(sql, campRowMapper);
    }

    public List<BloodCamp> findByCity(String city) {
        String sql = "SELECT * FROM blood_camps WHERE city = ? AND is_active = TRUE ORDER BY start_date DESC";
        return jdbcTemplate.query(sql, campRowMapper, city);
    }

    public List<BloodCamp> findActiveCamps() {
        String sql = "SELECT * FROM blood_camps WHERE is_active = TRUE AND start_date >= CURDATE() ORDER BY start_date";
        return jdbcTemplate.query(sql, campRowMapper);
    }

    public BloodCamp update(BloodCamp camp) {
        logger.info("Updating blood camp with ID: {}", camp.getId());

        String sql = """
            UPDATE blood_camps 
            SET camp_name = ?, organizer = ?, city = ?, address = ?,
                start_date = ?, end_date = ?, start_time = ?, end_time = ?,
                contact_phone = ?, description = ?, is_active = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                camp.getCampName(),
                camp.getOrganizer(),
                camp.getCity(),
                camp.getAddress(),
                camp.getStartDate(),
                camp.getEndDate(),
                camp.getStartTime(),
                camp.getEndTime(),
                camp.getContactPhone(),
                camp.getDescription(),
                camp.getIsActive(),
                camp.getId()
        );

        return camp;
    }

    public void deleteById(Long id) {
        logger.info("Deleting blood camp with ID: {}", id);
        String sql = "DELETE FROM blood_camps WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM blood_camps WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}