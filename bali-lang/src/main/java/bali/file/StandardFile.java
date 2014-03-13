package bali.file;

import bali.Boolean;
import bali.Character;
import bali.Iterator;
import bali.String;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 13/03/14
 */
public class StandardFile implements File {

	private java.io.File delegate;

	public StandardFile(java.io.File delegate) {
		this.delegate = delegate;
	}

	public void clear() {

	}

	public void close() {

	}

	public String getPath() {
		return convert(delegate.getPath());
	}

	public String getName() {
		return null;
	}

	public Directory getParent() {
		return null;
	}

	public Boolean isWritable() {
		return null;
	}

	public Boolean isReadable() {
		return null;
	}

	public void delete() {

	}

	public Iterator<String> iterator() {
		return null;
	}

	public Character read() {
		return null;
	}

	public String readLine() {
		return null;
	}

	public void write(Character in) {

	}

	public void writeLine(String in) {

	}
}
