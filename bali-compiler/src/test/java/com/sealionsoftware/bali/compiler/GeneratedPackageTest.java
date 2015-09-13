package com.sealionsoftware.bali.compiler;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class GeneratedPackageTest {

    private static final String NAME = "aName";

    private GeneratedPackage subject = new GeneratedPackage(NAME);

    @Test
    public void testGetName() throws Exception {
        String name = subject.getName();
        assertThat(name, equalTo(NAME));
    }

    @Test
    public void testGetClasses() throws Exception {

        GeneratedClass clazz = mock(GeneratedClass.class);

        subject.addClass(clazz);
        List<GeneratedClass> classes = subject.getClasses();

        assertThat(classes, notNullValue());
        assertThat(classes, hasSize(1));
        assertThat(classes, hasItem(clazz));
    }


}