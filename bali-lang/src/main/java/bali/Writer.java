package bali;

import com.sealionsoftware.bali.CharArrayString;

/**
 * User: Richard
 * Date: 07/07/13
 */
public interface Writer {

	@Operator("<<")
	public void writeLine(CharArrayString in);

}
