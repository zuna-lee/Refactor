package zuna.metric.similarity.classlevel;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;


public class Classintimacy {

	
	public double getDistanceOfClasses(MyClass c1, MyClass c2){
		ArrayList<MyMethod> methods1 = c1.getOwnedMethods();
		ArrayList<MyMethod> methods2 = c2.getOwnedMethods();
		ArrayList<MyField> attribute1 = c2.getOwendField();
		ArrayList<MyField> attribute2 = c2.getOwendField();
		Hashtable<String, String> refered = new Hashtable<String, String>();
		
		for(MyMethod m1: methods1){
			ArrayList<MyMethod> fanouts = m1.getFanOut();
			for(MyMethod fanout: fanouts){
				if(fanout.getParent().getID().equals(c2.getID())){
					refered.put(m1.getID(), m1.getID());
					refered.put(fanout.getID(), fanout.getID());
				}
			}
		}
		
		for(MyMethod m2: methods1){
			ArrayList<MyMethod> fanouts = m2.getFanOut();
			for(MyMethod fanout: fanouts){
				if(fanout.getParent().getID().equals(c1.getID())){
					refered.put(m2.getID(), m2.getID());
					refered.put(fanout.getID(), fanout.getID());
				}
			}
		}
		
		for(MyField f1: attribute1){
			ArrayList<MyMethod> refMethod = f1.getReferencingMethod();
			for(MyMethod ref: refMethod){
				if(ref.getParent().getID().equals(c2.getID())){
					refered.put(ref.getID(), ref.getID());
					refered.put(f1.getID(), f1.getID());
				}
			}
		}
		
		for(MyField f2: attribute2){
			ArrayList<MyMethod> refMethod = f2.getReferencingMethod();
			for(MyMethod ref: refMethod){
				if(ref.getParent().getID().equals(c2.getID())){
					refered.put(ref.getID(), ref.getID());
					refered.put(f2.getID(), f2.getID());
				}
			}
		}
		
		double referedSize = refered.keySet().size();
		double allSize = methods1.size() + methods2.size() + attribute1.size() + attribute2.size(); 
		double intimacy = referedSize / allSize;
		if(Double.isInfinite(intimacy)) intimacy = 0.0;
		if(Double.isNaN(intimacy)) intimacy = 0.0;
		
		return intimacy; 
	}
	
}
