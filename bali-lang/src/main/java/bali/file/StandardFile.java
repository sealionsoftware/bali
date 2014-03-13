package bali.file;

import bali.Boolean;
import bali.Character;
import bali.Iterator;
import bali.String;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 13/03/14
 */
public class StandardFile implements File {

	private java.io.File delegate;
	private BufferedWriter output;
	private BufferedReader input;

	public StandardFile(java.io.File delegate) {
		this.delegate = delegate;
	}

	public void clear() {
		try {
			close();
			if (delegate.delete()){
				delegate.createNewFile();
			}
		} catch (Exception e){
			throw new RuntimeException("Could not clear the file");
		}
	}

	public void close() {
		try {
			if (output != null){
				output.close();
				input = null;
			}
			if (input != null){
				input.close();
				input = null;
			}
		} catch (Exception e){
			throw new RuntimeException("Could not close file");
		}
	}

	public String getPath() {
		return convert(delegate.getPath());
	}

	public String getName() {
		return convert(delegate.getName());
	}

	public Directory getParent() {
		java.io.File parent = delegate.getParentFile();
		if (parent == null){
			return null;
		}
		return new StandardDirectory(parent);
	}

	public Boolean isWritable() {
		return convert(delegate.canWrite());
	}

	public Boolean isReadable() {
		return convert(delegate.canRead());
	}

	public void delete() {
		if (!delegate.delete()){
			throw new RuntimeException("It was not possible to delete the file");
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

	public String readLine() {

		try {
			java.lang.String in = getInputStream().readLine();
			return in != null ? convert(in) : null;
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}

	public void writeLine(String in) {
		BufferedWriter output = getOutputStream();
		try {
			output.write(convert(in));
			output.write("\r\n");
			output.flush();
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}

	public Character read() {
		try {
			char in = (char) getInputStream().read();
			return in > -1 ? convert(in) : null;
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}

	public void write(Character in) {
		BufferedWriter output = getOutputStream();
		try {
			output.write(convert(in));
			output.flush();
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}

	private BufferedWriter getOutputStream(){
		if (output == null){
			try{
				output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(delegate)));
			} catch (Exception e){
				throw new RuntimeException("Could not open file for writing");
			}
		}
		return output;
	}

	private BufferedReader getInputStream(){
		if (input == null){
			try{
				input = new BufferedReader(new InputStreamReader(new FileInputStream(delegate)));
			} catch (Exception e){
				throw new RuntimeException("Could not open file for reading");
			}
		}
		return input;
	}
}
