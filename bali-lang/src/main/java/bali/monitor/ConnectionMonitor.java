package bali.monitor;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * A Connection Monitor waits for connections on one thread, then makes the connection
 * available to be picked up by another thread.
 *
 * User: Richard
 * Date: 07/02/14
 */
@MetaType(Kind.INTERFACE)
public interface ConnectionMonitor<T> {

	public void waitForConnection() throws java.lang.Exception;

	public T getConnection() throws java.lang.Exception;
}
