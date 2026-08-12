package com.rslsolution.speakmateai.service.impl;

import com.rslsolution.speakmateai.dto.request.SchoolRequest;
import com.rslsolution.speakmateai.dto.response.SchoolResponse;
import com.rslsolution.speakmateai.entity.School;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.entity.SchoolAdmin;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.Status;
import com.rslsolution.speakmateai.repository.SchoolRepository;
import com.rslsolution.speakmateai.repository.SchoolAdminRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.EmailService;
import com.rslsolution.speakmateai.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final SchoolAdminRepository schoolAdminRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Override
    @Transactional
    public SchoolResponse createSchool(SchoolRequest request) {
        if (schoolRepository.existsByName(request.getSchoolName())) {
            throw new RuntimeException("School with this name already exists");
        }

        if (userRepository.existsByEmail(request.getAdminEmail())) {
            throw new RuntimeException("Admin email is already in use");
        }

        // 1. Create School
        School school = School.builder()
                .name(request.getSchoolName())
                .schoolName(request.getSchoolName())
                .schoolCode("SCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .address(request.getAddress())
                .contactPhone(request.getContactPhone())
                .active(true)
                .build();
        school = schoolRepository.save(school);

        // 2. Create School Admin
        String verificationToken = UUID.randomUUID().toString();
        SchoolAdmin adminUser = SchoolAdmin.builder()
                .firstName(request.getAdminFirstName())
                .lastName(request.getAdminLastName())
                .email(request.getAdminEmail())
                .password(passwordEncoder.encode("changeMe123!")) // Temporary password
                .role(Role.SCHOOL_ADMIN)
                .schoolId(school.getId())
                .status(Status.ACTIVE)
                .active(true)
                .emailVerified(false)
                .emailVerificationToken(verificationToken)
                .build();
        adminUser = schoolAdminRepository.save(adminUser);

        // 3. Send Verification Email
        String verifyLink = frontendUrl + "/verify-email?token=" + verificationToken;
        String subject = "Welcome to SpeakMate AI - Verify your School Admin Account";
        String text = "Hello " + request.getAdminFirstName() + ",\n\n" +
                "You have been added as the School Admin for " + school.getName() + ".\n" +
                "Please click the link below to verify your email and set up your account:\n\n" +
                verifyLink + "\n\n" +
                "Thank you!";
        
        try {
            emailService.sendEmail(adminUser.getEmail(), subject, text);
        } catch (Exception e) {
            // Log the error but don't fail the transaction, or handle accordingly.
            System.err.println("Failed to send email: " + e.getMessage());
        }

        return mapToResponse(school, adminUser);
    }

    @Override
    public List<SchoolResponse> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(school -> {
                    // For listing, we might just return the school data without admin details
                    // or fetch the admin if needed. Let's just return basic info here.
                    return mapToResponse(school, null);
                })
                .collect(Collectors.toList());
    }

    @Override
    public SchoolResponse getSchoolById(Long id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));
        return mapToResponse(school, null);
    }

    private SchoolResponse mapToResponse(School school, User adminUser) {
        return SchoolResponse.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .contactPhone(school.getContactPhone())
                .active(school.isActive())
                .createdAt(school.getCreatedAt())
                .adminId(adminUser != null ? adminUser.getId() : null)
                .adminEmail(adminUser != null ? adminUser.getEmail() : null)
                .build();
    }
}
