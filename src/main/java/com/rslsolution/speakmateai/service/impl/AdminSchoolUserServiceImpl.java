package com.rslsolution.speakmateai.service.impl;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rslsolution.speakmateai.dto.request.AdminSchoolUserCreateRequest;
import com.rslsolution.speakmateai.dto.request.AdminSchoolUserUpdateRequest;
import com.rslsolution.speakmateai.dto.response.AdminSchoolUserResponse;
import com.rslsolution.speakmateai.dto.response.UserStatisticsResponse;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.UserType;
import com.rslsolution.speakmateai.repository.SchoolUserSpecification;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.AdminSchoolUserService;

@Service
@Transactional
public class AdminSchoolUserServiceImpl implements AdminSchoolUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSchoolUserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private AdminSchoolUserResponse mapToResponse(User user) {
        if (user == null) {
            return null;
        }

        AdminSchoolUserResponse response = AdminSchoolUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .schoolName(user.getSchoolName())
                .standard(user.getStandard())
                .division(user.getDivision())
                .rollNumber(user.getRollNumber())
                .parentName(user.getParentName())
                .parentPhone(user.getParentPhone())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
                
        // Calculate statistics optimally using repository count queries
        response.setTotalLessonsCompleted(userRepository.countLessonProgressByUserId(user.getId()));
        response.setTotalSpeakingSessions(userRepository.countSpeakingSessionsByUserId(user.getId()));
        response.setTotalGrammarSessions(userRepository.countGrammarHistoriesByUserId(user.getId()));
        response.setTotalVocabularySaved(userRepository.countVocabularyByUserId(user.getId()));

        return response;
    }

    @Override
    public Page<AdminSchoolUserResponse> getAllSchoolUsers(int page, int size, String sortBy, String sortDir, 
                                                           String keyword, String standard, String division, String schoolName, 
                                                           Boolean status, LocalDateTime registrationFrom, LocalDateTime registrationTo) {
        
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> spec = SchoolUserSpecification.filterSchoolUsers(keyword, standard, division, schoolName, status, registrationFrom, registrationTo);

        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(this::mapToResponse);
    }

    @Override
    public AdminSchoolUserResponse getSchoolUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("School User not found with id: " + id));
        
        if (user.getUserType() != UserType.SCHOOL) {
            throw new IllegalArgumentException("User found, but is not a school user.");
        }
        
        return mapToResponse(user);
    }

    @Override
    public AdminSchoolUserResponse createSchoolUser(AdminSchoolUserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER); // Still standard USER role for login
        user.setUserType(UserType.SCHOOL);
        user.setActive(request.isActive());
        
        user.setPhone(request.getPhone());
        user.setSchoolName(request.getSchoolName());
        user.setStandard(request.getStandard());
        user.setDivision(request.getDivision());
        user.setRollNumber(request.getRollNumber());
        user.setParentName(request.getParentName());
        user.setParentPhone(request.getParentPhone());
        
        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public AdminSchoolUserResponse updateSchoolUser(Long id, AdminSchoolUserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("School User not found with id: " + id));
                
        if (user.getUserType() != UserType.SCHOOL) {
            throw new IllegalArgumentException("User found, but is not a school user.");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setSchoolName(request.getSchoolName());
        user.setStandard(request.getStandard());
        user.setDivision(request.getDivision());
        user.setRollNumber(request.getRollNumber());
        user.setParentName(request.getParentName());
        user.setParentPhone(request.getParentPhone());
        
        if (request.getActive() != null) {
            user.setActive(request.getActive());
        }

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public void deleteSchoolUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("School User not found with id: " + id));
        
        if (user.getUserType() != UserType.SCHOOL) {
            throw new IllegalArgumentException("User found, but is not a school user.");
        }
        
        // Soft delete
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public UserStatisticsResponse getSchoolUserStatistics() {
        Specification<User> spec = SchoolUserSpecification.filterSchoolUsers(null, null, null, null, null, null, null);
        long totalUsers = userRepository.count(spec);
        
        Specification<User> activeSpec = SchoolUserSpecification.filterSchoolUsers(null, null, null, null, true, null, null);
        long activeUsers = userRepository.count(activeSpec);
        
        Specification<User> inactiveSpec = SchoolUserSpecification.filterSchoolUsers(null, null, null, null, false, null, null);
        long inactiveUsers = userRepository.count(inactiveSpec);
        
        LocalDateTime thisMonthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        Specification<User> newThisMonthSpec = SchoolUserSpecification.filterSchoolUsers(null, null, null, null, null, thisMonthStart, null);
        long newUsersThisMonth = userRepository.count(newThisMonthSpec);
        
        return UserStatisticsResponse.builder()
                .totalUsers(totalUsers)
                .activeUsers(activeUsers)
                .inactiveUsers(inactiveUsers)
                .thisMonthRegistrations(newUsersThisMonth)
                .build();
    }

    @Override
    public String exportSchoolUsersCsv() {
        Specification<User> spec = SchoolUserSpecification.filterSchoolUsers(null, null, null, null, null, null, null);
        java.util.List<User> users = userRepository.findAll(spec);
        
        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("ID,First Name,Last Name,Email,Phone,School Name,Standard,Division,Roll Number,Parent Name,Parent Phone,Status,Registration Date\n");
        
        for (User user : users) {
            csvBuilder.append(user.getId()).append(",")
                    .append(escapeSpecialCharacters(user.getFirstName())).append(",")
                    .append(escapeSpecialCharacters(user.getLastName())).append(",")
                    .append(escapeSpecialCharacters(user.getEmail())).append(",")
                    .append(escapeSpecialCharacters(user.getPhone())).append(",")
                    .append(escapeSpecialCharacters(user.getSchoolName())).append(",")
                    .append(escapeSpecialCharacters(user.getStandard())).append(",")
                    .append(escapeSpecialCharacters(user.getDivision())).append(",")
                    .append(escapeSpecialCharacters(user.getRollNumber())).append(",")
                    .append(escapeSpecialCharacters(user.getParentName())).append(",")
                    .append(escapeSpecialCharacters(user.getParentPhone())).append(",")
                    .append(user.isActive() ? "Active" : "Inactive").append(",")
                    .append(user.getCreatedAt())
                    .append("\n");
        }
        
        return csvBuilder.toString();
    }
    
    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
