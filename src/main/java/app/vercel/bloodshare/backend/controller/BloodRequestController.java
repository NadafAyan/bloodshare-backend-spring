package app.vercel.bloodshare.backend.controller;
import app.vercel.bloodshare.backend.entity.BloodRequest;
import app.vercel.bloodshare.backend.service.BloodRequestService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = {"https://bloodshare.vercel.app", "http://localhost:3000"})
public class BloodRequestController {

    private static final Logger logger = LoggerFactory.getLogger(BloodRequestController.class);
    private final BloodRequestService requestService;

    public BloodRequestController(BloodRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<BloodRequest> createRequest(@Valid @RequestBody BloodRequest request) {
        logger.info("POST /api/requests - Creating request for: {}", request.getPatientName());
        BloodRequest savedRequest = requestService.createRequest(request);
        return new ResponseEntity<>(savedRequest, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BloodRequest>> getAllRequests() {
        logger.info("GET /api/requests - Fetching all requests");
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodRequest> getRequestById(@PathVariable Long id) {
        logger.info("GET /api/requests/{} - Fetching request", id);
        return ResponseEntity.ok(requestService.getRequestById(id));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<BloodRequest>> getPendingRequests() {
        logger.info("GET /api/requests/pending - Fetching pending requests");
        return ResponseEntity.ok(requestService.getPendingRequests());
    }

    @GetMapping("/blood-group/{bloodGroup}")
    public ResponseEntity<List<BloodRequest>> getRequestsByBloodGroup(@PathVariable String bloodGroup) {
        return ResponseEntity.ok(requestService.getRequestsByBloodGroup(bloodGroup));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<BloodRequest>> getRequestsByCity(@PathVariable String city) {
        return ResponseEntity.ok(requestService.getRequestsByCity(city));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BloodRequest>> searchRequests(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String city) {

        List<BloodRequest> requests;
        if (bloodGroup != null && city != null) {
            requests = requestService.getRequestsByBloodGroupAndCity(bloodGroup, city);
        } else if (bloodGroup != null) {
            requests = requestService.getRequestsByBloodGroup(bloodGroup);
        } else if (city != null) {
            requests = requestService.getRequestsByCity(city);
        } else {
            requests = requestService.getAllRequests();
        }

        return ResponseEntity.ok(requests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodRequest> updateRequest(@PathVariable Long id,
                                                      @Valid @RequestBody BloodRequest request) {
        logger.info("PUT /api/requests/{} - Updating request", id);
        return ResponseEntity.ok(requestService.updateRequest(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam BloodRequest.RequestStatus status) {

        logger.info("PATCH /api/requests/{}/status - Setting to {}", id, status);
        requestService.updateRequestStatus(id, status);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Status updated successfully");
        response.put("status", status.name());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRequest(@PathVariable Long id) {
        logger.info("DELETE /api/requests/{} - Deleting request", id);
        requestService.deleteRequest(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Request deleted successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/summary")
    public ResponseEntity<Map<String, Long>> getRequestStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", requestService.getRequestCount());
        stats.put("pending", requestService.getRequestCountByStatus(BloodRequest.RequestStatus.PENDING));
        stats.put("fulfilled", requestService.getRequestCountByStatus(BloodRequest.RequestStatus.FULFILLED));
        return ResponseEntity.ok(stats);
    }
}