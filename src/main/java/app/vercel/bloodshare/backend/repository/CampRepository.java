package app.vercel.bloodshare.backend.repository;

import app.vercel.bloodshare.backend.entity.Camp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampRepository extends JpaRepository<Camp, String> {
}
