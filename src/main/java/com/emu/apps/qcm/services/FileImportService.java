package com.emu.apps.qcm.services;

import com.emu.apps.qcm.web.rest.dtos.FileQuestionDto;

import java.security.Principal;

public interface FileImportService {

    void createQuestionnaires(String name, FileQuestionDto[] fileQuestionDtos,Principal principal);
}