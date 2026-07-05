package app.vercel.bloodshare.backend.controller;

import app.vercel.bloodshare.backend.entity.Camp;
import app.vercel.bloodshare.backend.service.CampService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
