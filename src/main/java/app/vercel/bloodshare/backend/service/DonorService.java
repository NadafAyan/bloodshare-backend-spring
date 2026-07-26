package app.vercel.bloodshare.backend.service;
import app.vercel.bloodshare.backend.dto.DonorDTO;
import app.vercel.bloodshare.backend.dto.DonorMatchDTO;
import app.vercel.bloodshare.backend.entity.Donor;
import app.vercel.bloodshare.backend.exception.ResourceNotFoundException;
import app.vercel.bloodshare.backend.repository.DonorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonorService {

    private static final Logger logger = LoggerFactory.getLogger(DonorService.class);
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor registerDonor(Donor donor) {
        logger.info("Registering new donor: {}", donor.getEmail());

        if (donorRepository.existsByEmail(donor.getEmail())) {
            throw new IllegalArgumentException("Donor with email " + donor.getEmail() + " already exists");
        }

        return donorRepository.save(donor);
    }

    public Donor getDonorById(Long id) {
        logger.debug("Fetching donor with ID: {}", id);
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor", "id", id));
    }

    public List<DonorDTO> getAllDonors() {
        logger.debug("Fetching all donors");
        return donorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DonorDTO> getDonorsByBloodGroup(String bloodGroup) {
        logger.debug("Fetching donors with blood group: {}", bloodGroup);
        return donorRepository.findByBloodGroup(bloodGroup).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DonorDTO> getDonorsByCity(String city) {
        logger.debug("Fetching donors in city: {}", city);
        return donorRepository.findByCity(city).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DonorDTO> getDonorsByBloodGroupAndCity(String bloodGroup, String city) {
        logger.debug("Fetching donors with blood group {} in city {}", bloodGroup, city);
        return donorRepository.findByBloodGroupAndCity(bloodGroup, city).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DonorMatchDTO> findMatchingDonors(String bloodGroup, String city) {
        logger.info("Finding matching donors for blood group {} in city {}", bloodGroup, city);
        return donorRepository.findMatchingDonors(bloodGroup, city);
    }

    public Donor updateDonor(Long id, Donor donorDetails) {
        logger.info("Updating donor with ID: {}", id);

        Donor existingDonor = getDonorById(id);
        existingDonor.setFullName(donorDetails.getFullName());
        existingDonor.setEmail(donorDetails.getEmail());
        existingDonor.setPhone(donorDetails.getPhone());
        existingDonor.setBloodGroup(donorDetails.getBloodGroup());
        existingDonor.setCity(donorDetails.getCity());
        existingDonor.setAddress(donorDetails.getAddress());
        existingDonor.setAge(donorDetails.getAge());
        existingDonor.setWeightKg(donorDetails.getWeightKg());
        existingDonor.setLastDonationDate(donorDetails.getLastDonationDate());
        existingDonor.setIsAvailable(donorDetails.getIsAvailable());

        return donorRepository.update(existingDonor);
    }

    public void deleteDonor(Long id) {
        logger.info("Deleting donor with ID: {}", id);
        if (!donorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Donor", "id", id);
        }
        donorRepository.deleteById(id);
    }

    public long getDonorCount() {
        return donorRepository.count();
    }

    public long getDonorCountByBloodGroup(String bloodGroup) {
        return donorRepository.countByBloodGroup(bloodGroup);
    }

    private DonorDTO convertToDTO(Donor donor) {
        return new DonorDTO(
                donor.getId(),
                donor.getFullName(),
                donor.getEmail(),
                donor.getPhone(),
                donor.getBloodGroup(),
                donor.getCity(),
                donor.getIsAvailable()
        );
    }
}