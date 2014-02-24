package bali.compiler.type;

import bali.compiler.reference.BlockingReference;
import bali.compiler.reference.Reference;
import bali.compiler.reference.SimpleReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Richard
 * Date: 15/10/13
 */
public class ConstantLibrary {

	private Map<String, Reference<List<Declaration<Site>>>> constants = new HashMap<>();
	private PackageConstantsBuilder constantsBuilder;

	public ConstantLibrary(ClassLibrary library) {
		constantsBuilder = new PackageConstantsBuilder(library);
	}

	public void notifyOfPackage(String name) {
		Reference<List<Declaration<Site>>> reference = constants.get(name);
		if (reference == null){
			reference = new BlockingReference<>();
			constants.put(name, reference);
		}
	}

	public void addPackageConstants(String name, List<Declaration<Site>> declarations) {
		Reference<List<Declaration<Site>>> reference = constants.get(name);
		reference.set(declarations);
	}

	public List<Declaration<Site>> getConstants(String fullyQualifiedPackageName) {
		Reference<List<Declaration<Site>>> reference = constants.get(fullyQualifiedPackageName);
		if (reference != null) {
			return reference.get();
		}
		List<Declaration<Site>> packageConstants = constantsBuilder.buildPackageConstants(fullyQualifiedPackageName);
		constants.put(fullyQualifiedPackageName, new SimpleReference<>(packageConstants));
		return packageConstants;
	}

}
