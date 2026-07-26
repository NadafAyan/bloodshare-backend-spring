package app.vercel.bloodshare.backend.service;
import app.vercel.bloodshare.backend.entity.BloodCamp;
import app.vercel.bloodshare.backend.exception.ResourceNotFoundException;
import app.vercel.bloodshare.backend.repository.BloodCampRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodCampService {

    private static final Logger logger = LoggerFactory.getLogger(BloodCampService.class);
    private final BloodCampRepository campRepository;

    public BloodCampService(BloodCampRepository campRepository) {
        this.campRepository = campRepository;
    }

    public BloodCamp createCamp(BloodCamp camp) {
        logger.info("Creating blood camp: {}", camp.getCampName());
        return campRepository.save(camp);
    }

    public BloodCamp getCampById(Long id) {
        return campRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BloodCamp", "id", id));
    }

    public List<BloodCamp> getAllCamps() {
        return campRepository.findAll();
    }

    public List<BloodCamp> getCampsByCity(String city) {
        return campRepository.findByCity(city);
    }

    public List<BloodCamp> getActiveCamps() {
        return campRepository.findActiveCamps();
    }

    public BloodCamp updateCamp(Long id, BloodCamp campDetails) {
        logger.info("Updating blood camp with ID: {}", id);

        BloodCamp existingCamp = getCampById(id);
        existingCamp.setCampName(campDetails.getCampName());
        existingCamp.setOrganizer(campDetails.getOrganizer());
        existingCamp.setCity(campDetails.getCity());
        existingCamp.setAddress(campDetails.getAddress());
        existingCamp.setStartDate(campDetails.getStartDate());
        existingCamp.setEndDate(campDetails.getEndDate());
        existingCamp.setStartTime(campDetails.getStartTime());
        existingCamp.setEndTime(campDetails.getEndTime());
        existingCamp.setContactPhone(campDetails.getContactPhone());
        existingCamp.setDescription(campDetails.getDescription());
        existingCamp.setIsActive(campDetails.getIsActive());

        return campRepository.update(existingCamp);
    }

    public void deleteCamp(Long id) {
        logger.info("Deleting blood camp with ID: {}", id);
        if (!campRepository.existsById(id)) {
            throw new ResourceNotFoundException("BloodCamp", "id", id);
        }
        campRepository.deleteById(id);
    }
}
