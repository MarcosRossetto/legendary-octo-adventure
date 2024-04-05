package org.acme.dtos.output;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserOutputDTO {

    private Long id;
    
    private String name;

    private String email;

    private Boolean isActive;

    private Date createdAt;

    private Date updatedAt;
    
}
