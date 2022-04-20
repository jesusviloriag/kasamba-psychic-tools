package com.kasamba.psychictools.repository;

import com.kasamba.psychictools.domain.FortuneCookie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FortuneCookie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FortuneCookieRepository extends JpaRepository<FortuneCookie, Long> {}
