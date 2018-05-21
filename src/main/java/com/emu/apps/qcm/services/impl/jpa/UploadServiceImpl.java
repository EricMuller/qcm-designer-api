package com.emu.apps.qcm.services.impl.jpa;


import com.emu.apps.qcm.services.*;
import com.emu.apps.qcm.services.entity.epics.Category;
import com.emu.apps.qcm.services.entity.questionnaires.Questionnaire;
import com.emu.apps.qcm.services.entity.questionnaires.QuestionnaireQuestion;
import com.emu.apps.qcm.services.entity.questions.Question;
import com.emu.apps.qcm.services.entity.questions.Response;
import com.emu.apps.qcm.services.entity.questions.Type;
import com.emu.apps.qcm.services.entity.tags.QuestionTag;
import com.emu.apps.qcm.services.entity.tags.QuestionnaireTagBuilder;
import com.emu.apps.qcm.services.entity.tags.Tag;
import com.emu.apps.qcm.web.rest.dtos.FileQuestionDto;
import com.emu.apps.qcm.web.rest.mappers.FileQuestionMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class UploadServiceImpl implements UploadService {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService epicService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FileQuestionMapper fileQuestionMapper;

    @Autowired
    private QuestionnaireTagService questionnaireTagService;

    @Override
    public void createQuestionnaires(String name, FileQuestionDto[] fileQuestionDtos) {

        Map<String, Questionnaire> questionnaireCacheMap = Maps.newHashMap();
        Map<String, Long> tagsCounterMap = Maps.newHashMap();

       // Long maxPos = questionnaireService.getMaxPosition();

        //Tag tag = tagService.findOrCreateByLibelle("import");

        Category epic = epicService.findOrCreateByLibelle("Java");

        for (FileQuestionDto fileQuestionDto : fileQuestionDtos) {

            if (StringUtils.isNotEmpty(fileQuestionDto.getCategorie())) {

                //Category category = epicService.findOrCreateByLibelle(fileQuestionDto.getCategorie());

                Tag tag = tagService.findOrCreateByLibelle(fileQuestionDto.getCategorie());

                Long aLong = tagsCounterMap.containsKey(tag.getLibelle()) ? tagsCounterMap.get(tag.getLibelle()) : Long.valueOf(0);
                tagsCounterMap.put(tag.getLibelle(), ++aLong);

                Question question = fileQuestionMapper.dtoToModel(fileQuestionDto);
                question.setType(Type.FREE_TEXT);
                // question.setPosition(categoryCounterMap.get(category.getLibelle()));

                Response response = new Response();
                response.setResponse(fileQuestionDto.getResponse());
                question.setResponses(Lists.newArrayList(response));

                // new questionnaire by tag
                Questionnaire questionnaire = questionnaireCacheMap.get(tag.getLibelle());
                if (questionnaire == null) {
                    questionnaire = new Questionnaire(name + "-" + fileQuestionDto.getCategorie());
                    questionnaire.setEpic(epic);
                    //questionnaire.setPosition(++maxPos);
                    questionnaire = questionnaireService.saveQuestionnaire(questionnaire);

                    questionnaireCacheMap.put(tag.getLibelle(), questionnaire);
                }

                // question.setPosition(0L);
                question = questionService.saveQuestion(question);

                //question.setQuestionnaire(questionnaire);
                Long position = tagsCounterMap.get(tag.getLibelle());

                questionnaireService.saveQuestionnaireQuestion(new QuestionnaireQuestion(questionnaire, question, position));

                questionService.saveQuestionTag(new QuestionTag(question, tag));
                questionnaireTagService.saveQuestionnaireTag(new QuestionnaireTagBuilder().setQuestionnaire(questionnaire).setTag(tag).createQuestionnaireTag());

            }
        }
    }

}
