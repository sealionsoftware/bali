package bali.logic;

import bali.Logic;
import org.junit.Test;

import static bali.Logic.FALSE;
import static bali.Logic.TRUE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TrueTest {

    private Logic subject = True.VALUE;

    @Test
    public void testNot() throws Exception {

        Logic ret = subject.not();
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testAndTrue() throws Exception {

        Logic ret = subject.and(TRUE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testAndFalse() throws Exception {

        Logic ret = subject.and(FALSE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testOrTrue() throws Exception {

        Logic ret = subject.or(TRUE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testOrFalse() throws Exception {

        Logic ret = subject.or(FALSE);
        assertThat(ret, equalTo(TRUE));
    }


    @Test
    public void testEqualToTrue() throws Exception {

        Logic ret = subject.equalTo(TRUE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testEqualToFalse() throws Exception {

        Logic ret = subject.equalTo(FALSE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testNotEqualToTrue() throws Exception {

        Logic ret = subject.notEqualTo(TRUE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testNotEqualToFalse() throws Exception {

        Logic ret = subject.notEqualTo(FALSE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testToString() throws Exception {

        String ret = subject.toString();
        assertThat(ret, equalTo("TRUE"));
    }
}