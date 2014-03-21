package bali.type;

import bali.Boolean;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 18/03/14
 */
@MetaType(Kind.BEAN)
public class Declaration {

	public String name;
	public Type type;
	public Boolean nullable = Boolean.FALSE;

	@Parameters
	public Declaration(@Name("name") String name, @Name("name") Type type, @Name("nullable") @Nullable Boolean nullable) {
		this.name = name;
		this.type = type;
		if (nullable != null){
			this.nullable = nullable;
		}
	}
}
