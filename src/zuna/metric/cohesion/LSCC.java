package zuna.metric.cohesion;

import java.util.ArrayList;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.Repo;

public class LSCC extends Metric{

	private ArrayList<MyField> fields = new ArrayList<MyField>();
	
	public LSCC(Repo p) {
		super(p);
	}

	public LSCC(Repo p, MyClass c) {
		super(p);
		this.fields = c.getOwendField();
	}
	
	
	@Override
	public double getMetric(MyClass c) {
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		this.fields = c.getOwendField();
		if(this.fields.size()==0) return 0;
		else if((this.fields.size()>=1 && methods.size()==0)|| methods.size()==1) return 1;
		else{
			double ns = 0;
			double cnt = 0;
			for(int i = 0 ; i < methods.size()-1 ; i++){
				for(int j = i+1 ; j < methods.size() ; j++){
					ns+=this.getMetric(methods.get(i), methods.get(j));
					cnt++;
				}
			}
			return ns/cnt;
		}
	}
	


	private double getNoOfCoReferredField(ArrayList<MyField> referredField1,
			ArrayList<MyField> referredField2) {
		double cnt = 0;
		for(MyField rf1: referredField1){
			for(MyField rf2: referredField2){
				if(rf1.getID().equals(rf2.getID())){
					cnt+=1.0;
				}
			}
		}
		
		return cnt;
	}


	private ArrayList<MyField> getNotReferredField(MyMethod m, ArrayList<MyField> referredField) {
		ArrayList<MyField> notReferredField = new ArrayList<MyField>();
		boolean isReferred = false;
		
		for(MyField f: this.fields){
			for(MyField rf: referredField){
				if(f.getID().equals(rf.getID()) 
						&& f.getParent().getID().equals(rf.getParent().getID())) isReferred = true;
			}
			if(!isReferred) notReferredField.add(f);
			isReferred = false;
		}
		
		return notReferredField;
	}


	public double getMetric(MyMethod m1, MyMethod m2) {
		ArrayList<MyField> referredField1 = m1.getReferencedField();
		ArrayList<MyField> referredField2 = m2.getReferencedField();
		ArrayList<MyField> notReferredField1 = this.getNotReferredField(m1, referredField1);
		ArrayList<MyField> notReferredField2 = this.getNotReferredField(m2, referredField2);
		double correlated = 0.0;
		
		correlated+=this.getNoOfCoReferredField(referredField1, referredField2);
//		correlated+=this.getNoOfCoReferredField(notReferredField1, notReferredField2);
		
		if((double)this.fields.size()==0) return 0.0;
		else return correlated/(double)this.fields.size();
	}

}
