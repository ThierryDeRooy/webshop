package com.webshop.service;

import com.webshop.entity.Verification;
import com.webshop.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationCodeRepository repository;

    public String getVerifictionIdByUsername(String username) {
        Verification verification = repository.findByUsername(username);
        if(verification != null) {
            return verification.getId();
        }
        return null;
    }

    public String createVerification(String username, String id) {
        if (!repository.existsByUsername(username)) {
            Verification verification = new Verification(id,username);
            verification = repository.save(verification);
            return verification.getId();
        }
        return getVerifictionIdByUsername(username);
    }

    public String getUsernameForId(String id) {
        Optional<Verification> verification = repository.findById(id);
        if(verification.isPresent()) {
            return verification.get().getUsername();
        }
        return null;
    }

    public void deleteVerification(String id) {
        repository.deleteById(id);
    }


}
