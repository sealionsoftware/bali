package com.sealionsoftware.json;

import bali.Iterator;
import bali.Serializer;
import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Parameters;
import bali.collection.Collection;
import bali.compiler.parser.JsonLexer;
import bali.compiler.parser.JsonParser;
import bali.number.NumberFactory;
import bali.type.Declaration;
import bali.type.Type;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static bali.Primitive.convert;
import static bali.compiler.parser.JsonParser.JsonObjectContext;
import static bali.compiler.parser.JsonParser.JsonArrayContext;
import static bali.compiler.parser.JsonParser.JsonBooleanContext;
import static bali.compiler.parser.JsonParser.JsonNumberContext;
import static bali.compiler.parser.JsonParser.JsonStringContext;
import static bali.compiler.parser.JsonParser.JsonValueContext;
import static bali.compiler.parser.JsonParser.MemberContext;

/**
 * User: Richard
 * Date: 16/03/14
 */
@MetaType(Kind.OBJECT)
public class JSONSerializer<T> implements Serializer<T> {

	public Type T;

	private Deque<Object> references = new LinkedList<>();

	@Parameters
	public JSONSerializer(@Name("T") Type T) {
		this.T = T;
	}

	public bali.String format(@Name("in") T in) {
		StringBuilder sb = new StringBuilder();
		format(in, sb, 0);
		sb.append("\n");
		return convert(sb.toString());
	}

	private void format(Object in, StringBuilder sb, int indent) {

		if (in == null) {
			return;
		}

		if (in instanceof bali.String){
			format((bali.String) in, sb);
		} else if (in instanceof bali.Boolean){
			format((bali.Boolean) in, sb);
		} else if (in instanceof bali.Integer){
			format((bali.Integer) in, sb);
		} else {

			if (references.contains(in)){
				throw new RuntimeException("This bean cannot be serialized as the structure it describes is self referencing");
			}
			references.push(in);

			if (in instanceof bali.collection.Collection){
				format((Collection) in, sb, indent);
			} else {
				formatBean(in, sb, indent);
			}
			references.pop();
		}

	}

	private void format(String in, StringBuilder sb) {
		sb.append("\"")
				.append(in)
				.append("\"");
	}

	private void format(bali.String in, StringBuilder sb) {
		format(convert(in), sb);
	}

	private void format(bali.Boolean in, StringBuilder sb) {
		sb.append(convert(in) ? "true" : "false");
	}

	private void format(bali.Integer in, StringBuilder sb) {
		sb.append(convert(in));
	}

	private void formatBean(Object in, StringBuilder sb, int indent) {

		try {
			sb.append("{");
			indent++;
			boolean first = true;
			for (Field field : in.getClass().getFields()){
				if ((field.getModifiers() & Modifier.STATIC) == 0){
					Object value = field.get(in);
					if (value != null){
						if (first){
							first = false;
						} else {
							sb.append(",");
						}
						sb.append("\n");
						indent(sb, indent);
						format(field.getName(), sb);
						sb.append(": ");
						format(value, sb, indent);
					}
				}
			}
			sb.append("\n");
			indent(sb, --indent);
			sb.append("}");

		} catch(Exception e){
			throw new RuntimeException("Could not format bean", e);
		}

	}

	private void indent(StringBuilder sb, int indent){
		while (indent-- > 0){
			sb.append("\t");
		}
	}

	private void format(Collection<?> in, StringBuilder sb, int indent) {

		sb.append("[");
		indent++;
		Iterator i = in.iterator();
		boolean first = true;
		while (convert(i.hasNext())){
			if (first){
				first = false;
			} else {
				sb.append(",");
			}
			sb.append("\n");
			indent(sb, indent);
			format(i.next(), sb, indent);
		}
		sb.append("\n");
		indent(sb, --indent);
		sb.append("]");
	}



