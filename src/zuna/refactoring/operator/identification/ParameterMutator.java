package zuna.refactoring.operator.identification;

import java.util.ArrayList;

import zuna.model.MyMethod;
import zuna.model.MyParameter;

public class ParameterMutator {

	private MyMethod m1, m2;
	
	public ParameterMutator(MyMethod m1, MyMethod m2){
		this.m1 = m1;
		this.m2 = m2;
	}
	
	public ArrayList<MyParameter> getParameterOfm1(){
		return this.m1.getParameters();
	}
	
	public ArrayList<MyParameter> getParameterOfm2(){
		return this.m2.getParameters();
	}

	public MyMethod merge(){
		MyMethod m = null;
		try {
			m = (MyMethod)m2.clone();	
			ArrayList<MyParameter> methodsOfm1 = m1.getParameters();
			
			m.getParameters().addAll(methodsOfm1);
			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

	private Integer[] matchedElements(ArrayList<MyParameter> param1, ArrayList<MyParameter> param2){
		
		Integer[] matched = new Integer[4];
		
		int n11 = getMatchedNumber(param1, this.getParameterOfm1());
		int n12 = getMatchedNumber(param1, this.getParameterOfm2());
		int n21 = getMatchedNumber(param2, this.getParameterOfm1());
		int n22 = getMatchedNumber(param2, this.getParameterOfm2());
		
		if(n11+n22>n12+n21){
			matched[0] = new Integer(n11);
			matched[1] = new Integer(n22);
			matched[2] = new Integer(0);
			matched[3] = new Integer(1);
		}else{
			matched[0] = new Integer(n12);
			matched[1] = new Integer(n21);
			matched[2] = new Integer(1);
			matched[3] = new Integer(0);
		}
		
		
		return matched;
	}

	private int getMatchedNumber(ArrayList<MyParameter> params, ArrayList<MyParameter> cluster) {
		
		int n = 0;
		for(MyParameter m1: params){
			for(MyParameter m2: cluster){
				if(m1.getName().equals(m2.getName())){
					n++;
				}
			}
		}
		return n;
	}
	
	public double getFmeasure(ArrayList<MyParameter> cluster1, ArrayList<MyParameter> cluster2){
		Integer[] matched = this.matchedElements(cluster1, cluster2);
		
		if(matched[2]==0){
			double p1 = (double)matched[0]/(double)cluster1.size();
			double r1 = (double)matched[0]/(double)this.getParameterOfm1().size();
			
			double p2 = (double)matched[1]/(double)cluster2.size();
			double r2 = (double)matched[1]/(double)this.getParameterOfm2().size();
			double p = (p1 + p2)/2;
			double r = (r1 + r2)/2;
			if(Double.isNaN(p) || Double.isNaN(r)|| Double.isInfinite(p)|| Double.isInfinite(r) || (p+r)==0) return 0;
			else return (p*r*2)/(p+r);
		}else{
			double p1 = (double)matched[0]/(double)cluster1.size();
			double r1 = (double)matched[0]/(double)this.getParameterOfm2().size();
			
			double p2 = (double)matched[1]/(double)cluster2.size();
			double r2 = (double)matched[1]/(double)this.getParameterOfm1().size();
			double p = (p1 + p2)/2;
			double r = (r1 + r2)/2;
			if(Double.isNaN(p) || Double.isNaN(r)|| Double.isInfinite(p)|| Double.isInfinite(r) || (p+r)==0) return 0;
			else return (p*r*2)/(p+r);
		}
		
		
		
		
		
	}
	
}