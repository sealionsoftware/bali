package bali.command;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static bali.text.Primitive.convert;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConsoleTest {

    private InputStream originalIn = System.in;
    private PrintStream originalOut = System.out;
    private InputStream in = mock(InputStream.class);
    private PrintStream out = mock(PrintStream.class);
    private Console subject;

    @Before
    public void setUp(){
        System.setIn(in);
        System.setOut(out);
        subject = new Console();
    }

    @Test
    public void testWriteLine() throws Exception {

        subject.writeLine(convert("Hello World"));
        verify(out).println("Hello World");
    }

    @Test
    public void testReadLine() throws Exception {

        when(in.read(any(byte[].class), any(int.class), any(int.class))).then(invocation -> {
            byte[] bites = "Hello World".getBytes();
            System.arraycopy(bites, 0, invocation.getArguments()[0], 0, bites.length);
            return bites.length;
        }).thenReturn(-1);

        assertThat(subject.readLine(), equalTo(convert("Hello World")));
    }

    @Test
    public void testReadLineException() throws Exception {

        when(in.read(any(byte[].class), any(int.class), any(int.class))).thenThrow(new IOException());

        assertThat(subject.readLine(), nullValue());
    }

    @Test
    public void testRead() throws Exception {

        when(in.read(any(byte[].class), any(int.class), any(int.class))).then(invocation -> {
            ((byte[]) invocation.getArguments()[0])[0] = 'H';
            return 1;
        });
        assertThat(subject.read(), equalTo(convert('H')));
    }

    @Test
    public void testReadException() throws Exception {

        when(in.read(any(byte[].class), any(int.class), any(int.class))).thenThrow(new IOException());

        assertThat(subject.read(), nullValue());
    }

    @Test
    public void testWrite() throws Exception {

        subject.write(convert('H'));
        verify(out).print('H');
    }

    @After
    public void tearDown(){
        System.setIn(originalIn);
        System.setOut(originalOut);
    }


}