	public T parse(@Name("serialization") bali.String serialization) {

		try {

			ANTLRInputStream input = new ANTLRInputStream(new StringReader(convert(serialization)));
			Lexer lexer = new JsonLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			tokens.fill();
			JsonParser parser = new JsonParser(tokens);
			parser.setErrorHandler(new DefaultErrorStrategy());

			return create(T, parser.jsonObject());

		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}

	private <C> C create(Type type, JsonObjectContext context){

		try {

			Class<C> clazz = (Class<C>) Thread.currentThread().getContextClassLoader().loadClass(convert(type.getClassName()));

			Map<String, JsonValueContext> hashedValues = new HashMap<>();
			for (MemberContext member : context.member()){
				hashedValues.put(trimString(member.STRING()), member.jsonValue());
			}

			Constructor<C> constructor = getParametersConstructor(clazz);

			Collection<Declaration> parameters = type.getParameters();
			Object[] arguments = new Object[convert(parameters.size())];
			Iterator<Declaration> i = parameters.iterator();
			int j = 0;
			while (convert(i.hasNext())){
				Declaration parameter = i.next();
				JsonValueContext valueContext = hashedValues.get(convert(parameter.name));
				if (valueContext == null){
					if (!convert(parameter.nullable)){
						throw new RuntimeException("Parameter " + parameter.name + " is required");
					}
					arguments[j++] = null;
				} else {
					arguments[j++] = create(parameter.type, valueContext);
				}
			}

			return constructor.newInstance(arguments);

		} catch (Exception e) {
			throw new RuntimeException("Could not create object of type " + type.getClassName(), e);
		}
	}

	private String trimString(TerminalNode string){
		String text = string.getText();
		return text.substring(1, text.length() - 1);
	}

	private <C> Constructor<C> getParametersConstructor(Class<C> clazz){
		Constructor<C>[] constructors = (Constructor<C>[]) clazz.getConstructors();
		for (Constructor<C> constructor : constructors){
			if (constructor.isAnnotationPresent(Parameters.class)){
				return constructor;
			}
		}
		throw new RuntimeException("Class " + clazz + " has no @Parameters constructor and is not a valid bali type");
	}

	private Object create(Type type, JsonValueContext context){
		//TODO: need "is assignable" method on Type
		String className = convert(type.getClassName());
		Collection<Type> typeArguments = type.getTypeArguments();
		if ("bali.String".equals(className)){
			JsonStringContext jsonStringContext = context.jsonString();
			if (jsonStringContext == null){
				throw new RuntimeException("Serialized property is of incorrect type");
			}
			return convert(trimString(jsonStringContext.STRING()));
		}
		if ("bali.Boolean".equals(className)){
			JsonBooleanContext jsonBooleanContext = context.jsonBoolean();
			if (jsonBooleanContext == null){
				throw new RuntimeException("Serialized property is of incorrect type");
			}
			return convert("true".equals(jsonBooleanContext.getText()));
		}
		if ("bali.Number".equals(className) || "bali.Integer".equals(className)){
			JsonNumberContext jsonNumberContext = context.jsonNumber();
			if (jsonNumberContext == null){
				throw new RuntimeException("Serialized property is of incorrect type");
			}
			return NumberFactory.NUMBER_FACTORY.forDecimalString(jsonNumberContext.NUMBER().getText().toCharArray());
		}
		if ("bali.collection.Map".equals(className)){
			JsonArrayContext jsonArrayContext = context.jsonArray();
			if (jsonArrayContext == null){
				throw new RuntimeException("Serialized property is of incorrect type");
			}
			bali.collection.Map ret = new bali.collection.HashMap();
			for (JsonValueContext jsonValue : jsonArrayContext.jsonValue()){
				JsonObjectContext entry = jsonValue.jsonObject();
				Type keyType = typeArguments.get(convert(1));
				Type valueType = typeArguments.get(convert(2));
				Value key = null;
				Object value = null;
				for (MemberContext memberContext : entry.member()){
					String entryPropertyName = trimString(memberContext.STRING());
					if ("key".equals(entryPropertyName)){
						key = (Value) create(keyType, memberContext.jsonValue());
					} else if ("value".equals(entryPropertyName)){
						value =  create(valueType, memberContext.jsonValue());
					}
				}
				if (key != null){
					ret.put(key, value);
				}
			}
			return ret;
		}
		if ("bali.collection.Collection".equals(className)){
			JsonArrayContext jsonArrayContext = context.jsonArray();
			if (jsonArrayContext == null){
				throw new RuntimeException("Serialized property is of incorrect type");
			}
			bali.collection.List<Object> ret = new bali.collection.LinkedList<>();
			Type elementType = typeArguments.get(convert(1));
			for (JsonValueContext value : jsonArrayContext.jsonValue()){
				ret.add(create(elementType, value));
			}
			return ret;
		}
		if (context.jsonObject() != null){
			return create(type, context.jsonObject());
		}
		throw new RuntimeException("Could not create object");
	}
}
