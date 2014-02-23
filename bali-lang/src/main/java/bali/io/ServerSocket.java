package bali.io;

import bali.Closable;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface ServerSocket extends Closable {

	public Socket getConnection() throws Exception;

}
