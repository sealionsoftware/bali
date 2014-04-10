package bali.monitor;

import bali.Initialisable;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * A Resource Pool holds a pool of resources for use by clients
 *
 * User: Richard
 * Date: 10 Apr
 */
@MetaType(Kind.INTERFACE)
public interface ResourcePool<T> extends Initialisable {

	public T getResource();

}
