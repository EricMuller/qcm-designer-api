package com.emu.apps.qcm.web.rest.controllers;

import com.emu.apps.qcm.services.QuestionService;
import com.emu.apps.qcm.services.entity.questions.Question;
import com.emu.apps.qcm.services.repositories.specifications.question.QuestionSpecification;
import com.emu.apps.qcm.web.rest.ApiVersion;
import com.emu.apps.qcm.web.rest.dtos.FilterDto;
import com.emu.apps.qcm.web.rest.dtos.MessageDto;
import com.emu.apps.qcm.web.rest.dtos.QuestionDto;
import com.emu.apps.qcm.web.rest.dtos.question.QuestionTagsDto;
import com.emu.apps.qcm.web.rest.mappers.QuestionMapper;
import com.emu.apps.qcm.web.rest.utils.ExceptionUtil;
import com.emu.apps.qcm.web.rest.utils.StringToFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * Created by eric on 05/06/2017.
 */
@RestController
@RequestMapping(ApiVersion.V1 + "/questions")
@Api(value = "questions-store", description = "All operations ", tags = "Question")
public class QuestionRestController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;


    @Autowired
    private QuestionSpecification questionSpecification;

    @Autowired
    private StringToFilter stringToFilter;

    @ApiOperation(value = "Find all questions  by Page", responseContainer = "List", response = QuestionDto.class, nickname = "getTagsByPAge")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<QuestionTagsDto> getQuestionsWithFilters(Principal principal, @RequestParam(value = "filters", required = false) String filterString, Pageable pageable) throws IOException {
        FilterDto[] filterDtos = stringToFilter.getFilterDtos(filterString);
        return questionMapper.pageToPageTagDto(questionService.findAllByPage(questionSpecification.getFilter(filterDtos, principal), pageable));
    }

    @ApiOperation(value = "Find a question by ID", response = QuestionDto.class, nickname = "getQuestionById")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public QuestionDto getQuestionById(@PathVariable("id") long id) {
        return questionMapper.modelToDto(questionService.findOne(id));
    }

    @ApiOperation(value = "Update a question", response = Question.class, nickname = "updateQuestion")
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public QuestionDto updateQuestion(@RequestBody QuestionDto questionDto) {

        Question question = questionService.findOne(questionDto.getId());

        question = questionMapper.dtoToModel(question, questionDto);

        return questionMapper.modelToDto(questionService.saveQuestion(question));
    }

    @ApiOperation(value = "Save a question", response = QuestionDto.class, nickname = "saveQuestion")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public QuestionDto saveQuestion(@RequestBody QuestionDto questionDto) {
        Question question = questionMapper.dtoToModel(questionDto);
        return questionMapper.modelToDto(questionService.saveQuestion(question));
    }

    @ExceptionHandler({JsonProcessingException.class, IOException.class})
    public ResponseEntity<?> handleAllException(Exception e) throws IOException {
        return new ResponseEntity<>(new MessageDto(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Delete a question by ID", response = ResponseEntity.class, nickname = "deleteQuestionById")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<Question> deleteQuestionnaireById(@PathVariable("id") long id) {
        Question question = questionService.findOne(id);
        ExceptionUtil.assertFound(question, String.valueOf(id));
        questionService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}