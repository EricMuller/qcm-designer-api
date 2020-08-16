package com.emu.apps.qcm.api.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionnaireTagDtoTest {
    private QuestionnaireTag pojoObject;

    public QuestionnaireTagDtoTest() throws Exception {
        this.pojoObject = new QuestionnaireTag();
    }

    @Test
    public void testId() {
        String param = "123";
        pojoObject.setUuid(param);
        Object result = pojoObject.getUuid();
        assertEquals(param, result);
    }

    @Test
    public void testLibelle() {
        String param = "123";
        pojoObject.setLibelle(param);
        Object result = pojoObject.getLibelle();
        assertEquals(param, result);
    }

}
