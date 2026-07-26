package app.vercel.bloodshare.backend.service;

import app.vercel.bloodshare.backend.entity.BloodCamp;
import app.vercel.bloodshare.backend.repository.BloodCampRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class BloodCampService {
    private final BloodCampRepository bloodCampRepository;

    public BloodCampService(BloodCampRepository bloodCampRepository) {
        this.bloodCampRepository = bloodCampRepository;
    }

    public BloodCamp createCamp(BloodCamp receivedCamp) {
        receivedCamp.setDeleted(false);
        return bloodCampRepository.save(receivedCamp);
    }

    public List<BloodCamp> getAllCamps() {
        return bloodCampRepository.findAll();
    }

    public BloodCamp getCampById(String camp_name) {
        Optional<BloodCamp> camp = bloodCampRepository.findById(camp_name);
        return camp.orElse(null);
    }

    public BloodCamp updateCamp(BloodCamp campToUpdate, String camp_name) {
        boolean exists = bloodCampRepository.existsById(camp_name);

        if(!exists) {
            return null;
        }

        BloodCamp updatedCamp = bloodCampRepository.save(campToUpdate);
        return updatedCamp;
    }

    public BloodCamp deleteCamp(String camp_name) {
        bloodCampRepository.deleteById(camp_name);
        return null;
    }
}
