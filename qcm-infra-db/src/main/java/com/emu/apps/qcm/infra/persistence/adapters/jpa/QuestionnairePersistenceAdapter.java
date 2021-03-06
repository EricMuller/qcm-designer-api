package com.emu.apps.qcm.infra.persistence.adapters.jpa;


import com.emu.apps.qcm.domain.model.questionnaire.Questionnaire;
import com.emu.apps.qcm.domain.model.questionnaire.QuestionnaireQuestion;
import com.emu.apps.qcm.domain.model.questionnaire.QuestionnaireTag;
import com.emu.apps.qcm.infra.persistence.QuestionnairePersistencePort;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.builders.QuestionnaireTagBuilder;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.entity.category.CategoryEntity;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.entity.questionnaires.QuestionnaireEntity;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.entity.questionnaires.QuestionnaireQuestionEntity;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.entity.tags.QuestionnaireTagEntity;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.entity.tags.TagEntity;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.repositories.CategoryRepository;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.repositories.QuestionRepository;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.repositories.QuestionnaireQuestionRepository;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.repositories.QuestionnaireRepository;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.repositories.QuestionnaireTagRepository;
import com.emu.apps.qcm.infra.persistence.adapters.jpa.repositories.TagRepository;
import com.emu.apps.qcm.infra.persistence.adapters.mappers.QuestionnaireEntityMapper;
import com.emu.apps.qcm.infra.persistence.adapters.mappers.QuestionnaireQuestionEntityMapper;
import com.emu.apps.qcm.infra.persistence.adapters.mappers.UuidMapper;
import com.emu.apps.shared.exceptions.EntityNotFoundException;
import com.emu.apps.shared.exceptions.MessageSupport;
import org.javers.core.Javers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.UUID.fromString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
@Transactional
public class QuestionnairePersistenceAdapter implements QuestionnairePersistencePort {

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionRepository questionRepository;

    private final QuestionnaireQuestionRepository questionnaireQuestionRepository;

    private final CategoryRepository categoryRepository;

    private final QuestionnaireEntityMapper questionnaireMapper;

    private final UuidMapper uuidMapper;

    private final TagRepository tagRepository;

    private final QuestionnaireTagRepository questionnaireTagRepository;

    private final QuestionnaireQuestionEntityMapper questionnaireQuestionMapper;

    private final Javers javers;

    public QuestionnairePersistenceAdapter(QuestionnaireRepository questionnaireRepository, QuestionRepository questionRepository,
                                           QuestionnaireQuestionRepository questionnaireQuestionRepository,
                                           CategoryRepository categoryRepository, QuestionnaireEntityMapper questionnaireMapper,
                                           UuidMapper uuidMapper, TagRepository tagRepository,
                                           QuestionnaireTagRepository questionnaireTagRepository, QuestionnaireQuestionEntityMapper questionnaireQuestionMapper, Javers javers) {
        this.questionnaireRepository = questionnaireRepository;
        this.questionRepository = questionRepository;
        this.questionnaireQuestionRepository = questionnaireQuestionRepository;
        this.categoryRepository = categoryRepository;
        this.questionnaireMapper = questionnaireMapper;
        this.uuidMapper = uuidMapper;
        this.tagRepository = tagRepository;
        this.questionnaireTagRepository = questionnaireTagRepository;
        this.questionnaireQuestionMapper = questionnaireQuestionMapper;
        this.javers = javers;
    }

