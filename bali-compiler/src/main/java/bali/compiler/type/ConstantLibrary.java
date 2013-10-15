package bali.compiler.type;

import bali.compiler.reference.Semaphore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 15/10/13
 */
public class ConstantLibrary {

	private Map<String, List<Declaration>> constants = new HashMap<>();
	private PackageConstantsBuilder constantsBuilder;

	private Semaphore constantsComplete = new Semaphore();

	public ConstantLibrary(TypeLibrary library) {
		constantsBuilder = new PackageConstantsBuilder(library);
	}

	public void constantsComplete() {
		constantsComplete.release();
	}

	public void checkConstantsComplete() {
		constantsComplete.check();
	}

	public void addConstant(String name, Declaration declaration) {
		List<Declaration> declarations = constants.get(name);
		if (declarations == null){
			declarations = new ArrayList<>();
			constants.put(name, declarations);
		}
		declarations.add(declaration);
	}

	public List<Declaration> getConstants(String fullyQualifiedPackageName) {
		List<Declaration> cached = constants.get(fullyQualifiedPackageName);
		if (cached != null) {
			return cached;
		}
		List<Declaration> packageConstants = constantsBuilder.buildPackageConstants(fullyQualifiedPackageName);
		constants.put(fullyQualifiedPackageName, packageConstants);
		return packageConstants;
	}

}
