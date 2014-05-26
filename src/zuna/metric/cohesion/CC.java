package zuna.metric.cohesion;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.metric.similarity.methodlevel.IAS;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.Repo;


public class CC extends Metric{
	private IAS ias = new IAS();
	
	public CC(Repo p) {
		super(p);
	}

	@Override
	public double getMetric(MyClass c) {
		Hashtable<String, String> t = new Hashtable<String, String>();
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		double similarity = 0.0;
		double allPair = methods.size() * (methods.size()-1)/2;
		for(MyMethod m1: methods){
			for(MyMethod m2: methods){
				String key = getKey(m1.getID(), m2.getID());
				String key2 = getKey(m2.getID(), m1.getID());
				if(!m1.getID().equals(m2.getID()) && !t.containsKey(key) && !t.containsKey(key2)){
					t.put(key, key);
					t.put(key2, key2);
					similarity += this.ias.getIAS(c, m1, m2);
				}
			}
		}

		if(allPair==0) return 0;
		else return similarity/allPair;
	}
}
