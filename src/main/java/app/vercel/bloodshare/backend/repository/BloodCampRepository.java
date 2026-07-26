package app.vercel.bloodshare.backend.repository;

import app.vercel.bloodshare.backend.entity.BloodCamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodCampRepository extends JpaRepository<BloodCamp, String> {
}
