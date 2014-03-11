package bali.compiler.type;

import bali.annotation.Kind;
import bali.compiler.bytecode.TestSite;
import bali.compiler.bytecode.TestVariableSite;
import junit.framework.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 23/08/13
 */
public class ClasspathClassBuilderUnitTest {

	private ClassLoader loader = Thread.currentThread().getContextClassLoader();
	private ClassLibrary library = new ClassLibrary(loader);
	private ClasspathTypeBuilder builder = new ClasspathTypeBuilder(library, loader);

	@Test
	public void testBuildVanillaType() {

		Class aClass = builder.build(VanillaObject.class.getName());

		Assert.assertTrue(Kind.OBJECT.equals(aClass.getMetaType()));

		List<Method> methods = aClass.getMethods();
		List<Type> interfaces = aClass.getInterfaces();

		Assert.assertEquals(VanillaObject.class.getName(), aClass.getName());
		Assert.assertEquals(Collections.<Declaration<Type>>emptyList(), aClass.getTypeParameters());
		Assert.assertEquals(Collections.<Operator>emptyList(), aClass.getOperators());
		Assert.assertEquals(Collections.<UnaryOperator>emptyList(), aClass.getUnaryOperators());
		Assert.assertEquals(1, interfaces.size());
		Assert.assertEquals(3, methods.size());

		checkType(new TestSite(VanillaInterface.class), interfaces.get(0));

		Method expectation;
		Site expectationSite = new TestSite(bali.String.class);
		Declaration<Site> expectationDeclaration = new Declaration<>("parameter", expectationSite);

		// Method aVoidMethod

		expectation = new Method("aVoidMethod", null, Collections.<Declaration<Site>>emptyList());
		checkMethod(expectation, methods.get(0));

//		Method aVoidMethodWithArgument

		expectation = new Method("aVoidMethodWithParameter", null, Arrays.asList(expectationDeclaration));
		checkMethod(expectation, methods.get(1));

//		Method aStringMethod

		expectation = new Method("aStringMethod", expectationDeclaration.getType(), Collections.<Declaration<Site>>emptyList());
		checkMethod(expectation, methods.get(2));

	}

	@Test
	public void testBuildUnparametrisedInterface() {

		Class aClass = builder.build(UnparameterizedInterface.class.getName());

		Assert.assertEquals(Kind.INTERFACE, aClass.getMetaType());

		List<Method> methods = aClass.getMethods();
		List<Operator> operators = aClass.getOperators();
		List<UnaryOperator> unaryOperators = aClass.getUnaryOperators();

		Assert.assertEquals(UnparameterizedInterface.class.getName(), aClass.getName());
		Assert.assertEquals(Collections.<Declaration<Type>>emptyList(), aClass.getTypeParameters());
		Assert.assertEquals(Collections.<Type>emptyList(), aClass.getInterfaces());
		Assert.assertEquals(3, methods.size());
		Assert.assertEquals(2, operators.size());
		Assert.assertEquals(2, unaryOperators.size());

		Method methodExpectation;
		Operator operatorExpectation;
		UnaryOperator unaryOperatorExpectation;
		Site expectationSite = new TestSite(bali.String.class);
		Declaration<Site> expectationDeclaration = new Declaration<>("argument", expectationSite);

		// Method aVoidMethod

		methodExpectation = new Method("aVoidMethod", null, Collections.<Declaration<Site>>emptyList());
		checkMethod(methodExpectation, methods.get(0));

		// Method aVoidMethodWithArgument

		methodExpectation = new Method("aVoidMethodWithArgument", null, Collections.singletonList(expectationDeclaration));
		checkMethod(methodExpectation, methods.get(1));

		// Method aStringMethod

		methodExpectation = new Method("aStringMethod", expectationSite, Collections.<Declaration<Site>>emptyList());
		checkMethod(methodExpectation, methods.get(2));

		// Operator aVoidOperator

		operatorExpectation = new Operator("$", null, expectationSite, "aVoidOperator");
		checkOperator(operatorExpectation, operators.get(0));

		// Operator aStringOperator

		operatorExpectation = new Operator("%", expectationSite, expectationSite, "aStringOperator");
		checkOperator(operatorExpectation, operators.get(1));

		// UnaryOperator aVoidUnaryOperator

		unaryOperatorExpectation = new UnaryOperator("!", null, "aVoidUnaryOperator");
		checkUnaryOperator(unaryOperatorExpectation, unaryOperators.get(0));

		// UnaryOperator aStringUnaryOperator

		unaryOperatorExpectation = new UnaryOperator("£", expectationSite, "aStringUnaryOperator");
		checkUnaryOperator(unaryOperatorExpectation, unaryOperators.get(1));

	}

