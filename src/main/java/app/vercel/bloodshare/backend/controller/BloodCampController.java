package app.vercel.bloodshare.backend.controller;

import app.vercel.bloodshare.backend.entity.BloodCamp;
import app.vercel.bloodshare.backend.service.BloodCampService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/camp")
public class BloodCampController {
    private final BloodCampService bloodCampService;

    public BloodCampController(BloodCampService bloodCampService) {
        this.bloodCampService = bloodCampService;
    }

    @PostMapping("/")
    public ResponseEntity<BloodCamp> createCamp(@RequestBody BloodCamp camp) {
        BloodCamp createdCamp = bloodCampService.createCamp(camp);

        if(createdCamp == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(createdCamp);
    }

    @GetMapping("/")
    public ResponseEntity<List<BloodCamp>> getAllCamps() {
        List<BloodCamp> campList = bloodCampService.getAllCamps();

        return ResponseEntity.status(HttpStatus.OK).body(campList);
    }

    @GetMapping("/{camp_name}")
    public ResponseEntity<BloodCamp> getCampById(@PathVariable String camp_name) {
        BloodCamp camp = bloodCampService.getCampById(camp_name);

        if(camp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(camp);
    }

    @PutMapping("/{camp_name}")
    public ResponseEntity<BloodCamp> updateCamp(@RequestBody BloodCamp campToupdate, @PathVariable String camp_name) {
        BloodCamp updatedCamp = bloodCampService.updateCamp(campToupdate, camp_name);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCamp);
    }

    @DeleteMapping("/{camp_name}")
    public ResponseEntity<BloodCamp> deleteCamp(@PathVariable String camp_name) {
        bloodCampService.deleteCamp(camp_name);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
