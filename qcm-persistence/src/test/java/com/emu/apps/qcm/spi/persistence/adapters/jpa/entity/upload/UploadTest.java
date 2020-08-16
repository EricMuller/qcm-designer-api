package com.emu.apps.qcm.spi.persistence.adapters.jpa.entity.upload;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UploadTest {
    private UploadEntity pojoObject;

    public UploadTest() throws Exception {
        this.pojoObject = new UploadEntity();
    }

    @Test
    public void testId() {
        Long param = Long.valueOf(123);
        pojoObject.setId(param);
        Object result = pojoObject.getId();
        assertEquals(param, result);
    }

    @Test
    public void testFileName() {
        String param = "123";
        pojoObject.setFileName(param);
        Object result = pojoObject.getFileName();
        assertEquals(param, result);
    }

    @Test
    public void testPathfileName() {
        String param = "123";
        pojoObject.setPathfileName(param);
        Object result = pojoObject.getPathfileName();
        assertEquals(param, result);
    }

}
