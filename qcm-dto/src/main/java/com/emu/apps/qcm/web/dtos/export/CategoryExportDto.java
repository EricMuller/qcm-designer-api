package com.emu.apps.qcm.web.dtos.export;


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
public class CategoryExportDto {

    @JsonProperty("libelle")
    private String libelle;

    @JsonProperty("type")
    private String type;

    @JsonProperty("userId")
    private String userId;

}
