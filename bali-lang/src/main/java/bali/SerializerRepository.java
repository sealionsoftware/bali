package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.type.Type;

/**
 * User: Richard
 * Date: 04 Apr
 */
@MetaType(Kind.INTERFACE)
public interface SerializerRepository {

	public Serializer getSerializer(Type t);

}
