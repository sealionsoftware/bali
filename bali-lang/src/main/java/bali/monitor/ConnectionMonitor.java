package bali.monitor;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 07/02/14
 */
@MetaType(Kind.INTERFACE)
public interface ConnectionMonitor<T> {

	public void waitForConnection() throws java.lang.Exception;

	public T getConnection() throws java.lang.Exception;
}
