package zuna.metric.cohesion;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.Repo;


public class LCOM2 extends Metric{

	public LCOM2(Repo p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getMetric(MyClass c) {
		double connected = 0;
		double unconnected = 0;
		
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		
		for(int i = 0 ; i < methods.size() ; i++){
			for(int j = i+1 ; j < methods.size() ; j++){
				MyMethod m1 = methods.get(i);
				MyMethod m2 = methods.get(j);
				if(!this.isConnected(m1, m2)) unconnected+=1.0;
				else connected+=1.0;
			}
		}
		
		if(unconnected - connected > 0) return unconnected - connected;
		else return 0;
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
