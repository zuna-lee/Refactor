package zuna.metric.cohesion;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.Repo;


public class DCD extends Metric{

	private static Hashtable<String, MyField> fields = new Hashtable<String, MyField>();
	private static Hashtable<String, String> temp = new Hashtable<String, String>();
	
	public DCD(Repo p) {
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
		ArrayList<MyMethod> fanout1 = m1.getFanOut();
		ArrayList<MyMethod> fanout2 = m2.getFanOut();
		
		boolean conn = false;
		for(String key: files1.keySet()){
			MyField f1 = files1.get(key);
			for(String key2: files2.keySet()){
				MyField f2 = files2.get(key2);
				if(f1.getParent().getID().equals(owningClass.getID())
						&& f2.getParent().getID().equals(owningClass.getID())
						&& f1.getID().equals(f2.getID())){
					conn = true;
				}
			}
		}
		
		for(MyMethod out1: fanout1){
			for(MyMethod out2: fanout2){
				if((out1.getParent().getID().equals(owningClass.getID()) &&
						out2.getParent().getID().equals(owningClass.getID()))
						&& out1.getID().equals(out2.getID())){
					conn= true;
				}
			}
		}
		
		
		return conn;
	}

	private Hashtable<String, MyField> addIndirectFields(MyMethod m1,
									Hashtable<String, MyField> files1, MyClass owningClass) {
		
		fields.clear();
		fields.putAll(files1);
		temp.clear();
		this.addIndirectlyReferedField(owningClass, m1, m1);
		Hashtable<String, MyField> fieldsTmp = new Hashtable<String, MyField>();
		fieldsTmp.putAll(fields);
		fields.clear();
		
		return fieldsTmp;
	}
	
	private Hashtable<String, MyField> getHashtable(ArrayList<MyField> fields){
		Hashtable<String, MyField> fieldList = new Hashtable<String, MyField>();
		
		for(MyField f: fields){
			fieldList.put(f.getID(), f);
		}
		
		return fieldList;
	}

	private void addIndirectlyReferedField(MyClass c, MyMethod m, MyMethod orig){
		temp.put(m.getID(), m.getID());
		for(MyMethod om: m.getFanOut()){
			if(!m.getID().equals(om.getID()) && om.getParent().getID().equals(c.getID())){
				ArrayList<MyField> fs = om.getReferencedField();
				for(MyField mf: fs){
					if(mf.getParent().getID().equals(c.getID())) fields.put(mf.getID(), mf);
				}
				
				for(MyMethod om2: om.getFanOut()){
					if(!temp.containsKey(om2.getID())
							&&!om.getID().equals(om2.getID()) 
							&& !om.getID().equals(m.getID()) 
							&& !om2.getID().equals(m.getID())
							&& !orig.getID().equals(om.getID()) 
							&& !orig.getID().equals(om2.getID()) 
							&& om2.getParent().getID().equals(c.getID())){
						this.addIndirectlyReferedField(c, om2, orig);
					}
				}
			}
			
		}
	}
}
