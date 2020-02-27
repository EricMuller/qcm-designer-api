package com.emu.apps.qcm.services.entity.category;

import com.emu.apps.qcm.services.entity.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by eric on 05/06/2017.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue(value="QUESTION")
@Table(indexes = { @Index(name = "IDX_QC_CREATE_BY_IDX", columnList = "created_by") })
public class QuestionCategory extends Category {

    public QuestionCategory(String libelle) {
        this.libelle = libelle;
    }
}
