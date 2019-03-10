package com.emu.apps.qcm.web.rest.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by eric on 05/06/2017.
 */
@ApiModel(value = "Tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagDto extends EntityDto {

    @JsonProperty("libelle")
    private String libelle;


}
