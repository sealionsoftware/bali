package bali.net;


import bali.BaliThrowable;
import bali.Boolean;
import bali.Character;
import bali.Initialisable;
import bali.Iterator;
import bali.String;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.SocketTimeoutException;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 06/02/14
 */
public class StandardSocket implements Socket, Initialisable {

	private java.net.Socket delegate;
	private java.io.BufferedReader input;
	private java.io.BufferedWriter output;

	public StandardSocket(java.net.Socket delegate) {
		this.delegate = delegate;
	}

	public void initialise() {
		try {
			input = new BufferedReader(new InputStreamReader(delegate.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(delegate.getOutputStream()));
		} catch (IOException e){
			throw new BaliThrowable(e.getMessage());
		}
	}

	public Boolean isConnected() {
		return convert(delegate.isConnected());
	}

	public void close() {
		try {
			delegate.close();
		} catch (IOException e) {
			throw new BaliThrowable(e.getMessage());
		}
	}

	public String readLine() {

		try {
			java.lang.String in = input.readLine();
			return in != null ? convert(in) : null;
		} catch (SocketTimeoutException ioe){
			return null;
		} catch (IOException ioe){
			throw new BaliThrowable(ioe.getMessage());
		}
	}

	public Iterator<String> iterator() {
		return new Iterator<String>() {

			private String next;

			public Boolean hasNext() {
				next = readLine();
				return convert(next != null);
			}

			public String next() {
				String ret = next;
				next = null;
				return ret;
			}
		};
	}

	public void writeLine(String in) {
		try {
			output.write(convert(in));
			output.write("\r\n");
			output.flush();
		} catch (IOException ioe){
			throw new BaliThrowable(ioe.getMessage());
		}
	}

	public Character read() {
		try {
			char in = (char) input.read();
			return in > -1 ? convert(in) : null;
		} catch (SocketTimeoutException ioe){
			return null;
		} catch (IOException ioe){
			throw new BaliThrowable(ioe.getMessage());
		}
	}

	public void write(Character in) {
		try {
			output.write(convert(in));
			output.flush();
		} catch (IOException ioe){
			throw new BaliThrowable(ioe.getMessage());
		}
	}
}
