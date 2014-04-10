package bali.loader;

import bali.Initialisable;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.ThreadSafe;
import bali.type.Type;

/**
 * User: Richard
 * Date: 21 Mar
 */
@MetaType(Kind.INTERFACE)
public interface ModuleTypeLoader extends Initialisable {

	@Nullable
	@ThreadSafe
	public Type load(@Name("coordinates") String coordinates);

}
