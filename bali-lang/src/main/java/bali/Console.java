package bali;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class Console {

	private PrintStream ps = System.out;
	private InputStream is = System.in;

	public Console() {}

	public void printLine(String in){
		ps.println(in.characters);
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
		return new String(in);
	}
}
