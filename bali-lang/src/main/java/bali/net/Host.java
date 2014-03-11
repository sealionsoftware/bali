package bali.net;

import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface Host {

	public String shortName();

	public String canonicalName();

	public IPAddress ipAddress();

}
