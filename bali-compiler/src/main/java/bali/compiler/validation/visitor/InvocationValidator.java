package bali.compiler.validation.visitor;

import bali.compiler.parser.tree.InvocationNode;
import bali.compiler.type.Method;
import bali.compiler.type.Site;
import bali.compiler.validation.ValidationFailure;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 25/05/13
 */
public class InvocationValidator implements Validator<InvocationNode> {

	public List<ValidationFailure> validate(InvocationNode invocation) {

		List<ValidationFailure> ret = new ArrayList<>();

		Site targetType = invocation.getTarget().getType();
		Method method = getMethodWithName(invocation.getMethod(), targetType);
		if (method == null){
			ret.add(new ValidationFailure(invocation, "Could not resolve method with name " + invocation.getMethod()));
			return ret;
		}
		invocation.setReturnType(method.getType());

		return ret;
	}

	public Method getMethodWithName(String name, Site site) {
		for (Method method : site.getMethods()) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		for (Site iface : site.getInterfaces()){
			Method ret = getMethodWithName(name, iface);
			if (ret != null){
				return ret;
			}
		}
		return null;
	}
}
