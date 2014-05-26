package zuna.metric.coupling;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.Repo;


public class RFC extends Metric{

	public RFC(Repo p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getMetric(MyClass c) {
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		Hashtable<String, String> rs = new Hashtable<String, String>();
		
		for(MyMethod m: methods){
			ArrayList<MyMethod> fanout = m.getFanOut();
			for(MyMethod mo: fanout){
				rs.put(mo.getID(), mo.getID());
			}
		}
		
		return methods.size() + rs.size();
	}

}
