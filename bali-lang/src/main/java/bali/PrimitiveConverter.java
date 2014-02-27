package bali;

import bali.annotation.Hidden;

/**
 * User: Richard
 * Date: 26/02/14
 */
public interface PrimitiveConverter {

	public Integer from(int in);

	public int from(Integer in);

	public Boolean from(boolean in);

	public boolean from(Boolean in);

	public String from(char[] in);

}
