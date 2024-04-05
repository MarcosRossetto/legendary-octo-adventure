package org.acme.dtos.input;

import org.acme.dtos.validations.OnCreate;
import org.acme.dtos.validations.OnUpdate;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostInputDTO {

    @NotBlank(groups = OnCreate.class)
    @Length(min = 5, max = 200, groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @NotBlank(groups = OnCreate.class)
    @Length(min = 5, max = 10000, groups = {OnCreate.class, OnUpdate.class})
    private String content;

    private Long user; 

}