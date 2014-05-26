package zuna.model;

import java.io.Serializable;
import java.util.List;

import org.eclipse.jdt.core.dom.Modifier;

public abstract class Element implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6095461841857069444L;
	protected String ID;
	private boolean isLibrary=false;
	private List<Modifier> modifier;
	private double se;
	private double ic;
	private double test;
	
	public double getSe() {
		return se;
	}

	public void setSe(double se) {
		this.se = se;
	}

	public double getIc() {
		return ic;
	}

	public void setIc(double ic) {
		this.ic = ic;
	}

	public boolean isLibrary() {
		return isLibrary;
	}

	protected Element(String uri, boolean isLib) {
		super();
		this.ID = uri;
		this.isLibrary = isLib;
	}

	public String getID() {
		return ID;
	}
	
	public void display() {
		System.out.println("Element [" + getID() + "]");
	}

	public List<Modifier> getModifier() {
		return modifier;
	}
	
	
	public void setModifier(List<Modifier> modifier) {
		this.modifier = modifier;
	}
	
	public void setLibary(boolean b) {
		this.isLibrary = b;
		
	}
}
