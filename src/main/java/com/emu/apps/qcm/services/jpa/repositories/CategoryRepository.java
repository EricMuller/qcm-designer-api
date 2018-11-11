package com.emu.apps.qcm.services.jpa.repositories;

import com.emu.apps.qcm.services.jpa.entity.category.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by eric on 05/06/2017.
 */
@Repository
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    @Query("SELECT DISTINCT q FROM Category q WHERE q.libelle  = :libelle ")
    Category findByLibelle(@Param("libelle") String libelle);

}
