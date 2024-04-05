package org.acme.dtos.input;

import org.acme.config.interfaces.ValidPasswordChange;
import org.acme.dtos.validations.OnUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidPasswordChange(groups = OnUpdate.class)
public class UserChangePasswordInputDTO {

    private String currentPassword;

    private String newPassword;

}
