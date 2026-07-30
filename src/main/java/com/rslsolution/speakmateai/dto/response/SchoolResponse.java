package com.rslsolution.speakmateai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolResponse {
    private Long id;
    private String name;
    private String address;
    private String contactPhone;
    private boolean active;
    private LocalDateTime createdAt;
    
    // Details of the admin created for this school
    private Long adminId;
    private String adminEmail;
}
