package bali.compiler.type;

import bali.compiler.bytecode.TestVanillaSite;
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
public class ClasspathTypeBuilderUnitTest {

	private ClasspathTypeBuilder builder = new ClasspathTypeBuilder(new TypeLibrary());

	@Test
	public void testBuildVanillaType() {

		Type type = builder.build(VanillaObject.class.getName());

		Assert.assertFalse(type.isAbstract());

		List<Method> methods = type.getMethods();
		List<Site> interfaces = type.getInterfaces();

		Assert.assertEquals(VanillaObject.class.getName(), type.getName());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getTypeParameters());
		Assert.assertEquals(Collections.<Operator>emptyList(), type.getOperators());
		Assert.assertEquals(Collections.<UnaryOperator>emptyList(), type.getUnaryOperators());
		Assert.assertEquals(1, interfaces.size());
		Assert.assertEquals(3, methods.size());

		checkType(new TestVanillaSite(VanillaInterface.class), interfaces.get(0));

		Method expectation;
		Site expectationSite = new TestVanillaSite(bali.String.class);
		Declaration expectationDeclaration = new Declaration("parameter", expectationSite);

		// Method aVoidMethod

		expectation = new Method("aVoidMethod", null, Collections.<Declaration>emptyList());
		checkMethod(expectation, methods.get(0));

//		Method aVoidMethodWithArgument

		expectation = new Method("aVoidMethodWithParameter", null, Arrays.asList(expectationDeclaration));
		checkMethod(expectation, methods.get(1));

//		Method aStringMethod

		expectation = new Method("aStringMethod", expectationDeclaration.getType(), Collections.<Declaration>emptyList());
		checkMethod(expectation, methods.get(2));

	}

	@Test
	public void testBuildUnparametrisedInterface() {

		Type type = builder.build(UnparameterizedInterface.class.getName());

		Assert.assertTrue(type.isAbstract());

		List<Method> methods = type.getMethods();
		List<Operator> operators = type.getOperators();
		List<UnaryOperator> unaryOperators = type.getUnaryOperators();

		Assert.assertEquals(UnparameterizedInterface.class.getName(), type.getName());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getTypeParameters());
		Assert.assertEquals(Collections.<Site>emptyList(), type.getInterfaces());
		Assert.assertEquals(3, methods.size());
		Assert.assertEquals(2, operators.size());
		Assert.assertEquals(2, unaryOperators.size());

		Method methodExpectation;
		Operator operatorExpectation;
		UnaryOperator unaryOperatorExpectation;
		Site expectationSite = new TestVanillaSite(bali.String.class);
		Declaration expectationDeclaration = new Declaration("argument", expectationSite);

		// Method aVoidMethod

		methodExpectation = new Method("aVoidMethod", null, Collections.<Declaration>emptyList());
		checkMethod(methodExpectation, methods.get(0));

		// Method aVoidMethodWithArgument

		methodExpectation = new Method("aVoidMethodWithArgument", null, Collections.singletonList(expectationDeclaration));
		checkMethod(methodExpectation, methods.get(1));

		// Method aStringMethod

		methodExpectation = new Method("aStringMethod", expectationSite, Collections.<Declaration>emptyList());
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

		Type type = builder.build(ParametrizedObject.class.getName());

		Assert.assertFalse(type.isAbstract());
		List<Method> methods = type.getMethods();

		Assert.assertEquals(ParametrizedObject.class.getName(), type.getName());
		Assert.assertEquals(1, type.getTypeParameters().size());
		Assert.assertEquals(Collections.<Site>emptyList(), type.getInterfaces());
		Assert.assertEquals(2, methods.size());

		Method methodExpectation;
		Site expectationSite = new TestVanillaSite(B.class);

		Declaration typeParameterDeclaration = type.getTypeParameters().get(0);
		Assert.assertEquals("T", typeParameterDeclaration.getName());
		checkType(expectationSite, typeParameterDeclaration.getType());

		expectationSite = new TestVariableSite("T", new TestVanillaSite(B.class));
		Declaration expectationDeclaration = new Declaration("t", expectationSite);

		// Method getT

		methodExpectation = new Method("getT", expectationSite, Collections.<Declaration>emptyList());
		checkMethod(methodExpectation, methods.get(0));

		// Method setT

		methodExpectation = new Method("setT", null, Collections.singletonList(expectationDeclaration));
		checkMethod(methodExpectation, methods.get(1));

	}

	@Test
	public void testInterfaceInheritance() {

		Type type = builder.build(SubInterface.class.getName());

		Assert.assertTrue(type.isAbstract());

		List<Method> methods = type.getMethods();
		List<Site> interfaces = type.getInterfaces();
		List<Operator> operators = type.getOperators();
		List<UnaryOperator> unaryOperators = type.getUnaryOperators();

		Assert.assertEquals(SubInterface.class.getName(), type.getName());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getTypeParameters());
		Assert.assertEquals(1, interfaces.size());
