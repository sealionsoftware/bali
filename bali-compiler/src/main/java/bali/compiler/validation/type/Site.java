package bali.compiler.validation.type;

import bali.compiler.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/08/13
 */
public class Site extends MethodDeclaringType {

	public Site(MethodDeclaringType type, List<Site> parameters) {
		super(
				type.getClassName(),
				parametriseParameters(type.getParameters(), parameters),
				parametriseMethods(type.getMethods(), parameters),
				parametriseInterfaces(type.getInterfaces(), parameters)
		);
	}

	private static List<Declaration> parametriseParameters(List<Declaration> parameterDeclarations, List<Site> parameterArguments) {

		if (parameterDeclarations.size() != parameterArguments.size()){
			throw new RuntimeException("Invalid Parameterization: " + parameterArguments + " => " + parameterDeclarations);
		}

		List<Declaration> ret = new ArrayList<>(parameterDeclarations.size());
		Iterator<Site> i = parameterArguments.iterator();
		for (Declaration declaration : parameterDeclarations){
			Site site = i.next();
			if (!site.isAssignableTo(declaration.getType())){
				throw new RuntimeException("Parameter argument is not within site type");
			}
			ret.add(new Declaration(
					declaration.getName(),
					site
			));
		}

		return ret;
	}

	private static List<Site> parametriseInterfaces(List<Site> interfaces, List<Site> parameterArguments){

		List<Site> ret = new ArrayList<>();
		for (Site iface : interfaces){
			ret.add(parametriseSite(iface, parameterArguments));
		}
		return ret;
	}

	private static List<Method> parametriseMethods(List<Method> methods, List<Site> parameterArguments){

		List<Method> ret = new ArrayList<>();
		for (Method method : methods){

			List<Declaration> parametrisedArgumentDeclarations = new ArrayList<>();
			for (Declaration argumentDeclaration : method.getArguments()){
				parametrisedArgumentDeclarations.add(
						new Declaration(
								argumentDeclaration.getName(),
								parametriseSite(argumentDeclaration.getType(), parameterArguments)
						)
				);
			}

			ret.add(new Method(
					method.getName(),
					parametriseSite(method.getType(), parameterArguments),
					parametrisedArgumentDeclarations
			));

		}
		return ret;
	}

	private static Site parametriseSite(Site original, List<Site> parameterArguments){

		Site ret = retrieveSiteWithName(original.getClassName(), parameterArguments);
		if (ret != null){
			return ret;
		}

		List<Site> parametrisedArguments = new ArrayList<>();
		for (Site argument: parameterArguments){
			parameterArguments.add(parametriseSite(argument, parameterArguments));
		}

		return new Site(
				original,
				parametrisedArguments
		);
	}

	private static Site retrieveSiteWithName(String name, List<Site> from){
		for (Site site : from){
			if (site.getClassName().equals(name)){
				return site;
			}
		}
		return null;
	}

	private boolean isAssignableTo(Type t){

		if (getClassName().equals(t.getClassName())){
			Iterator<Declaration> i = t.getParameters().iterator();
			for (Declaration argument : getParameters()){
				Declaration parameter = i.next();
				if (!argument.getType().isAssignableTo(parameter.getType())){
					return false;
				}
			}
		}

		for (Site iface : getInterfaces()){
			if (iface.isAssignableTo(t)){
				return true;
			}
		}

		return false;
	}


}
