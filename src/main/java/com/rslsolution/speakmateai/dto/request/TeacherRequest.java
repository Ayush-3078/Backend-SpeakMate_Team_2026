package com.rslsolution.speakmateai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    
    private String employeeId;
    private String department;
    private String designation;
    private String experience;
    private String qualification;
    private Long schoolId;
}
