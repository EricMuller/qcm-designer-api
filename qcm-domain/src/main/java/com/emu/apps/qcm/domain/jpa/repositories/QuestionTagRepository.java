package com.emu.apps.qcm.domain.jpa.repositories;

import com.emu.apps.qcm.domain.entity.tags.QuestionTag;
import com.emu.apps.qcm.domain.entity.tags.QuestionTagId;
import com.emu.apps.qcm.domain.entity.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, QuestionTagId> {


    @Query("SELECT qt.tag  from QuestionTag  qt WHERE qt.id.questionId = :id ")
    Iterable<Tag> findByQuestionId(@Param("id") Long questionId);


}