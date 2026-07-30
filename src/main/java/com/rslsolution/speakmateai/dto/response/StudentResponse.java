package com.rslsolution.speakmateai.dto.response;

import com.rslsolution.speakmateai.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String studentId;
    private Long schoolId;
    private Status status;
    private LocalDateTime createdAt;
}
