package bali.time;

import bali.*;
import bali.Number;

/**
 * User: Richard
 * Date: 15/07/13
 */
public interface Date {
	bali.String getEpoch();

	Number getYear();

	Number getMonth();

	Number getDay();
}
