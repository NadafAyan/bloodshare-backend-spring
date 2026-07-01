package app.vercel.bloodshare.backend.controller;

import app.vercel.bloodshare.backend.entity.Donor;
import app.vercel.bloodshare.backend.service.DonorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donor")
public class DonorController {
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

    @PostMapping("/")
    public ResponseEntity<Donor> createDonor(@RequestBody Donor donor) {
        Donor createdDonor = donorService.createDonor(donor);

        if(createdDonor == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDonor);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Donor> getDonor(@PathVariable String email) {
        Donor receivedDonor = donorService.getDonor(email);

        if(receivedDonor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.FOUND).body(receivedDonor);
    }

    @GetMapping("/")
    public ResponseEntity<List<Donor>> getAllDonors() {
        List<Donor> donorsList = donorService.getAllDonors();

        return ResponseEntity.status(HttpStatus.OK).body(donorsList);
    }

    @PutMapping("/{email}")
    public ResponseEntity<Donor> updateDonor(@PathVariable String email, @RequestBody Donor donor) {
        Donor updatedDonor = donorService.updateDonor(email, donor);

        if(updatedDonor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(updatedDonor);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Donor> deleteDonor(@PathVariable String email) {
        Donor donor = donorService.deleteDonor(email);

        if(donor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(donor);
    }
}
