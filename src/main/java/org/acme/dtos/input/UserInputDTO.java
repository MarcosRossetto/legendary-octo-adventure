package org.acme.dtos.input;

import org.acme.config.interfaces.ValidPassword;
import org.acme.dtos.validations.OnCreate;
import org.acme.dtos.validations.OnUpdate;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInputDTO {

    @NotBlank(groups = OnCreate.class)
    @Length(min = 3, max = 100, groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotBlank(groups = OnCreate.class)
    @Email(groups = {OnCreate.class, OnUpdate.class})
    @Length(min = 5, max = 150, groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(groups = OnCreate.class)
    @ValidPassword(groups = {OnCreate.class})
    private String password;
}
