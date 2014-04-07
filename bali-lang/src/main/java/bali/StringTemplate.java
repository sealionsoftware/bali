package bali;

/**
 * User: Richard
 * Date: 31 Mar
 */
public class StringTemplate {

	private char[][] fragments;
	private int fragmentsLength;

	public StringTemplate(char[][] fragments) {
		this.fragments = fragments;
		for (char[] chars : fragments){
			fragmentsLength += chars.length;
		}
	}

	public String produce(char[][] inserts){

		int length = fragmentsLength;
		for(char[] chars : inserts){
			length+= chars.length;
		}

		char[] result = new char[length];

		int i = 0;
		for (char[] fragment : fragments){

		}

		return new CharArrayString(result);
	}
}
