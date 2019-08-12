package bali.number;

import bali.NotImplementedException;
import org.junit.Test;

public class NotImplementedExceptionTest {

    private NotImplementedException subject = new NotImplementedException();

    @Test(expected = NotImplementedException.class)
    public void testThrowNotImplementedException(){
        throw subject;
    }

}