//		Assert.assertEquals(1, methods.size());
//		Assert.assertEquals(1, operators.size());
//		Assert.assertEquals(1, unaryOperators.size());

		checkType(new TestVanillaSite(SuperInterface.class), interfaces.get(0));
//		checkMethod(new Method("aMethod", null, Collections.<Declaration>emptyList()), methods.get(0));
//		checkUnaryOperator(new UnaryOperator("#", null, "anUnaryOperator"), unaryOperators.get(0));
//		checkOperator(new Operator("~", null, new TestVanillaSite(bali.String.class), "anOperator"), operators.get(0));
	}

	@Test
	public void testParameterizedInterfaceInheritance() {

		Type type = builder.build(ParameterizedSubInterface.class.getName());

		Assert.assertTrue(type.isAbstract());

		List<Method> methods = type.getMethods();
		List<Site> interfaces = type.getInterfaces();
		List<Operator> operators = type.getOperators();
		List<UnaryOperator> unaryOperators = type.getUnaryOperators();

		Site expectationSite = new TestVanillaSite(D.class);

		Assert.assertEquals(ParameterizedSubInterface.class.getName(), type.getName());
		Assert.assertEquals(Collections.<Declaration>emptyList(), type.getTypeParameters());
		Assert.assertEquals(1, interfaces.size());
//		Assert.assertEquals(1, methods.size());
//		Assert.assertEquals(1, operators.size());
//		Assert.assertEquals(1, unaryOperators.size());

		checkType(new TestVanillaSite(ParameterizedSuperInterface.class), interfaces.get(0));
//		checkMethod(new Method("aMethod", expectationSite, Collections.<Declaration>emptyList()), methods.get(0));
//		checkUnaryOperator(new UnaryOperator("#", expectationSite, "anUnaryOperator"), unaryOperators.get(0));
//		checkOperator(new Operator("~", null, expectationSite, "anOperator"), operators.get(0));
	}

	private void checkMethod(
			Method expectation,
			Method result
	) {
		Assert.assertEquals(expectation.getName(), result.getName());

		checkType(expectation.getType(), result.getType());

		Assert.assertEquals(expectation.getParameters().size(), result.getParameters().size());
		Iterator<Declaration> expectedArguments = expectation.getParameters().iterator();
		Iterator<Declaration> resultArguments = result.getParameters().iterator();
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
			Site expectation,
			Site result
	) {
		if (expectation == null) {
			Assert.assertNull(result);
			return;
		}
		Assert.assertNotNull(result);
		Assert.assertEquals(expectation.getName(), result.getName());
		Iterator<Declaration> expectedParameters = expectation.getParameters().iterator();
		Iterator<Declaration> resultParameters = result.getParameters().iterator();
		while (expectedParameters.hasNext()) {
			Declaration expectedParameter = expectedParameters.next();
			Declaration resultParameter = resultParameters.next();
			Assert.assertEquals(expectedParameter.getName(), resultParameter.getName());
			checkType(expectedParameter.getType(), resultParameter.getType());
		}
		Assert.assertEquals(expectation.getType(), result.getType());

	}


}
