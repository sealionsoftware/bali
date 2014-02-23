package bali.compiler.type;

import bali.annotation.Kind;
import bali.compiler.parser.tree.ClassNode;
import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 19/06/13
 */
public class ClassLibrary {

	private final ClassDeclarationTypeBuilder declarationBuilder = new ClassDeclarationTypeBuilder();
	private final ClasspathTypeBuilder classpathBuilder = new ClasspathTypeBuilder(this);
	private final Map<String, Reference<Class>> types = new HashMap<>();{
		types.put(Object.class.getName(), new SimpleReference<Class>(new MutableClassModel(Object.class.getName(),
				null,
				Collections.<Declaration<Type>>emptyList(),
				Collections.<Type>emptyList(),
				Collections.<Declaration<Site>>emptyList(),
				Collections.<Method>emptyList(),
				Collections.<Operator>emptyList(),
				Collections.<UnaryOperator>emptyList(),
				Collections.<Declaration<Site>>emptyList(),
				Kind.OBJECT
		)));
	}

	public void notifyOfDeclaration(String qualifiedClassName) {
		Reference<Class> reference = new BlockingReference<>();
		types.put(qualifiedClassName, reference);
	}

	public Class addDeclaration(ClassNode declaration) {
		Class ret = declarationBuilder.build(declaration);
		Reference<Class> reference = types.get(declaration.getQualifiedClassName());
		reference.set(ret);
		return ret;
	}

	public synchronized Reference<Class> getReference(String fullyQualifiedClassName) {

		Reference<Class> cached = types.get(fullyQualifiedClassName);
		if (cached != null) {
			return cached;
		}

		cached = new SimpleReference<>();
		types.put(fullyQualifiedClassName, cached);
		try {
			cached.set(classpathBuilder.build(fullyQualifiedClassName));
		} catch (Exception e){
			types.remove(fullyQualifiedClassName);
			throw e;
		}

		return cached;
	}

}
