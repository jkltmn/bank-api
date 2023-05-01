package com.joshkaltman.emceestudios.bankapi.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AtmSession {
    public static final int DEFAULT_ATM_SESSION_LENGTH_MINUTES = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private BankAccount bankAccount;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private ZonedDateTime createdAt;

    @Column(nullable = false)
    @Builder.Default
    private ZonedDateTime terminatesAt = ZonedDateTime.now().plusMinutes(DEFAULT_ATM_SESSION_LENGTH_MINUTES);

    @Transient
    @JsonGetter(value = "isActive")
    public boolean isActive() {
        ZonedDateTime now = ZonedDateTime.now();
        return now.isBefore(terminatesAt);
    }
}
