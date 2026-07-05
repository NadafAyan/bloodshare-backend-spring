package app.vercel.bloodshare.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Camp {
    @Id
    private String camp_name;
    private String organization;
    private LocalDateTime camp_date_time;
    private int expected_donors;
    private Long contact;
    private String description;
    private boolean deleted;

    public String getCamp_name() {
        return camp_name;
    }

    public void setCamp_name(String camp_name) {
        this.camp_name = camp_name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public LocalDateTime getCamp_date_time() {
        return camp_date_time;
    }

    public void setCamp_date_time(LocalDateTime camp_date_time) {
        this.camp_date_time = camp_date_time;
    }

    public int getExpected_donors() {
        return expected_donors;
    }

    public void setExpected_donors(int expected_donors) {
        this.expected_donors = expected_donors;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
