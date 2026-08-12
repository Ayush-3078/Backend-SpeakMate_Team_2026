package com.rslsolution.speakmateai.service.impl;

import com.rslsolution.speakmateai.dto.request.TeacherRequest;
import com.rslsolution.speakmateai.dto.response.TeacherResponse;
import com.rslsolution.speakmateai.entity.Admin;
import com.rslsolution.speakmateai.entity.Teacher;
import com.rslsolution.speakmateai.entity.User;
import com.rslsolution.speakmateai.enums.Role;
import com.rslsolution.speakmateai.enums.Status;
import com.rslsolution.speakmateai.repository.AdminRepository;
import com.rslsolution.speakmateai.repository.TeacherRepository;
import com.rslsolution.speakmateai.repository.UserRepository;
import com.rslsolution.speakmateai.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return user;
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null && admin.getRole() == Role.SUPER_ADMIN) {
            User proxyAdmin = new User();
            proxyAdmin.setEmail(admin.getEmail());
            proxyAdmin.setRole(admin.getRole());
            return proxyAdmin;
        }

        throw new RuntimeException("Current user not found");
    }

    @Override
    public List<TeacherResponse> getAllTeachers() {
        User currentUser = getCurrentUser();
        List<Teacher> teachers;

        if (currentUser.getRole() == Role.SUPER_ADMIN) {
            teachers = teacherRepository.findAll();
        } else if (currentUser.getRole() == Role.SCHOOL_ADMIN) {
            // Wait, Teacher doesn't currently have schoolId explicitly in the Teacher entity if it's not in User?
            // User entity has schoolId.
            // Oh, I need to check if Teacher has schoolId via inheritance. Yes, User has schoolId!
            // But does teacherRepository have findBySchoolId? I need to check TeacherRepository!
            // I'll add findBySchoolId to TeacherRepository if missing, but it's inherited so I'll just stream and filter for now to be safe if it doesn't.
            // Let's filter by schoolId from findAll for now, or assume findBySchoolId exists.
            teachers = teacherRepository.findAll().stream()
                .filter(t -> currentUser.getSchoolId().equals(t.getSchoolId()))
                .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Unauthorized to access teachers");
        }

        return teachers.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public TeacherResponse getTeacherById(Long id) {
        User currentUser = getCurrentUser();
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (currentUser.getRole() == Role.SCHOOL_ADMIN) {
            if (!currentUser.getSchoolId().equals(teacher.getSchoolId())) {
                throw new RuntimeException("Teacher not in your school");
            }
        } else if (currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new RuntimeException("Unauthorized to access teachers");
        }

        return mapToResponse(teacher);
    }

    @Override
    public TeacherResponse createTeacher(TeacherRequest request) {
        User currentUser = getCurrentUser();
        
        Long schoolIdToUse;
        if (currentUser.getRole() == Role.SUPER_ADMIN) {
            if (request.getSchoolId() == null) {
                throw new RuntimeException("Super Admin must provide a schoolId to assign the teacher to.");
            }
            schoolIdToUse = request.getSchoolId();
        } else if (currentUser.getRole() == Role.SCHOOL_ADMIN) {
            schoolIdToUse = currentUser.getSchoolId();
        } else {
            throw new RuntimeException("Unauthorized to create teachers");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        
        String password = request.getPassword() != null && !request.getPassword().isEmpty() ? request.getPassword() : "defaultPassword123!";
        teacher.setPassword(passwordEncoder.encode(password));
        
        teacher.setPhone(request.getPhone());
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setDepartment(request.getDepartment());
        teacher.setDesignation(request.getDesignation());
        teacher.setExperience(request.getExperience());
        teacher.setQualification(request.getQualification());
        teacher.setJoinedAt(LocalDateTime.now());
        
        teacher.setRole(Role.TEACHER);
        teacher.setSchoolId(schoolIdToUse);
        teacher.setStatus(Status.ACTIVE);
        teacher.setActive(true);

        Teacher savedTeacher = teacherRepository.save(teacher);
        return mapToResponse(savedTeacher);
    }

    @Override
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        User currentUser = getCurrentUser();
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (currentUser.getRole() == Role.SCHOOL_ADMIN) {
            if (!currentUser.getSchoolId().equals(teacher.getSchoolId())) {
                throw new RuntimeException("Teacher not in your school");
            }
        } else if (currentUser.getRole() == Role.SUPER_ADMIN) {
            if (request.getSchoolId() != null) {
                teacher.setSchoolId(request.getSchoolId());
            }
        } else {
            throw new RuntimeException("Unauthorized to update teachers");
        }

        if (!teacher.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }

        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setDepartment(request.getDepartment());
        teacher.setDesignation(request.getDesignation());
        teacher.setExperience(request.getExperience());
        teacher.setQualification(request.getQualification());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Teacher updatedTeacher = teacherRepository.save(teacher);
        return mapToResponse(updatedTeacher);
    }

    @Override
    public void deleteTeacher(Long id) {
        User currentUser = getCurrentUser();
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (currentUser.getRole() == Role.SCHOOL_ADMIN) {
            if (!currentUser.getSchoolId().equals(teacher.getSchoolId())) {
                throw new RuntimeException("Teacher not in your school");
            }
        } else if (currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new RuntimeException("Unauthorized to delete teachers");
        }

        teacherRepository.delete(teacher);
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        User currentUser = getCurrentUser();
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (currentUser.getRole() == Role.SCHOOL_ADMIN) {
            if (!currentUser.getSchoolId().equals(teacher.getSchoolId())) {
                throw new RuntimeException("Teacher not in your school");
            }
        } else if (currentUser.getRole() != Role.SUPER_ADMIN) {
            throw new RuntimeException("Unauthorized to reset teacher passwords");
        }

        teacher.setPassword(passwordEncoder.encode(newPassword));
        teacherRepository.save(teacher);
    }

    private TeacherResponse mapToResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .email(teacher.getEmail())
                .phone(teacher.getPhone())
                .isActive(teacher.isActive())
                .employeeId(teacher.getEmployeeId())
                .department(teacher.getDepartment())
                .designation(teacher.getDesignation())
                .experience(teacher.getExperience())
                .qualification(teacher.getQualification())
                .joinedAt(teacher.getJoinedAt())
                .build();
    }
}
