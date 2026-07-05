package app.vercel.bloodshare.backend.service;

import app.vercel.bloodshare.backend.entity.Camp;
import app.vercel.bloodshare.backend.repository.CampRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CampService {
    private final CampRepository campRepository;

    public CampService(CampRepository campRepository) {
        this.campRepository = campRepository;
    }

    public Camp createCamp(Camp receivedCamp) {
        receivedCamp.setDeleted(false);
        return campRepository.save(receivedCamp);
    }

    public List<Camp> getAllCamps() {
        return campRepository.findAll();
    }
}
