package com.emu.apps.qcm.services.entity.epics;

import com.emu.apps.qcm.services.entity.common.RefEntity;

import javax.persistence.Entity;

/**
 * Created by eric on 05/06/2017.
 */
@Entity
//@Cacheable
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
//@Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region="Category")
public class Epic extends RefEntity {

    private String libelle;

    public Epic() {
    }

    public Epic(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
