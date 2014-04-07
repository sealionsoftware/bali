package bali;

import bali.net.IPAddress;
import bali.type.Type;
import bali.type.TypeFactory;

import java.util.HashMap;
import java.util.Map;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 04 Apr
 */
public class DefaultSerializerRepository implements SerializerRepository {

	private Map<Type, Serializer> seralizers = new HashMap<>();

	public DefaultSerializerRepository() {
		seralizers.put(TypeFactory.getType(Boolean.class.getName()), bali.logic._.BOOLEAN_SERIALIZER);
		seralizers.put(TypeFactory.getType(Number.class.getName()), bali.number._.NUMBER_SERIALIZER);
		seralizers.put(TypeFactory.getType(IPAddress.class.getName()), bali.net._.IPADDRESS_SERIALIZER);
	}

	public Serializer getSerializer(Type t) {

		Serializer ret = seralizers.get(t);
		if (ret != null){
			return ret;
		}

		synchronized (this){
			ret = seralizers.get(t);
			if (ret != null){
				return ret;
			}

			for (Map.Entry<Type, Serializer> entry : seralizers.entrySet()){
				Type key = entry.getKey();
				Serializer serializer = entry.getValue();
				if (convert(t.assignableTo(key))){
					seralizers.put(t, serializer);
					return serializer;
				}
			}
		}

		return null;
	}
}
