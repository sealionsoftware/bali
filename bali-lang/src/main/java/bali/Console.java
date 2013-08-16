package bali;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * User: Richard
 * Date: 05/05/13
 */
public final class Console implements ReaderWriter {

	private final PrintStream ps = System.out;
	private final InputStream is = System.in;

	public Console() {}

	public void writeLine(String in){
		CharArrayString cas = (CharArrayString) in;
		ps.println(cas.characters);
	}

	public String readLine(){
		char[] in = new char[256];
		try {
			char n;
			int i = 0;
			while((n = (char) is.read()) != '\n'){
				in[i++] = n;
			}
		} catch (IOException e) {
			throw new RuntimeException("IOException whilst reading from standard in");
		}
		return new CharArrayString(in);
	}
}
