package bali.loader;

import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.collection.Collection;
import bali.collection.Map;
import bali.type.Declaration;

/**
 * User: Richard
 * Date: 21 Mar
 */
@MetaType(Kind.INTERFACE)
public interface ModuleLoader<T> {

	@Nullable
	public T load(@Name("coordinates") String coordinates, @Name("parameters") Map<String, String> parameters);

	@Nullable
	public Collection<Declaration> getParameters(String coordinates);
}
