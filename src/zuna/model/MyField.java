package zuna.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.FieldDeclaration;


public class MyField extends Element implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1165841328034939340L;
	private String type;
	private MyClass parent;
	private ArrayList<MyMethod> refMethod = new ArrayList<MyMethod>();
	private FieldDeclaration fd;
	
	public MyField(String id, String type, MyClass parent, FieldDeclaration fd) {
		super(id, parent == null? false : parent.isLibrary());
		this.type = type;
		this.parent = parent;
		this.fd = fd;
	}
	
	
	
	public void setParent(MyClass parent) {
		this.parent = parent;
	}



	public FieldDeclaration getFd() {
		return fd;
	}



	public String getType() {
		return type;
	}

	public MyClass getParent() {
		return parent;
	}

	public ArrayList<MyMethod> getReferencingMethod() {
		return refMethod;
	}
	
	public void addReferencingMethod(MyMethod m){
		this.refMethod.add(m);
	}
}