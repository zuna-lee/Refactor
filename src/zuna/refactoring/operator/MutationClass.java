package zuna.refactoring.operator;

import java.util.ArrayList;
import java.util.Vector;

import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.refactoring.ProjectAnalyzer;
import zuna.util.KeyMaker;

public class MutationClass {

	private MyClass c1, c2;
	private MyClass mutationClass;
	
	public MyClass getMutationClass() {
		return mutationClass;
	}

	public MyClass getC1() {
		return c1;
	}

	public MyClass getC2() {
		return c2;
	}
	
	public MutationClass(MyClass c1, MyClass c2){
		try {
			
			this.c1 = (MyClass)c1.clone();
			this.c2 = (MyClass)c2.clone();
			this.merge();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
	}


	
	public ArrayList<MyMethod> getMethodsOfC1(){
		return this.c1.getOwnedMethods();
	}
	
	public ArrayList<MyMethod> getMethodsOfC2(){
		return this.c2.getOwnedMethods();
	}
	
	public ArrayList<MyField> getFieldOfC1(){
		return this.c1.getOwendField();
	}
	

	public ArrayList<MyField> getFieldOfC2(){
		return this.c2.getOwendField();
	}
	
	private MyClass merge(){
		
		try {
			
			mutationClass = (MyClass)c2.clone();
			mutationClass.setID(KeyMaker.getKey(c1, c2));
			ArrayList<MyMethod> methodsOfc1 = c1.getOwnedMethods();
			ArrayList<MyField> fieldsOfc1 = c1.getOwendField();
			mutationClass.getOwnedMethods().addAll(methodsOfc1);
			mutationClass.getOwendField().addAll(fieldsOfc1);
			mutationClass.getUseClasses().addAll(c1.getUseClasses());
			mutationClass.getUsedClasses().addAll(c1.getUsedClasses());

			adjustID();
			
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		return mutationClass;
	}

	private void adjustID(){
		ArrayList<MyMethod> methods = this.mutationClass.getOwnedMethods();
		ArrayList<MyField> fields = this.mutationClass.getOwendField();
		
		for(MyMethod m: methods){
			m.setParent(this.mutationClass);
			if(m.getID().startsWith(this.c1.getID()))m.setID(m.getID().replace(this.c1.getID(), this.mutationClass.getID()));
			else m.setID(m.getID().replace(this.c2.getID(), this.mutationClass.getID()));
		}
		
		for(MyField f: fields){
			f.setParent(this.mutationClass);
			if(f.getID().startsWith(this.c1.getID())) f.setID(f.getID().replace(this.c1.getID(), this.mutationClass.getID()));
			else f.setID(f.getID().replace(this.c2.getID(), this.mutationClass.getID()));
		}
		
	}
	private Integer[] matchedElements(Vector<String> methods1, Vector<String> methods2){
		
		Integer[] matched = new Integer[4];
		
		int n11 = getMatchedNumber(methods1, this.getMethodsOfC1());
		int n12 = getMatchedNumber(methods1, this.getMethodsOfC2());
		int n21 = getMatchedNumber(methods2, this.getMethodsOfC1());
		int n22 = getMatchedNumber(methods2, this.getMethodsOfC2());
		
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

	private int getMatchedNumber(Vector<String> methods, ArrayList<MyMethod> cluster) {
		
		int n = 0;
		for(String m1: methods){
			for(MyMethod m2: cluster){
				if(m1.equals(m2.getID())){
					n++;
				}
			}
		}
		return n;
	}
	
	public double getFmeasure(Vector<String> cluster1, Vector<String> cluster2){
		Integer[] matched = this.matchedElements(cluster1, cluster2);
		
		if(matched[2]==0){
			double p1 = (double)matched[0]/(double)cluster1.size();
			double r1 = (double)matched[0]/(double)this.getMethodsOfC1().size();
			
			double p2 = (double)matched[1]/(double)cluster2.size();
			double r2 = (double)matched[1]/(double)this.getMethodsOfC2().size();
			double p = (p1 + p2)/2;
			double r = (r1 + r2)/2;
			if(Double.isNaN(p) || Double.isNaN(r)|| Double.isInfinite(p)|| Double.isInfinite(r) || (p+r)==0) return 0;
			else return (p*r*2)/(p+r);
		}else{
			double p1 = (double)matched[0]/(double)cluster1.size();
			double r1 = (double)matched[0]/(double)this.getMethodsOfC2().size();
			
			double p2 = (double)matched[1]/(double)cluster2.size();
			double r2 = (double)matched[1]/(double)this.getMethodsOfC1().size();
			double p = (p1 + p2)/2;
			double r = (r1 + r2)/2;
			if(Double.isNaN(p) || Double.isNaN(r)|| Double.isInfinite(p)|| Double.isInfinite(r) || (p+r)==0) return 0;
			else return (p*r*2)/(p+r);
		}
	}
	
	private void filterSupportingMethods(MyClass c) {
		ArrayList<MyMethod> remove = new ArrayList<MyMethod>();
		
		for(int i = 0 ; i < c.getOwnedMethods().size() ; i++){
			MyMethod m = c.getOwnedMethods().get(i);
			if(!(m.isConstructor() || m.isSupport() || m.isLibrary() || m.getStatementCnt()<=1 || this.isObjMethods(m))) 
				remove.add(m);
		}

		c.setOwnedMethods(remove);
	}
	
	private boolean isObjMethods(MyMethod m){
		MyClass objClass = ProjectAnalyzer.project.getClass("java.lang.Object");
		ArrayList<MyMethod> objMethod = objClass.getOwnedMethods();
		for(MyMethod obj: objMethod){
			if(m.getID().endsWith(obj.getID())) return true;
		}
		
		return false;
	}
	
}