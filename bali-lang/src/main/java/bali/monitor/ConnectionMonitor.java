package bali.monitor;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 07/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface ConnectionMonitor<T> {

	public void waitForConnection() throws java.lang.Exception;

	public T getConnection() throws java.lang.Exception;
}
