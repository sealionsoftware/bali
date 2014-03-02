package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Operator;
import bali.collection.Collection;
import bali.collection.ValueCollection;

/**
 * User: Richard
 * Date: 15/07/13
 */
@MetaType(Kind.INTERFACE)
public interface String extends Collection<Character>, Value<String> {

	@Operator("^^")
	public String uppercase();

	@Operator("/")
	public ValueCollection<String> split(@Name("separator") String seperator);

	public Boolean contains(@Name("value") Character value);

}
