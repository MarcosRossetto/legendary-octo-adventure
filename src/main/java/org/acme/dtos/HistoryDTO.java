package org.acme.dtos;

import org.acme.dtos.validations.OnCreate;
import org.acme.dtos.validations.OnUpdate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryDTO {

    private Long id;

    @NotNull(groups = OnCreate.class)
    @NotEmpty(groups = OnCreate.class)
    private String description;

    @NotNull(groups = {OnCreate.class, OnUpdate.class})
    private Long userId;

    @Past(groups = OnCreate.class)
    private LocalDateTime createdDate;
}
