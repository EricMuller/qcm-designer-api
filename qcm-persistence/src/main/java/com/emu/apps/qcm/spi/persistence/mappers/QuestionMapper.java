/*
 *
 * The MIT License (MIT)
 *
 * Copyright (c)  2019 qcm-rest-api
 * Author  Eric Muller
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.emu.apps.qcm.spi.persistence.mappers;

import com.emu.apps.qcm.api.models.Question;
import com.emu.apps.qcm.api.models.question.QuestionTags;
import com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.questions.QuestionEntity;
import com.emu.apps.qcm.spi.persistence.adapters.jpa.projections.QuestionResponseProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, QuestionTagMapper.class, ResponseMapper.class,
        UuidMapper.class} , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuestionMapper {

    @Mapping(target = "questionTags", ignore = true)
    QuestionEntity dtoToModel(Question questionDto);


    @Mapping(target = "questionTags", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    QuestionEntity dtoToModel(@MappingTarget QuestionEntity question, Question questionDto);

    Question entityToQuestion(QuestionEntity question);

    QuestionTags entityToQuestionTags(QuestionEntity question);

    Question questionResponseProjectionToDto(QuestionResponseProjection questionProjection);

    default Page <Question> pageQuestionResponseProjectionToDto(Page <QuestionResponseProjection> page) {
        return page.map(this::questionResponseProjectionToDto);
    }

    default Page <QuestionTags> pageEntityToPageTagDto(Page <QuestionEntity> page) {
        return page.map(this::entityToQuestionTags);
    }

    List <Question> modelToDtos(List <QuestionEntity> questions);

}
