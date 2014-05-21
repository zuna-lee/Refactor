package zuna.metric.cohesion;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.Repo;


public class LCOM1 extends Metric{

	public LCOM1(Repo p) {
		super(p);
	}

	@Override
	public double getMetric(MyClass c) {
		double metric = 0;
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		
		
		for(int i = 0 ; i < methods.size()-1 ; i++){
			for(int j = i+1 ; j < methods.size() ; j++){
				MyMethod m1 = methods.get(i);
				MyMethod m2 = methods.get(j);
				if(!this.isConnected(m1, m2)) metric+=1.0;
			}
		}
		metric = metric/(((double)methods.size() * ((double)methods.size()-1))/(double)2);
		if(methods.size()<1) return 1;
		return metric;
	}
	
	private boolean isConnected(MyMethod m1, MyMethod m2){
		ArrayList<MyField> files1 = m1.getReferencedField();
		ArrayList<MyField> files2 = m2.getReferencedField();
		MyClass owningClass = m1.getParent();
		boolean conn = false;
		for(MyField f1: files1){
			for(MyField f2: files2){
				if(f1.getParent().getID().equals(owningClass.getID())
						&& f2.getParent().getID().equals(owningClass.getID())
						&& f1.getID().equals(f2.getID())){
					conn = true;
					break;
				}
			}
			if(conn) break;
		}
		return conn;
	}
	
}
