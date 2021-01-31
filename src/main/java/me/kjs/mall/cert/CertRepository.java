package me.kjs.mall.cert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CertRepository extends JpaRepository<Cert, Long> {

    Optional<Cert> findTopByCertTypeAndTokenAndExpiredDateTimeAfter(CertType certType, String token, LocalDateTime now);

}
