package app.vercel.bloodshare.backend.service;
import app.vercel.bloodshare.backend.entity.BloodRequest;
import app.vercel.bloodshare.backend.exception.ResourceNotFoundException;
import app.vercel.bloodshare.backend.repository.BloodRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodRequestService {

    private static final Logger logger = LoggerFactory.getLogger(BloodRequestService.class);
    private final BloodRequestRepository requestRepository;

    public BloodRequestService(BloodRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public BloodRequest createRequest(BloodRequest request) {
        logger.info("Creating blood request for patient: {}", request.getPatientName());
        request.setStatus(BloodRequest.RequestStatus.PENDING);
        return requestRepository.save(request);
    }

    public BloodRequest getRequestById(Long id) {
        logger.debug("Fetching blood request with ID: {}", id);
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BloodRequest", "id", id));
    }

    public List<BloodRequest> getAllRequests() {
        logger.debug("Fetching all blood requests");
        return requestRepository.findAll();
    }

    public List<BloodRequest> getPendingRequests() {
        logger.debug("Fetching pending blood requests");
        return requestRepository.findPendingRequests();
    }

    public List<BloodRequest> getRequestsByBloodGroup(String bloodGroup) {
        return requestRepository.findByBloodGroup(bloodGroup);
    }

    public List<BloodRequest> getRequestsByCity(String city) {
        return requestRepository.findByCity(city);
    }

    public List<BloodRequest> getRequestsByBloodGroupAndCity(String bloodGroup, String city) {
        return requestRepository.findByBloodGroupAndCity(bloodGroup, city);
    }

    public List<BloodRequest> getRequestsByStatus(BloodRequest.RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    public BloodRequest updateRequest(Long id, BloodRequest requestDetails) {
        logger.info("Updating blood request with ID: {}", id);

        BloodRequest existingRequest = getRequestById(id);
        existingRequest.setPatientName(requestDetails.getPatientName());
        existingRequest.setContactName(requestDetails.getContactName());
        existingRequest.setContactPhone(requestDetails.getContactPhone());
        existingRequest.setContactEmail(requestDetails.getContactEmail());
        existingRequest.setBloodGroup(requestDetails.getBloodGroup());
        existingRequest.setCity(requestDetails.getCity());
        existingRequest.setHospitalName(requestDetails.getHospitalName());
        existingRequest.setHospitalAddress(requestDetails.getHospitalAddress());
        existingRequest.setUnitsNeeded(requestDetails.getUnitsNeeded());
        existingRequest.setUrgencyLevel(requestDetails.getUrgencyLevel());
        existingRequest.setNotes(requestDetails.getNotes());

        return requestRepository.update(existingRequest);
    }

    public void updateRequestStatus(Long id, BloodRequest.RequestStatus status) {
        logger.info("Updating request {} status to {}", id, status);
        if (!requestRepository.existsById(id)) {
            throw new ResourceNotFoundException("BloodRequest", "id", id);
        }
        requestRepository.updateStatus(id, status);
    }

    public void deleteRequest(Long id) {
        logger.info("Deleting blood request with ID: {}", id);
        if (!requestRepository.existsById(id)) {
            throw new ResourceNotFoundException("BloodRequest", "id", id);
        }
        requestRepository.deleteById(id);
    }

    public long getRequestCount() {
        return requestRepository.count();
    }

    public long getRequestCountByStatus(BloodRequest.RequestStatus status) {
        return requestRepository.countByStatus(status);
    }
}