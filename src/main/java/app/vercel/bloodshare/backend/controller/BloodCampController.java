package app.vercel.bloodshare.backend.controller;
import app.vercel.bloodshare.backend.entity.BloodCamp;
import app.vercel.bloodshare.backend.service.BloodCampService;
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
@RequestMapping("/api/camps")
@CrossOrigin(origins = {"https://bloodshare.vercel.app", "http://localhost:3000"})
public class BloodCampController {

    private static final Logger logger = LoggerFactory.getLogger(BloodCampController.class);
    private final BloodCampService campService;

    public BloodCampController(BloodCampService campService) {
        this.campService = campService;
    }

    @PostMapping
    public ResponseEntity<BloodCamp> createCamp(@Valid @RequestBody BloodCamp camp) {
        logger.info("POST /api/camps - Creating camp: {}", camp.getCampName());
        BloodCamp savedCamp = campService.createCamp(camp);
        return new ResponseEntity<>(savedCamp, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BloodCamp>> getAllCamps() {
        return ResponseEntity.ok(campService.getAllCamps());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BloodCamp> getCampById(@PathVariable Long id) {
        return ResponseEntity.ok(campService.getCampById(id));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<BloodCamp>> getCampsByCity(@PathVariable String city) {
        return ResponseEntity.ok(campService.getCampsByCity(city));
    }

    @GetMapping("/active")
    public ResponseEntity<List<BloodCamp>> getActiveCamps() {
        return ResponseEntity.ok(campService.getActiveCamps());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BloodCamp> updateCamp(@PathVariable Long id,
                                                @Valid @RequestBody BloodCamp camp) {
        return ResponseEntity.ok(campService.updateCamp(id, camp));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCamp(@PathVariable Long id) {
        campService.deleteCamp(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Camp deleted successfully");
        return ResponseEntity.ok(response);
    }
}
