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
public class PostOutputDTO {

    private Long id;

    private String title;

    private String content;

    private Long views;

    private Date createdAt;

    private Date updatedAt;

    //private UserOutputDTO user;
}