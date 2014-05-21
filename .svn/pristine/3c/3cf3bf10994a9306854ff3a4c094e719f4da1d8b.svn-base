package zuna.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class MyParameter  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4163343036096635695L;

	private MyMethod parent;
	
	private String type;
	private String name;
	
	public MyParameter(MyMethod parent, String type, String name) {
		super();
		this.parent = parent;
		this.type = type;
		this.name = name;
	}

	public static ArrayList<MyParameter> createParameters(MyMethod parent, List<SingleVariableDeclaration> singleVariableDeclarations) {

		ArrayList<MyParameter> list = new ArrayList<MyParameter>();
		
		for (SingleVariableDeclaration singleVariableDeclaration : singleVariableDeclarations) {
		
			String type = singleVariableDeclaration.getType().toString();
			String name = singleVariableDeclaration.getName().toString();
			
			list.add(new MyParameter(parent, type, name));
		}
		
		return list;
	}

	public MyMethod getParent() {
		return parent;
	}

	public void setParent(MyMethod parent) {
		this.parent = parent;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyParameter other = (MyParameter) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
