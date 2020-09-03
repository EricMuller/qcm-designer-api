package com.emu.apps.qcm.api.dtos.export.v1;

import com.emu.apps.qcm.api.dtos.export.v1.CategoryExportDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionExportDtoTest {
private QuestionExportDto pojoObject;
public QuestionExportDtoTest() {
this.pojoObject = new com.emu.apps.qcm.api.dtos.export.v1.QuestionExportDto();
}
@Test
public void testQuestion() {
String param = "123";
pojoObject.setQuestion(param);
Object result = pojoObject.getQuestion();
assertEquals(param, result);
}

@Test
public void testType() {
String param = "123";
pojoObject.setType(param);
Object result = pojoObject.getType();
assertEquals(param, result);
}

@Test
public void testStatus() {
String param = "123";
pojoObject.setStatus(param);
Object result = pojoObject.getStatus();
assertEquals(param, result);
}

@Test
public void testCategory() {
CategoryExportDto param = new CategoryExportDto();
pojoObject.setCategory(param);
Object result = pojoObject.getCategory();
assertEquals(param, result);
}

@Test
public void testTip() {
String param = "123";
pojoObject.setTip(param);
Object result = pojoObject.getTip();
assertEquals(param, result);
}

@Test
public void testPosition() {
Long param = Long.valueOf(123);
pojoObject.setPosition(param);
Object result = pojoObject.getPosition();
assertEquals(param, result);
}

@Test
public void testPoints() {
Long param = Long.valueOf(123);
pojoObject.setPoints(param);
Object result = pojoObject.getPoints();
assertEquals(param, result);
}

}
