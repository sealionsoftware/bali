package bali.io;

import bali.CharArrayString;
import bali.Initialisable;
import bali.String;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * User: Richard
 * Date: 06/02/14
 */
public class StandardSocket implements Socket, Initialisable {

	private java.net.Socket delegate;
	private java.io.Reader input;
	private java.io.Writer output;

	public StandardSocket(java.net.Socket delegate) {
		this.delegate = delegate;
	}

	public void initalise() throws Exception {
		input = new BufferedReader(new InputStreamReader(delegate.getInputStream()));
		output = new BufferedWriter(new OutputStreamWriter(delegate.getOutputStream()));
	}

	public void close() throws Exception {
		delegate.close();
	}

	public String readLine() throws Exception {
		final StringBuilder sb = new StringBuilder();
		int i = input.read();
		if (i < 0){
			return null;
		}
		while (i != '\n' && i > 0){
			if (i != '\r'){
				sb.append((char) i);
			}
			i = input.read();
		}

		return new CharArrayString(sb.toString().toCharArray());
	}

	public void writeLine(String in) throws IOException {
		output.write(in.toString());
		output.write("\r\n");
		output.flush();
	}

}
