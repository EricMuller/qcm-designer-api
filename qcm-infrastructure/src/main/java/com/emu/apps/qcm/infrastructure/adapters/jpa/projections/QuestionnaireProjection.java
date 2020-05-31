package com.emu.apps.qcm.infrastructure.adapters.jpa.projections;


import com.emu.apps.qcm.infrastructure.adapters.jpa.entity.Status;
import com.emu.apps.qcm.infrastructure.adapters.jpa.entity.category.Category;
import org.hibernate.annotations.Immutable;

import java.util.Date;

@Immutable
public interface QuestionnaireProjection {

    Long getId();

    Long getVersion();

    Date getDateCreation();

    String getTitle();

    Category getCategory();

    Status getStatus();

    String getDescription();

    String getLocale();


}