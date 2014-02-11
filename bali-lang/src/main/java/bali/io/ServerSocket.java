package bali.io;

import bali.Closable;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.ThreadSafe;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface ServerSocket extends Closable {

	public Socket getConnection() throws Exception;

}
