package bali;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * User: Richard
 * Date: 05/05/13
 */
public final class Console implements ReaderWriter {

	private static final int BUFFER_SIZE = 256;

	private final PrintStream ps = System.out;
	private final InputStream is = System.in;

	public Console() {}

	public void writeLine(String in){
		CharArrayString cas = (CharArrayString) in;
		ps.println(cas.characters);
	}

	public String readLine(){
		char[] in = new char[BUFFER_SIZE];
		int i = 0;
		try {
			char n;
			while((n = (char) is.read()) != '\n'){
				in[i++] = n;
				if (i % BUFFER_SIZE == 0){
					in = Arrays.copyOf(in, i + BUFFER_SIZE);
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("IOException whilst reading from standard in");
		}
		return new CharArrayString(Arrays.copyOf(in, i));
	}
}
