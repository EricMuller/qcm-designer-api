package com.emu.apps.qcm.services.repositories;

import com.emu.apps.qcm.services.entity.questions.Response;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by eric on 05/06/2017.
 */
@Repository
public interface ResponseRepository extends PagingAndSortingRepository<Response, Long> {


}
