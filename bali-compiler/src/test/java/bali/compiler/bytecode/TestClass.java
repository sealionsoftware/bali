package bali.compiler.bytecode;

import bali.annotation.Kind;
import bali.compiler.type.Declaration;
import bali.compiler.type.Method;
import bali.compiler.type.Operator;
import bali.compiler.type.Site;
import bali.compiler.type.Type;
import bali.compiler.type.UnaryOperator;

import java.lang.reflect.Constructor;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 06/03/14
 */
public class TestClass implements bali.compiler.type.Class {

	private Class delegate;

	public TestClass(Class delegate) {
		this.delegate = delegate;
	}

	public String getName() {
		return delegate.getName();
	}

	public List<Type> getSuperTypes() {
		return Collections.emptyList();
	}

	public List<Declaration<Type>> getTypeParameters() {
		List<Declaration<Type>> ret = new ArrayList<>();
		for (TypeVariable v : delegate.getTypeParameters()){
			ret.add(new Declaration<>(
				v.getName(),
				null
			));
		}
		return ret;
	}

	public List<Declaration<Site>> getParameters() {
		Constructor[] constructors = delegate.getConstructors();
		if (constructors.length == 0){
			return Collections.emptyList();
		}
		Constructor c = delegate.getConstructors()[0];
		List<Declaration<Site>> ret = new ArrayList<>();
		for(Class type : c.getParameterTypes()){
			ret.add(new Declaration<Site>(
					"test",
					new TestSite(type)
			));
		}
		return ret;
	}

	public List<Operator> getOperators() {
		return Collections.emptyList();
	}

	public List<UnaryOperator> getUnaryOperators() {
		return Collections.emptyList();
	}

	public List<Declaration<Site>> getProperties() {
		return Collections.emptyList();
	}

	public List<Type> getInterfaces() {
		return Collections.emptyList();
	}

	public List<Method> getMethods() {
		java.lang.reflect.Method[] methods = delegate.getMethods();
		List<Method> ret = new ArrayList<>(methods.length);
		for (java.lang.reflect.Method method : methods){
			List<Declaration<Site>> parameters = new ArrayList<>();
			for (Class parameterType : method.getParameterTypes()){
				parameters.add(new Declaration<Site>(
					null,
					new TestSite(parameterType)
				));
			}
			ret.add(new Method(
					method.getName(),
					new TestSite(method.getReturnType()),
					parameters
			));
		}
		return ret;
	}

	public Kind getMetaType() {
		return Kind.OBJECT;
	}

	public Method getMethod(String name) {
		return null;
	}

	public String toString(){
		return delegate.getName();
	}

	public boolean equals(Object o) {
		return o instanceof bali.compiler.type.Class && ((bali.compiler.type.Class) o).getName().equals(getName());
	}
}