    @Override
    @Transactional()
    public void deleteByUuid(String uuid) {
        questionnaireRepository.deleteByUuid(fromString(uuid));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional <Questionnaire> findByUuid(String uuid) {

        return ofNullable(questionnaireMapper
                .modelToDto(questionnaireRepository.findByUuid(fromString(uuid)).orElse(null)));
    }


    @Override
    public Questionnaire saveQuestionnaire(final Questionnaire questionnaire, String principal) {

        CategoryEntity category = null;
        UUID uuid = uuidMapper.getUuid(questionnaire.getCategory());
        if (nonNull(uuid)) {
            category = categoryRepository.findByUuid(uuid);
        }

        QuestionnaireEntity questionnaireEntity = null;
        if (nonNull(questionnaire.getId()) && isNotBlank(questionnaire.getId().toUuid())) {
            questionnaireEntity = questionnaireRepository.findByUuid(fromString(questionnaire.getId().toUuid()))
                    .orElseThrow(() -> new EntityNotFoundException(questionnaire.getId().toUuid(), MessageSupport.UNKNOWN_UUID_QUESTIONNAIRE));
        }

        if (nonNull(questionnaireEntity)) {
            questionnaireEntity = questionnaireMapper.dtoToModel(questionnaireEntity, questionnaire);
            questionnaireEntity.setCategory(category);
        } else {
            questionnaireEntity = questionnaireMapper.dtoToModel(questionnaire);
            questionnaireEntity.setCategory(category);
            // flush for datemodification issue with javers
            questionnaireEntity = questionnaireRepository.saveAndFlush(questionnaireEntity);
        }

        saveQuestionnaireTags(questionnaireEntity, questionnaire.getTags(), principal);

        //fixme: performance issue avec javers
        // javers.commit(principal, questionnaireEntity);

        return questionnaireMapper.modelToDto(questionnaireEntity);
    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Iterable <QuestionnaireProjection> findByTitleContaining(String title) {
//        return questionnaireRepository.findByTitleContaining(title);
//    }

    @Override
    @Transactional(readOnly = true)
    public Page <Questionnaire> findAllByPage(String[] tagUuid, String principal, Pageable pageable) {

        var specificationBuilder = new QuestionnaireEntity.SpecificationBuilder();
        specificationBuilder.setPrincipal(principal);
        specificationBuilder.setTagUuids(uuidMapper.toUUIDs(tagUuid));

        return questionnaireMapper.pageToDto(questionnaireRepository.findAll(specificationBuilder.build(), pageable));
    }


    private QuestionnaireEntity saveQuestionnaireTags(QuestionnaireEntity questionnaireEntity, Iterable <QuestionnaireTag> questionnaireTagDtos, String principal) {

        questionnaireEntity.getTags().clear();

        if (nonNull(questionnaireTagDtos)) {
            for (QuestionnaireTag questionnaireTagDto : questionnaireTagDtos) {
                TagEntity tag;
                if (nonNull(questionnaireTagDto.getUuid())) {
                    tag = tagRepository.findByUuid(fromString(questionnaireTagDto.getUuid()))
                            .orElse(null);
                } else {
                    tag = tagRepository.findByLibelle(questionnaireTagDto.getLibelle(), principal);
                    if (isNull(tag)) {
                        tag = tagRepository.save(new TagEntity(questionnaireTagDto.getLibelle(), true));
                    }
                }
                if (tag != null) {
                    QuestionnaireTagEntity newTag = new QuestionnaireTagBuilder()
                            .setQuestionnaire(questionnaireEntity)
                            .setTag(tag)
                            .build();
                    questionnaireEntity.getTags().add(questionnaireTagRepository.save(newTag));
                }
            }
        }
        return questionnaireEntity;

    }

    @Override
    public QuestionnaireQuestion addQuestion(@NotNull String uuid, String questionUUid, Optional <Integer> positionOpt, String principal) {

        if ( isNull(questionUUid)) {
            throw new EntityNotFoundException(uuid, MessageSupport.INVALID_UUID_QUESTION);
        }

        if ( isNull(uuid)) {
            throw new EntityNotFoundException(uuid, MessageSupport.INVALID_UUID_QUESTIONNAIRE);
        }

        var questionnaireEntity = questionnaireRepository.findByUuid(fromString(uuid))
                .orElseThrow(() -> new EntityNotFoundException(uuid, MessageSupport.UNKNOWN_UUID_QUESTIONNAIRE));


        Integer position = positionOpt.isEmpty() ? questionnaireEntity.getQuestionnaireQuestions().size() + 1 : positionOpt.get();
        var questionEntity = questionRepository.findByUuid(fromString(questionUUid))
                .orElseThrow(() -> new EntityNotFoundException(questionUUid, MessageSupport.UNKNOWN_UUID_QUESTION));

        QuestionnaireQuestionEntity questionnaireQuestionEntity = questionnaireQuestionRepository
                .save(new QuestionnaireQuestionEntity(questionnaireEntity, questionEntity, position));

        javers.commit(principal, questionnaireEntity);

        return questionnaireQuestionMapper.questionnaireQuestionEntityToDomain(questionnaireQuestionEntity);
    }


//    @Override
//    @Transactional(readOnly = true)
//    public PublishedQuestionnaire findOnePublishedByUuid(final String uuid) {
//
//        var questionnaire = questionnaireRepository.findByUuid(UUID.fromString(uuid))
//                .orElseThrow(() -> new EntityNotFoundException(uuid, MessageSupport.UNKNOWN_UUID_QUESTIONNAIRE));
//
//        return publishedMapper.questionnaireToPublishedQuestionnaireDto(questionnaire);
//
//    }

    @Override
    @Transactional(readOnly = true)
    public Questionnaire findOnePublishedByUuid(final String uuid) {

        var questionnaire = questionnaireRepository.findByUuid(fromString(uuid))
                .orElseThrow(() -> new EntityNotFoundException(uuid, MessageSupport.UNKNOWN_UUID_QUESTIONNAIRE));

        return questionnaireMapper.modelToDto(questionnaire);
    }

//    @Override
//    @Transactional(readOnly = true)
//    public Page <PublishedQuestionnaire> findAllPublishedByPage(Pageable pageable) {
//
//        var specificationBuilder = new QuestionnaireEntity.SpecificationBuilder();
//
//        specificationBuilder.setPublished(Boolean.TRUE);
//
//        return publishedMapper.pageQuestionnaireToPublishedQuestionnaireDto(questionnaireRepository.findAll(specificationBuilder.build(), pageable));
//
//    }

    @Override
    @Transactional(readOnly = true)
    public Page <Questionnaire> findAllPublishedByPage(Pageable pageable) {

        var specificationBuilder = new QuestionnaireEntity.SpecificationBuilder();

        specificationBuilder.setPublished(Boolean.TRUE);

        // return publishedMapper.pageQuestionnaireToPublishedQuestionnaireDto(questionnaireRepository.findAll(specificationBuilder.build(), pageable));

        return questionnaireMapper.pageToDto(questionnaireRepository.findAll(specificationBuilder.build(), pageable));
    }


    @Override
    public Iterable <String> findPublishedCategories() {
        return questionnaireRepository.findAllDistinctCategoryLibelleByPublishedTrue();
    }


    @Transactional(readOnly = true)
    public Iterable <String> findPublishedTags() {

        return questionnaireTagRepository.findDistinctTagLibelleByDeletedFalseAndQuestionnairePublishedTrue();
    }

    @Override
    public void deleteQuestion(String questionnaireUuid, String questionUuid) {

        QuestionnaireQuestionEntity questionnaireQuestionEntity =
                questionnaireQuestionRepository.findByQuestionUuid(fromString(questionnaireUuid), fromString(questionUuid))
                        .orElseThrow(() -> new EntityNotFoundException(questionUuid, MessageSupport.UNKNOWN_UUID_QUESTION));
        questionnaireQuestionRepository.delete(questionnaireQuestionEntity);
    }


    @Override
    public QuestionnaireQuestion getQuestion(String questionnaireUuid, String questionUuid) {
        return questionnaireQuestionMapper.questionnaireQuestionEntityToDomain(
                questionnaireQuestionRepository.findByQuestionUuid(fromString(questionnaireUuid), fromString(questionUuid))
                        .orElseThrow(() -> new EntityNotFoundException(questionUuid, MessageSupport.UNKNOWN_UUID_QUESTION)));

    }

    @Transactional(readOnly = true)
    public Page <QuestionnaireQuestion> getQuestionsByQuestionnaireUuid(String questionnaireUuid, Pageable pageable) {

        return questionnaireQuestionMapper.pageQuestionnaireQuestionEntityToDomain(
                questionnaireQuestionRepository.findWithTagsAndResponseByQuestionnaireUuid(fromString(questionnaireUuid), pageable));


    }

    @Transactional(readOnly = true)
    public Iterable <QuestionnaireQuestion> getQuestionsByQuestionnaireUuid(String questionnaireUuid) {
        return questionnaireQuestionMapper.questionnaireQuestionEntityToDomain(
                questionnaireQuestionRepository.findWithTagsAndResponseByQuestionnaireUuid(fromString(questionnaireUuid)));
    }


}
