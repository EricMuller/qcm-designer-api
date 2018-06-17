package com.emu.apps.qcm.services.impl.jpa;


import com.emu.apps.qcm.services.QuestionnaireTagService;
import com.emu.apps.qcm.services.TagService;
import com.emu.apps.qcm.services.entity.questionnaires.Questionnaire;
import com.emu.apps.qcm.services.entity.tags.QuestionnaireTag;
import com.emu.apps.qcm.services.entity.tags.QuestionnaireTagBuilder;
import com.emu.apps.qcm.services.entity.tags.Tag;
import com.emu.apps.qcm.services.repositories.QuestionnaireRepository;
import com.emu.apps.qcm.services.repositories.QuestionnaireTagRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;

@Service
@Transactional()
public class QuestionnaireTagServiceImpl implements QuestionnaireTagService {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuestionnaireTagRepository questionnaireTagRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private TagService tagService;

    @Override
    public QuestionnaireTag saveQuestionnaireTag(QuestionnaireTag questionnaireTag) {
        return questionnaireTagRepository.save(questionnaireTag);
    }

    @Transactional()
    public Questionnaire saveQuestionnaireTags(long questionnaireId, Iterable<QuestionnaireTag> questionnaireTags, Principal principal) {

        Questionnaire questionnaire = questionnaireRepository.findOne(questionnaireId);

        questionnaire.getQuestionnaireTags().clear();

        if (Objects.nonNull(questionnaireTags)) {
            for (QuestionnaireTag questionnaireTag : questionnaireTags) {
                Tag tag;
                if (Objects.nonNull(questionnaireTag.getId().getTagId())) {
                    tag = tagService.findById(questionnaireTag.getId().getTagId());
                } else {
                    tag = tagService.findOrCreateByLibelle(questionnaireTag.getTag().getLibelle(), principal);
                }
                if (tag != null) {
                    QuestionnaireTag newTag = new QuestionnaireTagBuilder().setQuestionnaire(questionnaire).setTag(tag).createQuestionnaireTag();
                    questionnaire.getQuestionnaireTags().add(questionnaireTagRepository.save(newTag));
                }
            }
        }
        return questionnaire;
    }

}
