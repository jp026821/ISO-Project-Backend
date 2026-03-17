package com.example.loginframe.Repository;

import com.example.loginframe.Entity.IsoStandard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IsoStandardRepository extends JpaRepository<IsoStandard, Long> {

    Optional<IsoStandard> findByIsoCode(String isoCode);

    // ✅ NEW: fetch multiple iso standards
    List<IsoStandard> findByIsoCodeIn(List<String> isoCodes);


}