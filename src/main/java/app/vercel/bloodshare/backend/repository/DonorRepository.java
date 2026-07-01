package app.vercel.bloodshare.backend.repository;

import app.vercel.bloodshare.backend.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorRepository extends JpaRepository<Donor, String> {
}
