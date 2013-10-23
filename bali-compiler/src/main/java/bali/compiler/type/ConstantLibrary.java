package bali.compiler.type;

import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.reference.Semaphore;
import bali.compiler.reference.SimpleReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 15/10/13
 */
public class ConstantLibrary {

	private Map<String, Reference<List<Declaration>>> constants = new HashMap<>();
	private PackageConstantsBuilder constantsBuilder;

	public ConstantLibrary(TypeLibrary library) {
		constantsBuilder = new PackageConstantsBuilder(library);
	}

	public void notifyOfPackage(String name) {
		Reference<List<Declaration>> reference = constants.get(name);
		if (reference == null){
			reference = new BlockingReference<>();
			constants.put(name, reference);
		}
	}

	public void addPackageConstants(String name, List<Declaration> declarations) {
		Reference<List<Declaration>> reference = constants.get(name);
		reference.set(declarations);
	}

	public List<Declaration> getConstants(String fullyQualifiedPackageName) {
		Reference<List<Declaration>> reference = constants.get(fullyQualifiedPackageName);
		if (reference != null) {
			return reference.get();
		}
		List<Declaration> packageConstants = constantsBuilder.buildPackageConstants(fullyQualifiedPackageName);
		constants.put(fullyQualifiedPackageName, new SimpleReference<>(packageConstants));
		return packageConstants;
	}

}
