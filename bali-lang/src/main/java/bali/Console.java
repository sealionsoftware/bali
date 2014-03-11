package bali;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 05/05/13
 */
public final class Console implements ReaderWriter {

	private final PrintStream ps = System.out;
	private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public Console() {}

	public synchronized void writeLine(String in){
		ps.println(convert(in));
	}

	public synchronized String readLine(){
		try {
			return convert(in.readLine());
		} catch (IOException e) {
			throw new java.lang.RuntimeException("IOException whilst reading from standard in");
		}
	}

	public synchronized Character read() {
		try {
			char read = (char) in.read();
			return read > 0 ? convert(read) : null;
		} catch (IOException e) {
			throw new java.lang.RuntimeException("IOException whilst reading from standard in");
		}
	}

	public synchronized void write(Character in) {
		ps.print(convert(in));
	}
}
