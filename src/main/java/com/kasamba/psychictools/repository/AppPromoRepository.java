package com.kasamba.psychictools.repository;

import com.kasamba.psychictools.domain.AppPromo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppPromo entity.
 */
@Repository
public interface AppPromoRepository extends JpaRepository<AppPromo, Long> {
    default Optional<AppPromo> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AppPromo> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AppPromo> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct appPromo from AppPromo appPromo left join fetch appPromo.app",
        countQuery = "select count(distinct appPromo) from AppPromo appPromo"
    )
    Page<AppPromo> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct appPromo from AppPromo appPromo left join fetch appPromo.app")
    List<AppPromo> findAllWithToOneRelationships();

    @Query("select appPromo from AppPromo appPromo left join fetch appPromo.app where appPromo.id =:id")
    Optional<AppPromo> findOneWithToOneRelationships(@Param("id") Long id);

    List<AppPromo> findByApp_CodenameLikeAndDateEquals(String codename, LocalDate now);
}
