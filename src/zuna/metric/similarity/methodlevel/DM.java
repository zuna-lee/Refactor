package zuna.metric.similarity.methodlevel;

import java.util.Hashtable;

import zuna.model.MyField;
import zuna.model.MyMethod;


public class DM {

	public double getMetric(MyMethod m1, MyMethod m2){
		
		Hashtable<String, String> entitySet1 = getEntitySet(m1);
		Hashtable<String, String> entitySet2 = getEntitySet(m2);
		int union = this.getUnion(entitySet1, entitySet2);
		int intersection = this.getIntersection(entitySet1, entitySet2);
		
		return (double)intersection/(double)union;
	}
	
	private int getIntersection(Hashtable<String, String> entitySet1, Hashtable<String, String> entitySet2){
		Hashtable<String, String> intersectionSet = new Hashtable<String, String>();
		
		for(String key1: entitySet1.keySet())
		{
			for(String key2: entitySet2.keySet())
			{
				if(key1.equals(key2)) intersectionSet.put(key1, key1);	
			}
		}
		
		return intersectionSet.size();
		
		
	}
	
	private int getUnion(Hashtable<String, String> entitySet1, Hashtable<String, String> entitySet2){
		
		Hashtable<String, String> unionSet = new Hashtable<String, String>();
		
		for(String key1: entitySet1.keySet())
		{
			unionSet.put(key1, key1);
		}
		
		for(String key2: entitySet2.keySet())
		{
			unionSet.put(key2, key2);
		}
		
		return unionSet.size();
	}

	private Hashtable<String, String> getEntitySet(MyMethod m1) {
		Hashtable<String, String> entitySet = new Hashtable<String, String>();
		
		
		for(MyMethod out: m1.getFanOut())
		{
//			if(out.getParent().getID().equals(m1.getParent().getID()))
//			{
				entitySet.put(out.getID(), out.getID());
//			}
		}
		
		for(MyField ref: m1.getReferencedField())
		{
//			if(ref.getParent().getID().equals(m1.getParent().getID()))
//			{
				entitySet.put(ref.getID(), ref.getID());
//			}
		}
		
		entitySet.put(m1.getID(), m1.getID());
		
		return entitySet;
	}
}
