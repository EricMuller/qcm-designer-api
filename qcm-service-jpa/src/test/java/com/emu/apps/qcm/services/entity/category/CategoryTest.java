package com.emu.apps.qcm.services.entity.category;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryTest {
    private Category pojoObject;

    public CategoryTest() throws Exception {
        this.pojoObject = new Category();
    }

    @Test
    public void testLibelle() {
        String param = "123";
        pojoObject.setLibelle(param);
        Object result = pojoObject.getLibelle();
        assertEquals(param, result);
    }

}

