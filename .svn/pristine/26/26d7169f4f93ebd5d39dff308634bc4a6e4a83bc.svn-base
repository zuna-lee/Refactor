package zuna.metric.similarity.methodlevel;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyMethod;


public class IMS extends IDC{

	public double getIMS(MyMethod m1, MyMethod m2){
		int cntOut=0;
		int cntIn=0;
		
		ArrayList<MyMethod> fanouts1 = m1.getFanOut();
		ArrayList<MyMethod> fanins1 = m1.getFanIn();
		ArrayList<MyMethod> fanouts2 = m2.getFanOut();
		ArrayList<MyMethod> fanins2 = m2.getFanIn();
		
		int unionOfOut = getUnion(m1, fanouts1, fanouts2);
		int unionOfIn = getUnion(m1, fanins1, fanins2);
		
		cntOut = getIntersection(m1, fanouts1, fanouts2);
		cntIn = getIntersection(m1, fanins1, fanins2);
		
		double ims1 = (double)cntOut/(double)unionOfOut;
		double ims2 = (double)cntIn/(double)unionOfIn;

		if(Double.isNaN(ims1) || Double.isInfinite(ims1)) ims1 = 0.0;
		if(Double.isNaN(ims2) || Double.isInfinite(ims2)) ims2 = 0.0;
		
		return (ims1+ims2)/2;
	}

	private int getIntersection(MyMethod m1, ArrayList<MyMethod> fanouts1, ArrayList<MyMethod> fanouts2) {
		int cntIntersection=0;
		Hashtable<String, MyMethod> outList1 = getList(fanouts1);
		Hashtable<String, MyMethod> outList2 = getList(fanouts2);
		
		for(String key1: outList1.keySet()){
			MyMethod out1 = outList1.get(key1);
			for(String key2: outList2.keySet()){
				MyMethod out2 = outList2.get(key2);
				if(out1.getParent().getID().equals(m1.getParent().getID()) 
						&& out2.getParent().getID().equals(m1.getParent().getID())
						&& out1.getID().equals(out2.getID())){
					cntIntersection++;
				}
			}
			
		}
		return cntIntersection;
	}
	
	private Hashtable<String, MyMethod> getList(ArrayList<MyMethod> methods){
		Hashtable<String, MyMethod> list = new Hashtable<String, MyMethod>();
		for(MyMethod m: methods){
			list.put(m.getID(), m);
		}
		return list;
	}
	
	private int getUnion(MyMethod m, ArrayList<MyMethod> array1, ArrayList<MyMethod> array2){
		Hashtable<String, MyMethod> union = new Hashtable<String, MyMethod>();
		
		for(MyMethod m1: array1){
			if(m1.getParent().getID().equals(m.getParent().getID()))
				union.put(m1.getID(), m1);
		}
		
		for(MyMethod m2: array2){
			if(m2.getParent().getID().equals(m.getParent().getID()))
				union.put(m2.getID(), m2);
		}
		
		return union.size();
	}
	
}
