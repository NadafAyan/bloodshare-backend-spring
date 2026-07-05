package app.vercel.bloodshare.backend.service;

import app.vercel.bloodshare.backend.entity.Camp;
import app.vercel.bloodshare.backend.repository.CampRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CampService {
    private final CampRepository campRepository;

    public CampService(CampRepository campRepository) {
        this.campRepository = campRepository;
    }

    public Camp createCamp(Camp receivedCamp) {
        receivedCamp.setDeleted(false);
        return campRepository.save(receivedCamp);
    }

    public List<Camp> getAllCamps() {
        return campRepository.findAll();
    }

    public Camp getCampById(String camp_name) {
        Optional<Camp> camp = campRepository.findById(camp_name);
        return camp.orElse(null);
    }

    public Camp updateCamp(Camp campToUpdate, String camp_name) {
        boolean exists = campRepository.existsById(camp_name);

        if(!exists) {
            return null;
        }

        Camp updatedCamp = campRepository.save(campToUpdate);
        return updatedCamp;
    }

    public Camp deleteCamp(String camp_name) {
        campRepository.deleteById(camp_name);
        return null;
    }
}
