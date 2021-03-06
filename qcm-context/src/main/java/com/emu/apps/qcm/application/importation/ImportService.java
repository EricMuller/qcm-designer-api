package com.emu.apps.qcm.application.importation;

import com.emu.apps.qcm.application.importation.dtos.ImportFileQuestion;
import com.emu.apps.qcm.application.reporting.dtos.Export;
import com.emu.apps.qcm.application.reporting.dtos.QuestionExport;
import com.emu.apps.qcm.domain.model.base.PrincipalId;
import com.emu.apps.qcm.domain.model.category.Category;
import com.emu.apps.qcm.domain.model.category.CategoryRepository;
import com.emu.apps.qcm.domain.model.imports.ImportStatus;
import com.emu.apps.qcm.domain.model.question.Question;
import com.emu.apps.qcm.domain.model.question.QuestionRepository;
import com.emu.apps.qcm.domain.model.question.QuestionTag;
import com.emu.apps.qcm.domain.model.question.Response;
import com.emu.apps.qcm.domain.model.questionnaire.Questionnaire;
import com.emu.apps.qcm.domain.model.questionnaire.QuestionnaireId;
import com.emu.apps.qcm.domain.model.questionnaire.QuestionnaireRepository;
import com.emu.apps.qcm.domain.model.questionnaire.QuestionnaireTag;
import com.emu.apps.qcm.domain.model.tag.TagRepository;
import com.emu.apps.qcm.domain.model.upload.Upload;
import com.emu.apps.qcm.domain.model.upload.UploadId;
import com.emu.apps.qcm.domain.model.upload.UploadRepository;
import com.emu.apps.shared.exceptions.TechnicalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.emu.apps.qcm.domain.model.Status.DRAFT;
import static com.emu.apps.qcm.domain.model.imports.ImportStatus.DONE;
import static com.emu.apps.qcm.domain.model.imports.ImportStatus.REJECTED;
import static com.emu.apps.qcm.domain.model.question.TypeQuestion.FREE_TEXT;
import static com.emu.apps.qcm.domain.model.upload.TypeUpload.EXPORT_JSON;
import static com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


@Service
public class ImportService {

    public static final String IMPORT = "import";

    public static String TYPE_QUESTION = "QUESTION";

    public static String TYPE_QUESTIONNAIRE = "QUESTIONNAIRE";

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionRepository questionRepository;

    private final TagRepository tagRepository;

    private final UploadRepository uploadRepository;

    private final CategoryRepository categoryRepository;


    public ImportService(QuestionnaireRepository questionnaireService, QuestionRepository questionService,
                         TagRepository tagRepository, UploadRepository uploadRepository, CategoryRepository categoryRepository) {
        this.questionnaireRepository = questionnaireService;
        this.questionRepository = questionService;
        this.tagRepository = tagRepository;
        this.uploadRepository = uploadRepository;
        this.categoryRepository = categoryRepository;
    }


    public Upload importFile(UploadId uploadId, PrincipalId principal) throws IOException {

        var upload = uploadRepository.getUploadByUuid(uploadId);

        //  use strategy
        if (EXPORT_JSON.name().equals(upload.getType())) {
            ObjectMapper mapper = new ObjectMapper()
                    .findAndRegisterModules()
                    .disable(WRITE_DATES_AS_TIMESTAMPS)
                    .configure(INDENT_OUTPUT, true);

            Export export = mapper.readValue(new ByteArrayInputStream(upload.getData()), Export.class);
            ImportStatus importStatus = importQuestionnaire(upload.getFileName(), export, principal);
            upload.setStatus(importStatus.name());
        } else {
            final ImportFileQuestion[] fileQuestionDtos = new ObjectMapper().readValue(new ByteArrayInputStream(upload.getData()), ImportFileQuestion[].class);
            ImportStatus importStatus = importQuestionnaire(upload.getFileName(), fileQuestionDtos, principal);
            upload.setStatus(importStatus.name());
        }

        return uploadRepository.saveUpload(upload);

    }

    private Question mapToQuestion(QuestionExport questionExport, Category categoryDto) {

        var questionDto = new Question();

        questionDto.setQuestionText(questionExport.getQuestionText());
        questionDto.setType(questionExport.getType());
        questionDto.setStatus(questionExport.getStatus());

        questionDto.setCategory(categoryDto);

        List <Response> responseDtos = new ArrayList <>();
        for (var response : questionExport.getResponses()) {

            Response responseDto = new Response();
            responseDto.setResponseText(response.getResponseText());
            responseDto.setGood(response.getGood());
            responseDto.setGood(response.getGood());

            responseDtos.add(responseDto);
        }

        questionDto.setResponses(responseDtos);

        Set <QuestionTag> questionTags = new HashSet <>();

        for (var qtag : questionExport.getQuestionTags()) {
            QuestionTag questionTagDto = new QuestionTag();
            questionTagDto.setLibelle(qtag.getLibelle());
            questionTags.add(questionTagDto);
        }
        questionDto.setTags(questionTags);
        return questionDto;

    }


