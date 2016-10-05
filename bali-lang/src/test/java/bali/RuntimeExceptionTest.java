package bali;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class RuntimeExceptionTest {

    private RuntimeException subject = new RuntimeException("Halt!");

    @Test(expected = RuntimeException.class)
    public void testThrow(){

        assertThat(subject.payload, equalTo("Halt!"));

        throw subject;
    }

}