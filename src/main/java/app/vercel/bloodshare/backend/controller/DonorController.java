package app.vercel.bloodshare.backend.controller;

import app.vercel.bloodshare.backend.dto.DonorDTO;
import app.vercel.bloodshare.backend.dto.DonorMatchDTO;
import app.vercel.bloodshare.backend.entity.Donor;
import app.vercel.bloodshare.backend.service.DonorService;
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
@RequestMapping("/api/donors")
@CrossOrigin(origins = {"https://bloodshare.vercel.app", "http://localhost:3000"})
public class DonorController {

    private static final Logger logger = LoggerFactory.getLogger(DonorController.class);
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping
    public ResponseEntity<Donor> registerDonor(@Valid @RequestBody Donor donor) {
        logger.info("POST /api/donors - Registering donor: {}", donor.getEmail());
        Donor savedDonor = donorService.registerDonor(donor);
        return new ResponseEntity<>(savedDonor, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DonorDTO>> getAllDonors() {
        logger.info("GET /api/donors - Fetching all donors");
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Donor> getDonorById(@PathVariable Long id) {
        logger.info("GET /api/donors/{} - Fetching donor", id);
        return ResponseEntity.ok(donorService.getDonorById(id));
    }

    @GetMapping("/blood-group/{bloodGroup}")
    public ResponseEntity<List<DonorDTO>> getDonorsByBloodGroup(@PathVariable String bloodGroup) {
        logger.info("GET /api/donors/blood-group/{} - Filtering donors", bloodGroup);
        return ResponseEntity.ok(donorService.getDonorsByBloodGroup(bloodGroup));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<DonorDTO>> getDonorsByCity(@PathVariable String city) {
        logger.info("GET /api/donors/city/{} - Filtering donors", city);
        return ResponseEntity.ok(donorService.getDonorsByCity(city));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DonorDTO>> searchDonors(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String city) {

        logger.info("GET /api/donors/search - bloodGroup: {}, city: {}", bloodGroup, city);

        List<DonorDTO> donors;
        if (bloodGroup != null && city != null) {
            donors = donorService.getDonorsByBloodGroupAndCity(bloodGroup, city);
        } else if (bloodGroup != null) {
            donors = donorService.getDonorsByBloodGroup(bloodGroup);
        } else if (city != null) {
            donors = donorService.getDonorsByCity(city);
        } else {
            donors = donorService.getAllDonors();
        }

        return ResponseEntity.ok(donors);
    }

    @GetMapping("/match")
    public ResponseEntity<List<DonorMatchDTO>> findMatchingDonors(
            @RequestParam String bloodGroup,
            @RequestParam String city) {

        logger.info("GET /api/donors/match - Finding matches for {} in {}", bloodGroup, city);
        return ResponseEntity.ok(donorService.findMatchingDonors(bloodGroup, city));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Donor> updateDonor(@PathVariable Long id, @Valid @RequestBody Donor donor) {
        logger.info("PUT /api/donors/{} - Updating donor", id);
        return ResponseEntity.ok(donorService.updateDonor(id, donor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDonor(@PathVariable Long id) {
        logger.info("DELETE /api/donors/{} - Deleting donor", id);
        donorService.deleteDonor(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Donor deleted successfully");
        response.put("id", id.toString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Map<String, Long>> getDonorCount() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalDonors", donorService.getDonorCount());
        return ResponseEntity.ok(stats);
    }
}