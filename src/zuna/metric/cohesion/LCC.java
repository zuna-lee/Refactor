package zuna.metric.cohesion;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.Repo;


public class LCC extends Metric{

	private static Hashtable<String, MyField> fields = new Hashtable<String, MyField>();
	private static Hashtable<String, String> refMethodList = new Hashtable<String, String>();
	
	public LCC(Repo p) {
		super(p);
	}

	@Override
	public double getMetric(MyClass c) {
		
		Hashtable<String, String> t = new Hashtable<String, String>();
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		double connected = 0;
		double allPair = methods.size() * (methods.size()-1)/2;
		for(MyMethod m1: methods){
			for(MyMethod m2: methods){
				String key = getKey(m1.getID(), m2.getID());
				String key2 = getKey(m2.getID(), m1.getID());
				if(!m1.getID().equals(m2.getID()) && !t.containsKey(key) && !t.containsKey(key2)){
					t.put(key, key);
					t.put(key2, key2);
					if(this.isConnected(m1, m2)) connected += 1.0;
				}
				
			}
			
		}
		if(allPair==0) return 0;
		else if(connected == 0) return 0;
		else return connected/allPair;
	}
	
	private boolean isConnected(MyMethod m1, MyMethod m2){
		MyClass owningClass = m1.getParent();
		Hashtable<String, MyField> files1 = this.getHashtable(m1.getReferencedField());
		Hashtable<String, MyField> files2 = this.getHashtable(m2.getReferencedField());
		files1 = addIndirectFields(m1, files1, owningClass);
		files2 = addIndirectFields(m2, files2, owningClass);
		
		boolean conn = false;
		for(String key: files1.keySet()){
			MyField f1 = files1.get(key);
			for(String key2: files2.keySet()){
				MyField f2 = files2.get(key2);
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

	private Hashtable<String, MyField> addIndirectFields(MyMethod m1,
									Hashtable<String, MyField> files1, MyClass owningClass) {
		
		fields.clear();
		refMethodList.clear();
		fields.putAll(files1);
		refMethodList.put(m1.getID(), m1.getID());
		this.addIndirectlyReferedField(owningClass, m1);
		Hashtable<String, MyField> fieldsTmp = new Hashtable<String, MyField>();
		fieldsTmp.putAll(fields);
		fields.clear();
		refMethodList.clear();
		
		return fieldsTmp;
	}
	
	private Hashtable<String, MyField> getHashtable(ArrayList<MyField> fields){
		Hashtable<String, MyField> fieldList = new Hashtable<String, MyField>();
		
		for(MyField f: fields){
			fieldList.put(f.getID(), f);
		}
		
		return fieldList;
	}

	private void addIndirectlyReferedField(MyClass c, MyMethod m){
		ArrayList<MyField> refFields =  m.getReferencedField();
		
		for(MyField f: refFields){
			fields.put(f.getID(), f);
			ArrayList<MyMethod> refMethods = f.getReferencingMethod();
			for(MyMethod refM: refMethods){
				if(!refMethodList.containsKey(refM.getID())){
					refMethodList.put(refM.getID(), refM.getID());
					this.addIndirectlyReferedField(c, refM);
				}
			}
		}
	}
}
