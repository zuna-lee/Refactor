package zuna.Repository;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyMethod;

public class CohesiveComponent{
	private Hashtable<Integer, ArrayList<MyMethod>> 
	components = new Hashtable<Integer, ArrayList<MyMethod>>();
	
	public CohesiveComponent(Hashtable<Integer, ArrayList<MyMethod>> components) {
		this.components = components;
	}

	public Hashtable<Integer, ArrayList<MyMethod>> getComponents() {
		return components;
	}

	public void setComponents(Hashtable<Integer, ArrayList<MyMethod>> components) {
		this.components = components;
	}
	
}