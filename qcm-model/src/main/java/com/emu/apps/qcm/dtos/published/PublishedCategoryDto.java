package com.emu.apps.qcm.dtos.published;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by eric on 05/06/2017.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublishedCategoryDto {

    private String uuid;

    @JsonProperty("libelle")
    private String libelle;

}