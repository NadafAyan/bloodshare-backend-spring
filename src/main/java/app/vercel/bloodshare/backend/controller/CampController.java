package app.vercel.bloodshare.backend.controller;

import app.vercel.bloodshare.backend.entity.Camp;
import app.vercel.bloodshare.backend.service.CampService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/camp")
public class CampController {
    private final CampService campService;

    public CampController(CampService campService) {
        this.campService = campService;
    }

    @PostMapping("/")
    public ResponseEntity<Camp> createCamp(@RequestBody Camp camp) {
        Camp createdCamp = campService.createCamp(camp);

        if(createdCamp == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(createdCamp);
    }

    @GetMapping("/")
    public ResponseEntity<List<Camp>> getAllCamps() {
        List<Camp> campList = campService.getAllCamps();

        return ResponseEntity.status(HttpStatus.OK).body(campList);
    }

    @GetMapping("/{camp_name}")
    public ResponseEntity<Camp> getCampById(@PathVariable String camp_name) {
        Camp camp = campService.getCampById(camp_name);

        if(camp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(camp);
    }

    @PutMapping("/{camp_name}")
    public ResponseEntity<Camp> updateCamp(@RequestBody Camp campToupdate, @PathVariable String camp_name) {
        Camp updatedCamp = campService.updateCamp(campToupdate, camp_name);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCamp);
    }

    @DeleteMapping("/{camp_name}")
    public ResponseEntity<Camp> deleteCamp(@PathVariable String camp_name) {
        campService.deleteCamp(camp_name);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
