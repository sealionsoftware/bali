package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 04/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Executable {

	public void execute() throws Throwable;

}
