package com.joshkaltman.emceestudios.bankapi.repository;


import com.joshkaltman.emceestudios.bankapi.entity.AtmSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AtmSessionRepository extends CrudRepository<AtmSession, Long> {
    @Query("""
            SELECT s FROM AtmSession s
            WHERE s.bankAccount.id = :accountId
            AND s.terminatesAt > NOW()
            """)
    Optional<AtmSession> findActiveSessionByBankAccountId(long accountId);
}
