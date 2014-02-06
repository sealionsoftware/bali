package bali.io;

import bali.String;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface Host {

	public String shortName();

	public String canonicalName();

	public IPAddress ipAddress();

}
