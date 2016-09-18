package bali.command;

import bali.Character;
import bali.ReaderWriter;
import bali.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static bali.text.Primitive.convert;


public final class Console implements ReaderWriter {

	private final PrintStream ps = System.out;
	private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	public void writeLine(Text in){
		ps.println(convert(in));
	}

	public Text readLine(){
		try {
			return convert(in.readLine());
		} catch (IOException e) {
            return null;
		}
	}

	public Character read() {
		try {
			char read = (char) in.read();
			return read > 0 ? convert(read) : null;
		} catch (IOException e) {
			return null;
		}
	}

	public void write(Character in) {
		ps.print(convert(in));
	}
}
