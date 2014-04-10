package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.collection.Collection;
import bali.collection.ValueCollection;

import java.util.Arrays;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 10/03/14
 */
@MetaType(Kind.OBJECT)
public class StringBuilder implements String {

	private static final int SCALE = 256;

	private int length = 0;
	private char[] characters = new char[SCALE];

	public String uppercase() {
		throw new BaliThrowable("Not implemented yet");
	}

	public ValueCollection<String> split(@Name("separator") String seperator) {
		throw new BaliThrowable("Not implemented yet");
	}

	public Boolean contains(@Name("value") Character value) {
		throw new BaliThrowable("Not implemented yet");
	}

	public Integer size() {
		return convert(length);
	}

	public Boolean isEmpty() {
		return convert(length > 0);
	}

	public Character get(Integer index) {
		int i = convert(index);
		if (i < length){
			return convert(characters[i-1]);
		}
		return null;
	}

	public Collection<Character> join(Collection<Character> operand) {

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			add(cas.characters);
		} else if (operand instanceof StringBuilder){
			StringBuilder sb = (StringBuilder) operand;
			add(sb.getChars());
		} else {
			throw new BaliThrowable("Not implemented yet");
		}
		return this;
	}

	private void add(char[] that){
		int thatLength = that.length;
		int retLength = length + thatLength;
		if (retLength > characters.length){
			expandFor(retLength);
		}
		System.arraycopy(that, 0, characters, length, thatLength);
		length = retLength;
	}

	private void expandFor(int newLength){
		int newBufferSize = SCALE;
		int counter = (newLength / SCALE);
		while (counter > 0){
			newBufferSize += (SCALE * counter--);
		}
		char[] newchars = new char[newBufferSize];
		System.arraycopy(characters, 0, newchars, 0, characters.length);
		characters = newchars;
	}

	public Collection<Character> head(Integer index) {
		throw new BaliThrowable("Not implemented yet");
	}

	public Collection<Character> tail(Integer index) {
		return null;
	}

	public Iterator<Character> iterator() {
		throw new BaliThrowable("Not implemented yet");
	}

	public Boolean equalTo(@Name("operand") String operand) {
		throw new BaliThrowable("Not implemented yet");
	}

	public Boolean notEqualTo(@Name("operand") String operand) {
		throw new BaliThrowable("Not implemented yet");
	}

	char[] getChars(){
		return Arrays.copyOf(characters, length);
	}
}