    private Question mapToQuestion(ImportFileQuestion fileQuestionDto, Category category) {

        var question = new Question();

        question.setQuestionText(fileQuestionDto.getQuestion());
        question.setType(FREE_TEXT.name());
        question.setStatus(DRAFT.name());

        question.setCategory(category);

        Response responseDto = new Response();
        responseDto.setResponseText(fileQuestionDto.getResponse());
        question.setResponses(asList(responseDto));

        QuestionTag questionTag = new QuestionTag();
        questionTag.setLibelle(fileQuestionDto.getCategorie());

        question.setTags(new HashSet <>(asList(questionTag)));

        return question;

    }


    @Transactional
    public ImportStatus importQuestionnaire(String name, Export export, PrincipalId principal) {

        Questionnaire questionnaire = new Questionnaire();

        // questionnaire
        questionnaire.setTitle(export.getQuestionnaire().getTitle());
        questionnaire.setStatus(export.getQuestionnaire().getStatus());
        if (StringUtils.isNotBlank(export.getQuestionnaire().getUuid())) {
            questionnaire.setId(new QuestionnaireId(export.getQuestionnaire().getUuid()));
            questionnaire.setVersion(export.getQuestionnaire().getVersion());
            questionnaire.setDateCreation(export.getQuestionnaire().getDateCreation());
            questionnaire.setDateModification(export.getQuestionnaire().getDateModification());
        }

        // tags
        HashSet <QuestionnaireTag> qtags = new HashSet <>();
        if (Objects.nonNull(export.getQuestionnaire().getQuestionnaireTags())) {
            for (var qtag : export.getQuestionnaire().getQuestionnaireTags()) {
                QuestionnaireTag questionnaireTagDto = new QuestionnaireTag();
                questionnaireTagDto.setLibelle(qtag.getLibelle());
                qtags.add(questionnaireTagDto);
            }
        }
        questionnaire.setTags(qtags);

        // categorie
        if (Objects.nonNull(export.getQuestionnaire().getCategory())) {
            Category category = new Category();
            category.setLibelle(export.getQuestionnaire().getCategory().getLibelle());
            category.setType(TYPE_QUESTIONNAIRE);
            category.setUserId(principal.toUuid());
            category = categoryRepository.saveCategory(category, principal);
            questionnaire.setCategory(category);
        }

        Questionnaire newQuestionnaire = questionnaireRepository.saveQuestionnaire(questionnaire, principal);

        //questions
        List <Question> questions = export.getQuestions()
                .stream()
                .map(questionExport ->
                {
                    Category category = null;
                    if (Objects.nonNull(questionExport.getCategory())) {
                        category = new Category();
                        category.setLibelle(questionExport.getCategory().getLibelle());
                        category.setType(TYPE_QUESTION);
                        category.setUserId(principal.toUuid());
                        category = categoryRepository.saveCategory(category, principal);
                    }

                    return mapToQuestion(questionExport, category);
                })
                .collect(Collectors.toList());

        Collection <Question> questionDtos = questionRepository.saveQuestions(questions, principal);

        questionnaireRepository.addQuestions(new QuestionnaireId(newQuestionnaire.getId().toUuid()), questionDtos, principal);

        return DONE;
    }


    @Transactional
    public ImportStatus importQuestionnaire(String name, ImportFileQuestion[] fileQuestions, PrincipalId principal) {

        if (ArrayUtils.isNotEmpty(fileQuestions)) {
            try {
                Questionnaire questionnaireDto = new Questionnaire();

                questionnaireDto.setTitle(IMPORT);
                questionnaireDto.setStatus(DRAFT.name());

                final var tagQuestionnaire = tagRepository.findOrCreateByLibelle(IMPORT, principal);

                QuestionnaireTag questionnaireTag = new QuestionnaireTag();
                questionnaireTag.setUuid(tagQuestionnaire.getId().toUuid());
                questionnaireDto.setTags(new HashSet <>(asList(questionnaireTag)));
                Questionnaire questionnaire = questionnaireRepository.saveQuestionnaire(questionnaireDto, principal);

                Category QuestionCategory = new Category();
                QuestionCategory.setLibelle(IMPORT);
                QuestionCategory.setType(TYPE_QUESTION);
                QuestionCategory.setUserId(principal.toUuid());

                final Category category = categoryRepository.saveCategory(QuestionCategory, principal);

                List <Question> questions = Arrays
                        .stream(fileQuestions)
                        .filter(fileQuestion -> isNotEmpty(fileQuestion.getCategorie()))
                        .map(fileQuestion -> mapToQuestion(fileQuestion, category))
                        .collect(Collectors.toList());

                Collection <Question> questionDtos = questionRepository.saveQuestions(questions, principal);

                questionnaireRepository.addQuestions(new QuestionnaireId(questionnaire.getId().toUuid()), questionDtos, principal);

            } catch (TechnicalException e) {

                return REJECTED;
            }

        }

        return DONE;
    }

}
