package bali.logic;

import bali.Boolean;
import org.junit.Test;

import static bali.Boolean.FALSE;
import static bali.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FalseTest {

    private Boolean subject = False.VALUE;

    @Test
    public void testNot() throws Exception {

        Boolean ret = subject.not();
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testAndTrue() throws Exception {

        Boolean ret = subject.and(TRUE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testAndFalse() throws Exception {

        Boolean ret = subject.and(FALSE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testOrTrue() throws Exception {

        Boolean ret = subject.or(TRUE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testOrFalse() throws Exception {

        Boolean ret = subject.or(FALSE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testXorTrue() throws Exception {

        Boolean ret = subject.xor(TRUE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testXorFalse() throws Exception {

        Boolean ret = subject.xor(FALSE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testEqualToTrue() throws Exception {

        Boolean ret = subject.equalTo(TRUE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testEqualToFalse() throws Exception {

        Boolean ret = subject.equalTo(FALSE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testNotEqualToTrue() throws Exception {

        Boolean ret = subject.notEqualTo(TRUE);
        assertThat(ret, equalTo(TRUE));
    }

    @Test
    public void testNotEqualToFalse() throws Exception {

        Boolean ret = subject.notEqualTo(FALSE);
        assertThat(ret, equalTo(FALSE));
    }

    @Test
    public void testToString() throws Exception {

        String ret = subject.toString();
        assertThat(ret, equalTo("FALSE"));
    }
}