package zuna.metric.cohesion;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import zuna.Repository.CohesiveComponent;
import zuna.Repository.RMGRepo;
import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.model.Repo;
import zuna.refactoring.ProjectAnalyzer;

public class LCOM4 extends Metric {

	private int[][] matrix;
	private boolean[] visited;
	private int numOfComponents;
	private static Vector<Integer> idx = new Vector<Integer>();
	private Hashtable<Integer, ArrayList<MyMethod>> components = new Hashtable<Integer, ArrayList<MyMethod>>();
	
	
	public LCOM4(Repo p) {
		super(p);
	}

	
	public Hashtable<Integer, ArrayList<MyMethod>> getComponents() {
		return components;
	}


	@Override
	public double getMetric(MyClass c) {
		
		if (c.getOwnedMethods().size() == 0)
			return 0.0;
		
		// visited.clear();
		matrix = new int[c.getOwnedMethods().size()][c.getOwnedMethods().size()];
		visited = new boolean[c.getOwnedMethods().size()];

		for (boolean b : visited) {
			b = false;
		}

		getGraph(c);
		numOfComponents = 0;
		for (int i = 0; i < matrix[0].length; i++) {
			if (!visited[i]) {
				
				numOfComponents++;
				depthFirstSearchForConnectedComponent(i);
				Vector<Integer> component = idx;
				ArrayList<MyMethod> methods = new ArrayList<MyMethod>();
				
				for(int index: component){
					methods.add(c.getOwnedMethods().get(index));
				}
				
				this.components.put(numOfComponents, methods);
				idx = new Vector<Integer>();
			}
		}
		return numOfComponents;
	}

	public void identifyRMGs(MyClass c) {
		double noOfComponent = this.getMetric(c);
		
		if(noOfComponent < 2){
			ProjectAnalyzer.project.getClassList().get(c.getID()).setCompletelyCohesive(true);
		}
		
		RMGRepo.componentOfClasses.put(c.getID(), new CohesiveComponent(components));
	}
	
	private void depthFirstSearchForConnectedComponent(int index) {
		visited[index] = true;
		idx.add(index);
		for (int i = 0; i < matrix[0].length; i++) {
			if (matrix[index][i] > 0) {
				if (!visited[i]) {
					depthFirstSearchForConnectedComponent(i);
				}
			}
		}
		return;
	}

	private boolean isConnected(MyMethod m1, MyMethod m2) {
		boolean internal = isRelated(m1, m2);
		boolean child = false;
		
		if(!internal) child = isConnectedInAllChild(m1, m2);
		return internal || child;
	}
	
	private boolean isConnectedInAllChild(MyMethod m1, MyMethod m2){
		return parentClass(m1, m2) || interfaceClasses(m1, m2);
	}

	
	private boolean interfaceClasses(MyMethod m1, MyMethod m2) {
		MyClass c = m1.getParent();
		ArrayList<MyClass> implemented = c.getImplementedClasses();
		int cnt = 0;
				
		if(!m1.isConstructor() && !m2.isConstructor()){
			for(MyClass child: implemented){
				ArrayList<MyMethod> methods = child.getOwnedMethods();
				MyMethod cm1 = null, cm2 = null;
				for(MyMethod m: methods){
					if(this.getMethodName(m).equals(this.getMethodName(m1))) cm1 = m;
					if(this.getMethodName(m).equals(this.getMethodName(m2))) cm2 = m;
				}
				
				if(cm1!=null && cm2!=null){
					if(this.isRelated(cm1, cm2)) {
						cnt++;
					}
				}
				
			}
		}
		return cnt>0 && cnt==implemented.size();
	}
	
	private boolean parentClass(MyMethod m1, MyMethod m2) {
		MyClass c = m1.getParent();
		ArrayList<MyClass> childClasses = c.getChildClasses();
		int cnt = 0;
				
		
		if(!m1.isConstructor() && !m2.isConstructor()){
			for(MyClass child: childClasses){
				ArrayList<MyMethod> methods = child.getOwnedMethods();
				MyMethod cm1 = null, cm2 = null;
				for(MyMethod m: methods){
					if(this.getMethodName(m).equals(this.getMethodName(m1))) cm1 = m;
					if(this.getMethodName(m).equals(this.getMethodName(m2))) cm2 = m;
				}
				
				if(cm1!=null && cm2!=null){
					if(this.isRelated(cm1, cm2)) {
						cnt++;
					}
				}
				
			}
		}
		
		return cnt>0 && cnt==childClasses.size();
	}
	
	private String getMethodName(MyMethod m){
		String[] methodName = m.getID().split("\\.");
		return methodName[methodName.length-1];
	}


	private boolean isRelated(MyMethod m1, MyMethod m2){
		ArrayList<MyField> fields1 = m1.getReferencedField();
		ArrayList<MyField> fields2 = m2.getReferencedField();

		ArrayList<MyMethod> fanout = m1.getFanOut();
		ArrayList<MyMethod> fanin = m1.getFanIn();

		MyClass owningClass = m1.getParent();
		boolean IAS = false, IDC = false, child = false;
		
		IAS = shareAttributes(fields1, fields2, owningClass);
		if(!IAS) IDC = hasCallDependency(m2, fanout, fanin);

		return IAS || IDC;
	}
	
	private boolean shareAttributes(ArrayList<MyField> files1,
			ArrayList<MyField> files2, MyClass owningClass) {
		
		for (MyField f1 : files1) {
			for (MyField f2 : files2) {
				if (f1.getParent().getID().equals(owningClass.getID())
						&& f2.getParent().getID().equals(owningClass.getID())
						&& f1.getID().equals(f2.getID())) {
					return true;
				}
			}
		}
		return false;
	}


	private boolean hasCallDependency(MyMethod m2, ArrayList<MyMethod> fanout,
			ArrayList<MyMethod> fanin) {
		
		for (MyMethod out : fanout) {
			if (out.getID().equals(m2.getID())) {
				return true;
			}
		}

		for (MyMethod in : fanin) {
			if (in.getID().equals(m2.getID())) {
				return true;
			}
		}
		return false;
	}

	
	private void getGraph(MyClass c) {
		ArrayList<MyMethod> methods = c.getOwnedMethods();

		for (int i = 0; i < methods.size(); i++) {
			matrix[i][i]= 1;
			for (int j = i + 1; j < methods.size(); j++) {
				if (this.isConnected(methods.get(i), methods.get(j))) {
					matrix[i][j]= 1;
					matrix[j][i]= 1;
				} else {
					matrix[i][j]= 0;
					matrix[j][i]= 0;
				}
			}
		}
	}
	
	private class Component{
		private int idx;
		private ArrayList<MyMethod> methods = new ArrayList<MyMethod>();
		public int getIdx() {
			return idx;
		}
		public ArrayList<MyMethod> getMethods() {
			return methods;
		}
	}
}
