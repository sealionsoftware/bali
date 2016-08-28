package com.sealionsoftware.bali.compiler;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SiteTest {

    private Type type = mock(Type.class);
    private Site subject = new Site(type, true);

    @Before
    public void setUp(){
        when(type.toString()).thenReturn("AType");
        when(type.isAssignableTo(type)).thenReturn(true);
    }

    @Test
    public void testToString() throws Exception {
        assertThat(subject.toString(), equalTo("AType?"));
    }

    @Test
    public void testIsAssignableTo() throws Exception {
        Site other = new Site(type, true);
        assertThat(subject.isAssignableTo(other), is(true));
    }

    @Test
    public void testIsNotAssignableTo() throws Exception {
        Site other = new Site(type, false);
        assertThat(subject.isAssignableTo(other), is(false));
    }


}