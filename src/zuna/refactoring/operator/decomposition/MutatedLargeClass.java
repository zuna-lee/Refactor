package zuna.refactoring.operator.decomposition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import org.eclipse.core.resources.IProject;

import zuna.clustering.Clustering;
import zuna.metric.LLDMetric;
import zuna.metric.cohesion.FCM_Distance;
import zuna.metric.cohesion.LSCC;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.MyPackage;
import zuna.refactoring.ProjectAnalyzer;
import zuna.util.Logger2File;

public class MutatedLargeClass{
	private ClassMutation mutation;
	private Vector<Vector<MyClass>> candidateClassPairs = new Vector<Vector<MyClass>>();
	private Vector<String> cluster1 = new Vector<String>();
	private Vector<String> cluster2 = new Vector<String>();
	private Clustering clusteringAlg;
	private LLDMetric metric;
	private LLDMetric metric2;
	private LLDMetric metric3;
	private double max = 0;
	private double min = 100000;
	private double maxTh = -1;
	
	public MutatedLargeClass(IProject iproject, Clustering clusteringAlg) {
		this.clusteringAlg = clusteringAlg;
	}

	public void analyze() {
//		this.identify();
		ArrayList<String> out = new ArrayList<String>();
		this.identify();
		int cnt = 0;
		for(Vector<MyClass> pair: this.candidateClassPairs){
			MyClass c1 = pair.get(0);
			MyClass c2 = pair.get(1);
			System.out.println(cnt++);
			mutation = new ClassMutation(c1, c2);
			MyClass lc = mutation.merge();
			this.metric = new LSCC(ProjectAnalyzer.project, lc);
			this.metric2 = new FCM_Distance(ProjectAnalyzer.project);
//			this.metric = new C3(ProjectAnalyzer.project, lc);
			
			
			if(lc.getOwnedMethods().size()<=4) continue;
			
//			this.metric = new C3(ProjectAnalyzer.project, lc);
//			this.metric2 = new LSCC(ProjectAnalyzer.project, lc);
			this.maxTh = -1;
			
//			double result = this.decomposing(lc);
			double[] results = decomposes(lc);
			String result = c1.getID() + ":" + c2.getID()
					+ ":" + c1.getOwendField().size() + ":" + c1.getOwnedMethods().size() 
					+ ":" + c2.getOwendField().size() + ":" + c2.getOwnedMethods().size();
			
			for(int i = 0 ; i < results.length ; i++){
				result+= ":"+results[i];
			}
			
			out.add(result);
			Logger2File.print2CSVFile(out, ProjectAnalyzer.prjName + "-mutation");
		}
	}
	
	private double characterize(MyClass c){
		double count = 0; 
		for(MyMethod m: c.getOwnedMethods()){
			if(m.getReferencedField().size()==0)
				count+=1.0;
		}
		if(c.getOwnedMethods().size()>0){
			return count/(double)c.getOwnedMethods().size();
		}else{
			return 0;
		}
		
	}
	
	private double evaluate() {
		return this.mutation.getFmeasure(cluster1, cluster2);
	}


	private double[] decomposes(MyClass lc) {
		double[] fmeasures = new double[101];
		ArrayList<String> obv = this.getObv(lc);
		Hashtable<String, Double> matrix = this.getMatrix(obv);
		this.clusteringAlg.setObv(obv);
		this.clusteringAlg.setMatrix(matrix);

		max = 0;
		min = 10000;
		this.getTh(matrix);
		double scale = (max-min)/100;
		int idx = 0;
		for(double e=max;e>min;e-=scale){
			double maxResult = 0.0;
			this.clusteringAlg.clustering(lc, metric, e);
			Vector<Vector<String>> result = this.clusteringAlg.getResult();
			if(result==null || result.size()<2){
				continue;
			}
			
			for(int i = 0; i < result.size()-1;i++){
				for(int j = i+1; j < result.size();j++){
					this.cluster1 = result.get(i);
					this.cluster2 = result.get(j);
					double fmeasure = this.evaluate();
					if(fmeasure> maxResult) {
						maxResult = fmeasure;
					}
				}
			}
			

			fmeasures[idx++] = maxResult;
			
			
		}
		return fmeasures;
	}

