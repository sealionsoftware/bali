package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

import java.lang.*;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface Closable {

	public void close() throws java.lang.Exception;

}
