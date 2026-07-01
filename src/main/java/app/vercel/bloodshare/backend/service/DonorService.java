package app.vercel.bloodshare.backend.service;

import app.vercel.bloodshare.backend.entity.Donor;
import app.vercel.bloodshare.backend.repository.DonorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonorService {
    private final DonorRepository donorRepository;

    public DonorService(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }

    public Donor createDonor(Donor donor) {
        Donor createdDonor = donorRepository.save(donor);
        return createdDonor;
    }

    public Donor getDonor(String email) {
        Optional<Donor> receivedDonor = donorRepository.findById(email);

        if(receivedDonor.isEmpty()) {
            return null;
        }

        return receivedDonor.get();
    }

    public List<Donor> getAllDonors() {
        List<Donor> donorsList = donorRepository.findAll();
        return donorsList;
    }

    public Donor updateDonor(String email, Donor receivedDonor) {
        boolean doesExists = donorRepository.existsById(email);

        if(!doesExists) {
            return null;
        }

        Optional<Donor> donar = donorRepository.findById(email);
        Donor donorToSave = donar.get();

        donorToSave.setFull_name(receivedDonor.getFull_name());
        donorToSave.setAddress(receivedDonor.getAddress());
        donorToSave.setAge(receivedDonor.getAge());
        donorToSave.setCity(receivedDonor.getCity());
        donorToSave.setAvailable(receivedDonor.isAvailable());
        donorToSave.setBlood_group(receivedDonor.getBlood_group());
        donorToSave.setEmergency_contact(receivedDonor.getEmergency_contact());
        donorToSave.setPhone_number(receivedDonor.getPhone_number());

        donorRepository.save(donorToSave);
        return donorToSave;
    }

    public Donor deleteDonor(String email) {
        boolean donorExists = donorRepository.existsById(email);

        if(!donorExists) {
            return null;
        }

        Optional<Donor> donor = donorRepository.findById(email);
        donorRepository.deleteById(email);

        return donor.get();
    }
}
