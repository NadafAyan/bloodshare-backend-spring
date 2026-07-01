package app.vercel.bloodshare.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class Donor {
    @Column(name = "full_name")
    private String full_name;
    @Column(name = "age")
    private int age;
    @Id
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private Long phone_number;
    @Column(name = "blood_group")
    private String blood_group;
    @Column(name = "city")
    private String city;
    @Column(name = "address")
    private String address;
    @Column(name = "emergency_contact")
    private Long emergency_contact;
    @Column(name = "available")
    private boolean available;
    @Column(name = "soft_delete")
    private boolean soft_delete;

    public boolean isSoft_delete() {
        return soft_delete;
    }

    public void setSoft_delete(boolean soft_delete) {
        this.soft_delete = soft_delete;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Long phone_number) {
        this.phone_number = phone_number;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(Long emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
