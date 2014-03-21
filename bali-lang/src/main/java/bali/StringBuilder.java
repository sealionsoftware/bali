package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.collection.Collection;
import bali.collection.ValueCollection;

/**
 * User: Richard
 * Date: 10/03/14
 */
@MetaType(Kind.OBJECT)
public class StringBuilder implements String {

	char[] characters = new char[255];

	public String uppercase() {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public ValueCollection<String> split(@Name("separator") String seperator) {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Boolean contains(@Name("value") Character value) {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Integer size() {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Boolean isEmpty() {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Character get(Integer index) {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Collection<Character> join(Collection<Character> operand) {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Collection<Character> head(Integer index) {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Collection<Character> tail(Integer index) {
		return null;
	}

	public Iterator<Character> iterator() {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Boolean equalTo(@Name("operand") String operand) {
		throw new java.lang.RuntimeException("Not implemented yet");
	}

	public Boolean notEqualTo(@Name("operand") String operand) {
		throw new java.lang.RuntimeException("Not implemented yet");
	}
}
