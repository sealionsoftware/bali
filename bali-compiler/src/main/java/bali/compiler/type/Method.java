package bali.compiler.type;

import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
public class Method extends Declaration<Site> {

	private List<Declaration<Site>> parameters;

	public Method(String name, Site returnType, List<Declaration<Site>> parameters) {
		super(name, returnType);
		this.parameters = parameters;
	}

	public List<Declaration<Site>> getParameters() {
		return parameters;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder(super.toString());
		if (parameters.size() > 0){
			sb.append("(");
			Iterator<Declaration<Site>> i = parameters.iterator();
			sb.append(i.next());
			while (i.hasNext()){
				sb.append(",").append(i.next());
			}
			sb.append(")");
		}
		return sb.toString();
	}
}