	public ArrayList<String> getObv(MyClass c){
		
		ArrayList<String> obv = new ArrayList<String>();
		
		for(MyMethod m1: c.getOwnedMethods()){
//			if(!m1.isOverride() && !m1.isConstructor() && !m1.isSupport()){
				obv.add(m1.getID());
//			}
		}
		
		return obv;
	}
	
	public Hashtable<String, Double> getMatrix(ArrayList<String> obv){
		Hashtable<String, Double> matrix = new Hashtable<String, Double>(); 	
		for(int i = 0 ; i < obv.size()-1 ; i++){
			for(int j = i+1 ; j < obv.size() ; j++){
				MyMethod m1 = ProjectAnalyzer.project.getMethod(obv.get(i));
				MyMethod m2 = ProjectAnalyzer.project.getMethod(obv.get(j));
				
				double similarity = 0;
				double similarity1 = metric.getMetric(m1, m2);
				double similarity2 = metric2.getMetric(m1, m2);
				
				if(similarity1>similarity2) similarity = similarity1;
				else similarity = similarity2;
				
//				if(similarity1>0) similarity = similarity1;
//				else similarity = similarity2;
				
//				if((similarity1 + similarity2)>0)
//					similarity = (0.9* similarity1) + (0.1*similarity2);
				
				double distance = 1-similarity;
				
				matrix.put(obv.get(i) + ":" + obv.get(j), distance);
				matrix.put(obv.get(j) + ":" + obv.get(i), distance);
			}
		}
		return matrix;
	}
	
	
	private void getTh(Hashtable<String, Double> matrix){
		for(String key: matrix.keySet()){
			double entry = matrix.get(key);
			
			if(entry>max) max = entry;
			if(entry<min) min = entry;
		}
	}
	
	private boolean isCandidates(MyClass c){
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		if(c.isAbstract() || methods.size()<=5) return false;
		if(!c.getOutterClassUri().equals("java.lang.Object")) return false;
//		for(MyMethod m: methods){
//			if(m.getFanOut().size()==0) return false;
//		}
		
		
		return true;
	}
	
	
	private void identify(){
		HashMap<String, MyPackage> packages = ProjectAnalyzer.project.getPackageList();
		
		for(String key: packages.keySet()){
			MyPackage p = packages.get(key);
			if(!p.isLibrary() && p.getClassChildren().size()>1){
				this.getClassPair(p);
			}
		}
	}
	
	private void getClassPair(MyPackage p){

		Hashtable<String, String> checked = new Hashtable<String, String>();
		Vector<MyClass> candidates = new Vector<MyClass>();
		HashMap<String, MyClass> child = p.getClassChildren();
		
		for(String key: child.keySet()){
			MyClass child1 = child.get(key);
			if(this.isCandidates(child1)){
				candidates.add(child1);
			}
		}
		
		for(MyClass c1: candidates){
			for(MyClass c2: candidates){
				if(!c1.getID().equals(c2.getID())){
					String key = this.getID(c1, c2);
					String inverseKey = this.getID(c2, c1);
					if(!checked.containsKey(key)){
//							 && (c1.getID().equals("org.jhotdraw.samples.svg.gui.AbstractToolBar") || c2.getID().equals("org.jhotdraw.samples.svg.gui.AbstractToolBar"))){
						checked.put(key, key);
						checked.put(inverseKey, inverseKey);
						Vector<MyClass> cp = new Vector<MyClass>();
						cp.add(c1);
						cp.add(c2);
						candidateClassPairs.add(cp);
					}
				}
			}
		}
	}
	
	private String getID(MyClass c1, MyClass c2){
		return c1.getID() + ":" + c2.getID();
	}
	
	
}