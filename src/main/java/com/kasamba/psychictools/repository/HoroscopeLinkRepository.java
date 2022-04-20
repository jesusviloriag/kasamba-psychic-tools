package com.kasamba.psychictools.repository;

import com.kasamba.psychictools.domain.HoroscopeLink;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HoroscopeLink entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HoroscopeLinkRepository extends JpaRepository<HoroscopeLink, Long> {}
