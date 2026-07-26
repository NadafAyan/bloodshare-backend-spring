package app.vercel.bloodshare.backend.entity;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class BloodRequest {

    public enum UrgencyLevel { LOW, MEDIUM, HIGH, CRITICAL }
    public enum RequestStatus { PENDING, FULFILLED, CANCELLED }

    private Long id;

    @NotBlank(message = "Patient name is required")
    private String patientName;

    @NotBlank(message = "Contact name is required")
    private String contactName;

    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{10,20}$", message = "Invalid phone number")
    private String contactPhone;

    @Email(message = "Invalid email format")
    private String contactEmail;

    @NotBlank(message = "Blood group is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood group")
    private String bloodGroup;

    @NotBlank(message = "City is required")
    private String city;

    private String hospitalName;
    private String hospitalAddress;

    @Min(value = 1, message = "At least 1 unit is required")
    private Integer unitsNeeded = 1;

    private UrgencyLevel urgencyLevel = UrgencyLevel.MEDIUM;
    private RequestStatus status = RequestStatus.PENDING;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public BloodRequest() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalAddress() { return hospitalAddress; }
    public void setHospitalAddress(String hospitalAddress) { this.hospitalAddress = hospitalAddress; }

    public Integer getUnitsNeeded() { return unitsNeeded; }
    public void setUnitsNeeded(Integer unitsNeeded) { this.unitsNeeded = unitsNeeded; }

    public UrgencyLevel getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(UrgencyLevel urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "BloodRequest{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", city='" + city + '\'' +
                ", status=" + status +
                '}';
    }
}
