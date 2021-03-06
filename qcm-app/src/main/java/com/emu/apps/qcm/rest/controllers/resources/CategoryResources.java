package com.emu.apps.qcm.rest.controllers.resources;


import com.emu.apps.qcm.rest.controllers.resources.openui.CategoryView;
import com.emu.apps.qcm.rest.controllers.resources.openui.CategoryView.Update;
import com.emu.apps.qcm.rest.controllers.resources.openui.QuestionnaireView;
import com.emu.apps.qcm.rest.controllers.resources.openui.QuestionView;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Created by eric on 05/06/2017.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "Category")
public class CategoryResources {

    @JsonProperty("uuid")
    @JsonView({CategoryView.Find.class, Update.class,
            QuestionView.Find.class, QuestionView.Create.class, QuestionView.Update.class,
            QuestionnaireView.Find.class, QuestionnaireView.Create.class, QuestionnaireView.Update.class

    })
    private String uuid;

    @JsonProperty("version")
    @JsonView({CategoryView.Find.class, Update.class,
            QuestionView.Find.class,

    })
    private Long version;

    @JsonProperty("dateCreation")
    @JsonView({CategoryView.Find.class,
            QuestionView.Find.class,})
    private ZonedDateTime dateCreation;


    @JsonProperty("dateModification")
    @JsonView({CategoryView.Find.class
            , QuestionView.Find.class})
    private ZonedDateTime dateModification;

    @JsonProperty("libelle")
    @JsonView({CategoryView.Find.class, Update.class, CategoryView.Create.class,
            QuestionView.Find.class

    })
    private String libelle;

    @JsonProperty("type")
    @JsonView({CategoryView.Find.class, Update.class, CategoryView.Create.class,
            QuestionView.Find.class ,
    })
    private String type;

    @JsonIgnore
    private String userId;

}
