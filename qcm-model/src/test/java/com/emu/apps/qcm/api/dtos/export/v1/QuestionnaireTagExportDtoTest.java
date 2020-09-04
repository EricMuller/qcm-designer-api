package com.emu.apps.qcm.api.dtos.export.v1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionnaireTagExportDtoTest {

	private QuestionnaireTagExportDto aQuestionnaireTagExportDto;

	public QuestionnaireTagExportDtoTest() {
		this.aQuestionnaireTagExportDto = new QuestionnaireTagExportDto();
	}
	@Test
	void testLibelle() {
		String param = "123";
		aQuestionnaireTagExportDto.setLibelle(param);
		Object result = aQuestionnaireTagExportDto.getLibelle();
		assertEquals(param, result);
	}

}

