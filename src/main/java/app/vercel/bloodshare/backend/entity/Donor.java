package app.vercel.bloodshare.backend.entity;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class Donor {

    private Long id;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]{10,20}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Blood group is required")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Invalid blood group")
    private String bloodGroup;

    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must be less than 50 characters")
    private String city;

    private String address;

    @Min(value = 18, message = "Donor must be at least 18 years old")
    @Max(value = 65, message = "Donor must be at most 65 years old")
    private Integer age;

    @DecimalMin(value = "45.0", message = "Weight must be at least 45 kg")
    private Double weightKg;

    private LocalDate lastDonationDate;

    private Boolean isAvailable = true;

    // Constructors
    public Donor() {}

    public Donor(Long id, String fullName, String email, String phone,
                 String bloodGroup, String city, String address,
                 Integer age, Double weightKg, LocalDate lastDonationDate,
                 Boolean isAvailable) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.address = address;
        this.age = age;
        this.weightKg = weightKg;
        this.lastDonationDate = lastDonationDate;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    @Override
    public String toString() {
        return "Donor{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", city='" + city + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }
}