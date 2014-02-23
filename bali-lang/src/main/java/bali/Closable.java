package bali;

import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface Closable {

	public void close() throws java.lang.Exception;

}
