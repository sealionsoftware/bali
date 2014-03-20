package bali.type;

import bali.collection.Collection;
import bali.collection.LinkedList;
import bali.collection.List;
import bali.collection._;

import java.util.HashMap;
import java.util.Map;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 19/03/14
 */
public final class TypeFactory {

	private static final Map<String, Type> TYPES = new HashMap<>();

	public static synchronized Type getType(String signature){

		Type t = TYPES.get(signature);
		if (t != null){
			return t;
		}

		int start = signature.indexOf('[');
		if (start < 0){
			t =  new LazyReflectedType(convert(signature), (Collection<Type>) _.EMPTY);
		} else {
			int end = signature.lastIndexOf(']');
			if (end >= signature.length()){
				throw new RuntimeException("Invalid signature: " + signature);
			}
			String[] argumentSignatures = signature.substring(start + 1, end).split(",");
			List<Type> arguments = new LinkedList<>(null, null);
			for (String argumentSignature : argumentSignatures){
				arguments.add(getType(argumentSignature)); // TODO - canonicalize self references
			}
			t = new LazyReflectedType(convert(signature.substring(0, start)), arguments);
		}
		TYPES.put(signature, t);
		return t;

	}

}
