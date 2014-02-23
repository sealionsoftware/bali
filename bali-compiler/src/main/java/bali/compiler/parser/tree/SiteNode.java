package bali.compiler.parser.tree;


import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class SiteNode extends Node {

	private String className;
	private List<SiteNode> parameters = new ArrayList<>();
	private boolean nullable = false;
	private boolean threadSafe = false;

	private BlockingReference<Site> site = new BlockingReference<>();

	public SiteNode() {
	}

	public SiteNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public boolean getThreadSafe() {
		return threadSafe;
	}

	public void setThreadSafe(boolean threadSafe) {
		this.threadSafe = threadSafe;
	}

	public List<SiteNode> getParameters() {
		return parameters;
	}

	public void addParameter(SiteNode parameter) {
		children.add(parameter);
		this.parameters.add(parameter);
	}

	public Site getSite() {
		return site.get();
	}

	public void setSite(Site site) {
		this.site.set(site);
	}

//	public boolean equals(OBJECT o) {
//
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//
//		SiteNode other = (SiteNode) o;
//
//		if (!getSite().getName().equals(other.getSite().getName())) {
//			return false;
//		}
//
//		if (){
//
//		}
//
//		return parameters.equals(other.parameters);
//	}

	public String toString() {

		StringBuilder sb = new StringBuilder(className);
		if (parameters.size() > 0) {
			sb.append("<");
			Iterator<SiteNode> i = parameters.iterator();
			sb.append(i.next());
			while (i.hasNext()) {
				sb.append(",").append(i.next());
			}
			sb.append(">");
		}
		if (nullable){
			sb.append("?");
		}
		return sb.toString();
	}

}
