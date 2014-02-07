package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface Initialisable {

	public void initalise() throws java.lang.Exception;

}
