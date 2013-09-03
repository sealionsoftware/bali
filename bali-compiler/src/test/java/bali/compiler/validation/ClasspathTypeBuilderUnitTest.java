package bali.compiler.validation;

import bali.compiler.validation.type.Class;
import bali.compiler.validation.type.Method;
import bali.compiler.validation.type.Type;
import bali.compiler.validation.types.UnparameterizedObject;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 23/08/13
 */
public class ClasspathTypeBuilderUnitTest {

	private ClasspathTypeBuilder builder = new ClasspathTypeBuilder(new TypeLibrary());

	@Test
	public void testBuildUnparamterizedType(){

		Type type = builder.build(UnparameterizedObject.class.getName());
		Assert.assertEquals(type.getClass(), Class.class);
		Class clazz = (Class) type;

		List<Method> methods = clazz.getMethods();

		Assert.assertEquals(UnparameterizedObject.class.getName(), clazz.getClassName());
		Assert.assertEquals(Collections.emptyList(), clazz.getParameters());
		Assert.assertEquals(Collections.emptyList(), clazz.getImplementations());
		Assert.assertEquals(4, methods.size());

		Method expectation;
		Type returnType = new TypeReference();
		returnType.setClassName(String.class.getName());

		// Method aVoidMethod

		expectation = new Method();
		expectation.setName("aVoidMethod");
		expectation.setType(returnType);
		checkMethod(expectation, methods.get(0));

		// Method aVoidMethodWithArgument

//		method = methods.get(2);
//		Assert.assertEquals("aVoidMethodWithArgument", method.getName());
//		Assert.assertEquals(Boolean.TRUE, method.getFinal());
//		Assert.assertNull(method.getType());
//		Assert.assertEquals(1, method.getArguments().size());
//		Assert.assertNull(method.getOperator());

		// Method aStringMethod

//		method = methods.get(0);


	}

	private void checkMethod(
			Method expectation,
	        Method result
	){
		Assert.assertEquals(expectation.getName(), result.getName());
		Assert.assertEquals(Boolean.TRUE, result.getFinal());
		Assert.assertEquals(expectation.getOperator(), result.getOperator());

		checkType(expectation.getType(), result.getType());

		Assert.assertEquals(expectation.getArguments().size(), result.getArguments().size());
		Iterator<Declaration> expectedArguments = expectation.getArguments().iterator();
		Iterator<Declaration> resultArguments = result.getArguments().iterator();
		while (expectedArguments.hasNext()){
			Declaration expectedArgument = expectedArguments.next();
			Declaration resultArgument = resultArguments.next();
			Assert.assertEquals(expectedArgument.getName(), resultArgument.getName());
			checkType(expectedArgument.getType(), resultArgument.getType());
		}

	}

	private void checkType(
			TypeReference expectation,
			TypeReference result
	){
		Assert.assertTrue(expectation == null ? result == null : result != null);
		Assert.assertEquals(expectation.getClassName(), result.getClassName());
		Iterator<TypeReference> expectedParameters = expectation.getParameters().iterator();
		Iterator<TypeReference> resultParameters = result.getParameters().iterator();
		while (expectedParameters.hasNext()){
			TypeReference expectedParameter = expectedParameters.next();
			TypeReference resultParameter = resultParameters.next();
			checkType(expectedParameter, resultParameter);
		}
		Assert.assertEquals(expectation.getDeclaration(), result.getDeclaration());

	}


}
