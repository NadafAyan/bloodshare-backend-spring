package app.vercel.bloodshare.backend.repository;
import app.vercel.bloodshare.backend.entity.BloodRequest;
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
public class BloodRequestRepository {

    private static final Logger logger = LoggerFactory.getLogger(BloodRequestRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public BloodRequestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BloodRequest> requestRowMapper = (rs, rowNum) -> {
        BloodRequest request = new BloodRequest();
        request.setId(rs.getLong("id"));
        request.setPatientName(rs.getString("patient_name"));
        request.setContactName(rs.getString("contact_name"));
        request.setContactPhone(rs.getString("contact_phone"));
        request.setContactEmail(rs.getString("contact_email"));
        request.setBloodGroup(rs.getString("blood_group"));
        request.setCity(rs.getString("city"));
        request.setHospitalName(rs.getString("hospital_name"));
        request.setHospitalAddress(rs.getString("hospital_address"));
        request.setUnitsNeeded(rs.getInt("units_needed"));
        request.setUrgencyLevel(BloodRequest.UrgencyLevel.valueOf(rs.getString("urgency_level")));
        request.setStatus(BloodRequest.RequestStatus.valueOf(rs.getString("status")));
        request.setNotes(rs.getString("notes"));
        request.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        request.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return request;
    };

    public BloodRequest save(BloodRequest request) {
        logger.info("Saving blood request for patient: {}", request.getPatientName());

        String sql = """
            INSERT INTO blood_requests (patient_name, contact_name, contact_phone, 
                contact_email, blood_group, city, hospital_name, hospital_address,
                units_needed, urgency_level, status, notes)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, request.getPatientName());
            ps.setString(2, request.getContactName());
            ps.setString(3, request.getContactPhone());
            ps.setString(4, request.getContactEmail());
            ps.setString(5, request.getBloodGroup());
            ps.setString(6, request.getCity());
            ps.setString(7, request.getHospitalName());
            ps.setString(8, request.getHospitalAddress());
            ps.setInt(9, request.getUnitsNeeded());
            ps.setString(10, request.getUrgencyLevel().name());
            ps.setString(11, request.getStatus().name());
            ps.setString(12, request.getNotes());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            request.setId(key.longValue());
        }

        return request;
    }

    public Optional<BloodRequest> findById(Long id) {
        String sql = "SELECT * FROM blood_requests WHERE id = ?";
        List<BloodRequest> requests = jdbcTemplate.query(sql, requestRowMapper, id);
        return requests.isEmpty() ? Optional.empty() : Optional.of(requests.get(0));
    }

    public List<BloodRequest> findAll() {
        String sql = "SELECT * FROM blood_requests ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, requestRowMapper);
    }

    public List<BloodRequest> findByBloodGroup(String bloodGroup) {
        String sql = "SELECT * FROM blood_requests WHERE blood_group = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, requestRowMapper, bloodGroup);
    }

    public List<BloodRequest> findByCity(String city) {
        String sql = "SELECT * FROM blood_requests WHERE city = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, requestRowMapper, city);
    }

    public List<BloodRequest> findByStatus(BloodRequest.RequestStatus status) {
        String sql = "SELECT * FROM blood_requests WHERE status = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, requestRowMapper, status.name());
    }

    public List<BloodRequest> findPendingRequests() {
        String sql = "SELECT * FROM blood_requests WHERE status = 'PENDING' ORDER BY urgency_level DESC, created_at DESC";
        return jdbcTemplate.query(sql, requestRowMapper);
    }

    public List<BloodRequest> findByBloodGroupAndCity(String bloodGroup, String city) {
        String sql = "SELECT * FROM blood_requests WHERE blood_group = ? AND city = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, requestRowMapper, bloodGroup, city);
    }

    public BloodRequest update(BloodRequest request) {
        logger.info("Updating blood request with ID: {}", request.getId());

        String sql = """
            UPDATE blood_requests 
            SET patient_name = ?, contact_name = ?, contact_phone = ?, 
                contact_email = ?, blood_group = ?, city = ?, hospital_name = ?,
                hospital_address = ?, units_needed = ?, urgency_level = ?,
                status = ?, notes = ?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql,
                request.getPatientName(),
                request.getContactName(),
                request.getContactPhone(),
                request.getContactEmail(),
                request.getBloodGroup(),
                request.getCity(),
                request.getHospitalName(),
                request.getHospitalAddress(),
                request.getUnitsNeeded(),
                request.getUrgencyLevel().name(),
                request.getStatus().name(),
                request.getNotes(),
                request.getId()
        );

        return request;
    }

    public void updateStatus(Long id, BloodRequest.RequestStatus status) {
        logger.info("Updating request {} status to {}", id, status);
        String sql = "UPDATE blood_requests SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status.name(), id);
    }

    public void deleteById(Long id) {
        logger.info("Deleting blood request with ID: {}", id);
        String sql = "DELETE FROM blood_requests WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM blood_requests WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM blood_requests";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public long countByStatus(BloodRequest.RequestStatus status) {
        String sql = "SELECT COUNT(*) FROM blood_requests WHERE status = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, status.name());
        return count != null ? count : 0;
    }
}