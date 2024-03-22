package org.acme.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TBL_HISTORY")
@RegisterForReflection
@Entity
public class History {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "history_id_seq")
    @SequenceGenerator(name = "history_id_seq", sequenceName = "history_seq", allocationSize = 1)
    private Integer id;

    @Column(nullable = false)
    private String description;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private Long userId;
}
