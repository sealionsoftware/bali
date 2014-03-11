package bali;

import bali.annotation.Name;
import bali.collection.Array;
import bali.collection.Collection;
import bali.collection.ValueCollection;

import java.util.Arrays;

import static bali.Boolean.FALSE;
import static bali.Boolean.TRUE;
import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 02/05/13
 */
public final class CharArrayString implements String  {

	final char[] characters;
	private static final String EMPTY_STRING = new CharArrayString(new char[0]);

	public CharArrayString(char[] characters) {
		this.characters = characters;
	}

	public String uppercase() {
		char[] upper = new char[characters.length];
		int i = 0;
		for(char character : characters){
			upper[i++] = java.lang.Character.toUpperCase(character);
		}
		return new CharArrayString(upper);
	}

	public ValueCollection<String> split(String seperator) {

		if (seperator instanceof CharArrayString){
			char[] sep = ((CharArrayString) seperator).characters;
			String[] ret = new String[(characters.length / sep.length) + 1];
			int wordStart = 0;
			int currentIndex = 0;
			int sepIndex = 0;
			int wordCount = 0;
			for (char c : characters){
				if (c != sep[sepIndex++]){
					sepIndex = 0;
				}
				currentIndex++;
				if (sepIndex == sep.length){
					ret[wordCount++] = new CharArrayString(Arrays.copyOfRange(characters, wordStart, currentIndex - sepIndex));
					wordStart = currentIndex;
					sepIndex = 0;
				}
			}
			ret[wordCount] = new CharArrayString(Arrays.copyOfRange(characters, wordStart, characters.length));

			return new Array<>(Arrays.copyOf(ret, wordCount + 1));
		}

		throw new java.lang.RuntimeException("Not supported yet");
	}

	public String head(Integer index) {
		int i = convert(index);
		if (i >= characters.length){
			return this;
		}
		return new CharArrayString(Arrays.copyOfRange(characters, 0, i));
	}

	public String tail(Integer index) {
		int i = convert(index);
		if (i >= characters.length){
			return EMPTY_STRING;
		}
		return new CharArrayString(Arrays.copyOfRange(characters, i, characters.length));
	}

	public Boolean contains(@Name("value") Character value) {
		for (char c : characters){
			if (value == CharCharacter.CHARS[c]){
				return TRUE;
			}
		}
		return FALSE;
	}

	public Integer size() {
		return convert(characters.length);
	}

	public Boolean isEmpty() {
		return convert(characters.length == 0);
	}

	public Character get(Integer index) {
		return convert(characters[convert(index)]);
	}

	public String join(Collection<Character> operand) {

		char[] ret;
		int thisLength = this.characters.length;

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			int thatLength = cas.characters.length;
			ret = Arrays.copyOf(this.characters, thisLength + thatLength);
			System.arraycopy(cas.characters, 0, ret, thisLength, thatLength);
		} else {
			int thatLength = convert(operand.size());
			ret = Arrays.copyOf(this.characters, thisLength + thatLength);
			Iterator<Character> i = operand.iterator();
			int index = thisLength;
			while (convert(i.hasNext())){
				ret[index++] = convert(i.next());
			}
		}

		return new CharArrayString(ret);
	}

	public Iterator<Character> iterator() {
		return new Iterator<Character>() {

			int i = 0;

			public Boolean hasNext() {
				return convert(i < characters.length);
			}

			public Character next() {
				return i < characters.length ? CharCharacter.CHARS[characters[i++]] : null;
			}
		};
	}

	public Boolean equalTo(@Name("operand") String operand) {

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			return convert(Arrays.equals(characters, cas.characters));
		}

		if (!convert(operand.size().equalTo(size()))){
			return FALSE;
		}

		int i = 0;
		Iterator<Character> iterator = operand.iterator();
		while(convert(iterator.hasNext())){
			if (convert(iterator.next().equalTo(convert(characters[i])))){
				return FALSE;
			}
		}

		return TRUE;
	}

	public Boolean notEqualTo(@Name("operand") String operand) {

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			return convert(!Arrays.equals(characters, cas.characters));
		}

		if (convert(operand.size().notEqualTo(size()))){
			return TRUE;
		}

		int i = 0;
		Iterator<Character> iterator = operand.iterator();
		while(convert(iterator.hasNext())){
			if (convert(iterator.next().notEqualTo(convert(characters[i])))){
				return TRUE;
			}
		}

		return FALSE;
	}

	public java.lang.String toString() {
		return new java.lang.String(characters);
	}
}
