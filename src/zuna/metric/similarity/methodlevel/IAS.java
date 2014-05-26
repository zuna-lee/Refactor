package zuna.metric.similarity.methodlevel;


import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;



public class IAS {


	
	public double getAttRefCounting(MyMethod m, MyClass c){
		double count = 0.0;
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		ArrayList<MyField> f = m.getReferencedField();
		
		for(MyMethod m1: methods){
			ArrayList<MyField> fields = m1.getReferencedField();
			boolean flg=false;
			for(MyField f1: fields){
				for(MyField f2: f){
					if(f1.getID().equals(f2.getID())){
						count+=count+1;
						flg=true;
						break;
					}
				}
				if(flg) break;
			}
			if(flg) break;
		}
		
		return count/(double)methods.size();
	}
	
	
	public double getIAS(MyClass c, MyMethod m1, MyMethod m2){
		double metric = 0.0;
		double totAttribute = this.getUnion(m1, m2).keySet().size();
		Hashtable<String, String> pairs = new Hashtable<String, String>();
		
		for(MyField f1: m1.getReferencedField()){
			for(MyField f2: m2.getReferencedField()){
				if(f1.getID().equals(f2.getID())){
					pairs.put(f1.getID(), f1.getID());
				}
			}
		}
		
		if(totAttribute==0 || pairs.keySet().size()==0){
			metric = 0.0;
		}else{
			metric = pairs.keySet().size() / totAttribute;
		}

		if(Double.isInfinite(metric)) metric=  0.0;
		if(Double.isNaN(metric)) metric = 0.0;
		
		return metric;
	}
	
	private Hashtable<String, MyField> getUnion(MyMethod m1, MyMethod m2){
		ArrayList<MyField> fs1 = m1.getReferencedField();
		ArrayList<MyField> fs2 = m2.getReferencedField();
		Hashtable<String, MyField> union = new Hashtable<String, MyField>();
		
		for(MyField f1: fs1){
			for(MyField f2: fs2){
				if(!union.containsKey(f1.getID())){
					union.put(f1.getID(), f1);
				}
				
				if(!union.containsKey(f2.getID())){
					union.put(f2.getID(), f2);	
				}
			}
		}
		
		return union;
	}
	
	private Hashtable<String, MyField> getList(ArrayList<MyField> methods){
		Hashtable<String, MyField> list = new Hashtable<String, MyField>();
		for(MyField m: methods){
			list.put(m.getID(), m);
		}
		return list;
	}
	
}
