package bali.time.standard;

import bali.String;
import bali.time.Interval;
import bali.time.TimeZone;

/**
 * User: Richard
 * Date: 15/07/13
 */
public final class StandardTimeZone implements TimeZone {

	private final String name;
	private final Interval offset;

	public StandardTimeZone(String name, Interval offset) {
		this.name = name;
		this.offset = offset;
	}

	public String getName() {
		return name;
	}

	public Interval getOffset() {
		return offset;
	}
}
