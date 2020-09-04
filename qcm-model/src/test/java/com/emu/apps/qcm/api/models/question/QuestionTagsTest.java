package com.emu.apps.qcm.api.models.question;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTagsTest {

	private final QuestionTags aQuestionTags;

	public QuestionTagsTest() {
		this.aQuestionTags = new QuestionTags();
	}
	@Test
	void testQuestion() {
		String param = "123";
		aQuestionTags.setQuestion(param);
		Object result = aQuestionTags.getQuestion();
		assertEquals(param, result);
	}

	@Test
	void testType() {
		String param = "123";
		aQuestionTags.setType(param);
		Object result = aQuestionTags.getType();
		assertEquals(param, result);
	}

	@Test
	void testStatus() {
		String param = "123";
		aQuestionTags.setStatus(param);
		Object result = aQuestionTags.getStatus();
		assertEquals(param, result);
	}

	@Test
	void testUuid() {
		String param = "123";
		aQuestionTags.setUuid(param);
		Object result = aQuestionTags.getUuid();
		assertEquals(param, result);
	}

	@Test
	void testVersion() {
		Long param = Long.valueOf(123);
		aQuestionTags.setVersion(param);
		Object result = aQuestionTags.getVersion();
		assertEquals(param, result);
	}

	@Test
	void testDateCreation() {
		ZonedDateTime param = ZonedDateTime.now();
		aQuestionTags.setDateCreation(param);
		Object result = aQuestionTags.getDateCreation();
		assertEquals(param, result);
	}

	@Test
	void testDateModification() {
		ZonedDateTime param = ZonedDateTime.now();
		aQuestionTags.setDateModification(param);
		Object result = aQuestionTags.getDateModification();
		assertEquals(param, result);
	}

}