	@Test
	public void testBuildParametrisedObject() {

		Class aClass = builder.build(ParametrizedObject.class.getName());

		Assert.assertEquals(Kind.OBJECT, aClass.getMetaType());
		List<Method> methods = aClass.getMethods();

		Assert.assertEquals(ParametrizedObject.class.getName(), aClass.getName());
		Assert.assertEquals(1, aClass.getTypeParameters().size());
		Assert.assertEquals(Collections.<Type>emptyList(), aClass.getInterfaces());
		Assert.assertEquals(2, methods.size());

		Method methodExpectation;
		Site expectationSite = new TestSite(B.class, Arrays.<Site>asList(new TestVariableSite("T", null)));

		Declaration typeParameterDeclaration = aClass.getTypeParameters().get(0);
		Assert.assertEquals("T", typeParameterDeclaration.getName());
		checkType(expectationSite, typeParameterDeclaration.getType());

		expectationSite = new TestVariableSite("T", expectationSite);
		Declaration<Site> expectationDeclaration = new Declaration<>("t", expectationSite);

		// Method getT

		methodExpectation = new Method("getT", expectationSite, Collections.<Declaration<Site>>emptyList());
		checkMethod(methodExpectation, methods.get(0));

		// Method setT

		methodExpectation = new Method("setT", null, Collections.singletonList(expectationDeclaration));
		checkMethod(methodExpectation, methods.get(1));

	}

	@Test
	public void testInterfaceInheritance() {

		Class aClass = builder.build(SubInterface.class.getName());

		Assert.assertEquals(Kind.INTERFACE, aClass.getMetaType());

		List<Type> interfaces = aClass.getInterfaces();

		Assert.assertEquals(SubInterface.class.getName(), aClass.getName());
		Assert.assertEquals(Collections.<Declaration<Type>>emptyList(), aClass.getTypeParameters());
		Assert.assertEquals(1, interfaces.size());

		checkType(new TestSite(SuperInterface.class), interfaces.get(0));
	}

	@Test
	public void testParameterizedInterfaceInheritance() {

		Class aClass = builder.build(ParameterizedSubInterface.class.getName());

		Assert.assertEquals(Kind.INTERFACE, aClass.getMetaType());

		List<Type> interfaces = aClass.getInterfaces();

		Assert.assertEquals(ParameterizedSubInterface.class.getName(), aClass.getName());
		Assert.assertEquals(Collections.<Declaration<Type>>emptyList(), aClass.getTypeParameters());
		Assert.assertEquals(1, interfaces.size());

		checkType(new TestSite(ParameterizedSuperInterface.class, Arrays.<Site>asList(new TestVariableSite("T", new TestSite(C.class)))), interfaces.get(0));
	}

	private void checkMethod(
			Method expectation,
			Method result
	) {
		Assert.assertEquals(expectation.getName(), result.getName());

		checkType(expectation.getType(), result.getType());

		Assert.assertEquals(expectation.getParameters().size(), result.getParameters().size());
		Iterator<Declaration<Site>> expectedArguments = expectation.getParameters().iterator();
		Iterator<Declaration<Site>> resultArguments = result.getParameters().iterator();
		while (expectedArguments.hasNext()) {
			Declaration expectedArgument = expectedArguments.next();
			Declaration resultArgument = resultArguments.next();
			Assert.assertEquals(expectedArgument.getName(), resultArgument.getName());
			checkType(expectedArgument.getType(), resultArgument.getType());
		}
	}

	private void checkUnaryOperator(
			UnaryOperator expectation,
			UnaryOperator result
	) {
		Assert.assertEquals(expectation.getName(), result.getName());
		Assert.assertEquals(expectation.getMethodName(), result.getMethodName());
		checkType(expectation.getType(), result.getType());
	}

	private void checkOperator(
			Operator expectation,
			Operator result
	) {
		Assert.assertEquals(expectation.getName(), result.getName());
		Assert.assertEquals(expectation.getMethodName(), result.getMethodName());
		checkType(expectation.getType(), result.getType());
		checkType(expectation.getParameter(), result.getParameter());
	}

	private void checkType(
			Type expectation,
			Type result
	) {
		if (expectation == null) {
			Assert.assertNull(result);
			return;
		}
		Assert.assertNotNull(result);
		Assert.assertEquals(expectation.getTemplate(), result.getTemplate());
		Iterator<Declaration<Site>> expectedParameters = expectation.getParameters().iterator();
		Iterator<Declaration<Site>> resultParameters = result.getParameters().iterator();
		while (expectedParameters.hasNext()) {
			Declaration expectedParameter = expectedParameters.next();
			Declaration resultParameter = resultParameters.next();
			Assert.assertEquals(expectedParameter.getName(), resultParameter.getName());
			checkType(expectedParameter.getType(), resultParameter.getType());
		}

	}


}
