package zuna.metric.coupling;

import java.util.HashSet;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.Repo;


public class CBO extends Metric{

	public CBO(Repo p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getMetric(MyClass c) {
		Hashtable<String, String> value = new Hashtable<String, String>();
		HashSet<MyClass> classes1 = c.getUseClasses();
		HashSet<MyClass> classes2 = c.getUsedClasses();
		
		for(MyClass class1: classes1){
			value.put(class1.getID(), class1.getID());
		}
		
		for(MyClass class2: classes2){
			value.put(class2.getID(), class2.getID());
		}
		
		return value.size();
	}

}
