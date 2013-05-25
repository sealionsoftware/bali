package bali.compiler;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 01/05/13
 */
public class GeneratedModule {

	private String name;
	private List<GeneratedPackage> packages = new ArrayList<>();

	public GeneratedModule(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<GeneratedPackage> getPackages() {
		return packages;
	}

	public void addPackage(GeneratedPackage generatedPackage){
		packages.add(generatedPackage);
	}
}
