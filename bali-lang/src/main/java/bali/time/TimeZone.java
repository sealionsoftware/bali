package bali.time;

import bali.String;

/**
 * User: Richard
 * Date: 11/07/13
 */
public interface TimeZone {

	public String getName();

	public Interval getOffset();

}
