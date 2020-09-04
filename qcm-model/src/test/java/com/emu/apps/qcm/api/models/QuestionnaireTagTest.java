package com.emu.apps.qcm.api.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionnaireTagTest {

	private QuestionnaireTag aQuestionnaireTag;

	public QuestionnaireTagTest() {
		this.aQuestionnaireTag = new QuestionnaireTag();
	}
	@Test
	void testUuid() {
		String param = "123";
		aQuestionnaireTag.setUuid(param);
		Object result = aQuestionnaireTag.getUuid();
		assertEquals(param, result);
	}

	@Test
	void testLibelle() {
		String param = "123";
		aQuestionnaireTag.setLibelle(param);
		Object result = aQuestionnaireTag.getLibelle();
		assertEquals(param, result);
	}

}

