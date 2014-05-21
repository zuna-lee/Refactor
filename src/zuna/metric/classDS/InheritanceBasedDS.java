package zuna.metric.classDS;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyClass;
import zuna.refactoring.ProjectAnalyzer;
import zuna.util.Logger2File;

public class InheritanceBasedDS  extends ICBasedDS{
	
	public static Hashtable<String, Double> dsTable = new Hashtable<String, Double>();
	
	public static double max = -1;
	
	public static String k1;
	public static String k2;
	
	public InheritanceBasedDS(){
		if(max==-1) {
			this.getTable();
		}
	}
	
	@Override
	public double DS(MyClass c1, MyClass c2) {
		if(c1.getID().equals(c2.getID())) return 1.0;
		else return 1-dsTable.get(super.getKey(c1, c2));//super.normalization(getDistance(c1, c2));
	}

	private double getDistance(MyClass c1, MyClass c2) {
		if(!c1.getOutterClassUri().equals("java.lang.Object")) c1 = super.getOutterClass(c1);
		if(!c2.getOutterClassUri().equals("java.lang.Object")) c2 = super.getOutterClass(c2);
		
		ArrayList<MyClass> parentsOfClass1 = new ArrayList<MyClass>();
		ArrayList<MyClass> parentsOfClass2 = new ArrayList<MyClass>();
		
		this.getParents(c1, parentsOfClass1);
		this.getParents(c2, parentsOfClass2);
		MyClass so = this.getSubOrdinateClass(parentsOfClass1, parentsOfClass2);
		
		double distInInheritance = this.getDistanceOfEdge(c1, parentsOfClass1, so);
		distInInheritance += this.getDistanceOfEdge(c2, parentsOfClass2, so);
		
		ArrayList<MyClass> interfaceOfClass1 = c1.getInterface();
		ArrayList<MyClass> interfaceOfClass2 = c2.getInterface();

		MyClass so2 = this.getSubOrdinateInterface(interfaceOfClass1, interfaceOfClass2);
		double distInInterface = 1000;
		
		if(so2!=null){
			distInInterface = this.getDistanceOfEdge(c1, interfaceOfClass1, so2);
			distInInterface += this.getDistanceOfEdge(c2, interfaceOfClass2, so2);
		}

		if(distInInheritance < distInInterface) return distInInheritance;
		else return distInInterface;
	}
	
	private MyClass getSubOrdinateInterface(ArrayList<MyClass> interfaceOfClass1,
			ArrayList<MyClass> interfaceOfClass2) {
		MyClass so = null;
		double max = 0.0;
		for(MyClass i1: interfaceOfClass1){
			for(MyClass i2: interfaceOfClass2){
				if(i1.getID().equals(i2.getID())){
					if(max< i1.getIcIninheritance()){
						max = i1.getIcIninheritance();
						so = i1;
					}
				}
			}
		}
		
		return so;
	}

	protected MyClass getSubOrdinateClass(ArrayList<MyClass> parents4M1, ArrayList<MyClass> parents4M2){
		MyClass so = null;
		for(int i = 0 ; i < parents4M1.size(); i++){
			String uri4M1 = parents4M1.get(i).getID();
			for(int k = 0 ; k < parents4M2.size(); k++){
				String uri4M2 = parents4M2.get(k).getID();
				if(uri4M1.equals(uri4M2)){
					so = parents4M2.get(k);
					return so;
				}
			}
		}
		return null;
	}
	
	protected ArrayList<MyClass> getParents(MyClass c, ArrayList<MyClass> parents){
		MyClass p = null;
		p = c.getSuperClass();
		
		if(p!=null){
			parents.add(p);
			this.getParents(p, parents);
		}
		
		return parents;
	}
	
	
	private void getTable(){
		int cnt = 0;
		ArrayList<Double> data = new ArrayList<Double>();
		double sum = 0;
		
		for(String key: ProjectAnalyzer.project.getClassList().keySet()){
			for(String key2: ProjectAnalyzer.project.getClassList().keySet()){
				if(!key.equals(key2)){
					MyClass c1 = ProjectAnalyzer.project.getClass(key);
					MyClass c2 = ProjectAnalyzer.project.getClass(key2);
					
					if(c1!=null && c2!=null// & !c1.isInterface() && !c2.isInterface() && !c1.getID().equals("ROOT") && !c2.getID().equals("ROOT")
							&& !dsTable.containsKey(super.getKey(c1, c2))){
						double ds = this.getDistance(c1, c2);
						dsTable.put(super.getKey(c1, c2), ds);
						dsTable.put(super.getKey(c2, c1), ds);
						data.add(ds);
						sum+=ds;
						cnt++;
						
					}
				}
			}
			
		}
		
		double avg = sum/(double) cnt;
		double sdv = this.getStdev(data, avg);
		max = 1;
		dsTable = super.normalization(dsTable, avg, sdv);
	}
	
	private int getSize(){
		int cnt=0;
		for(String key: ProjectAnalyzer.project.getClassList().keySet()){
			for(String key2: ProjectAnalyzer.project.getClassList().keySet()){
				if(!key.equals(key2)){
					MyClass c1 = ProjectAnalyzer.project.getClass(key);
					MyClass c2 = ProjectAnalyzer.project.getClass(key2);
					
					if(c1!=null && c2!=null & !c1.isInterface() && !c2.isInterface()){
						cnt++;
					}
				}
			}
		}
		
		return cnt;
	}
	
	private double getDistanceOfEdge(MyClass c, ArrayList<MyClass> parentsOfClass, MyClass so){
		MyClass child = c;
		double distance = 0.0;
		
		try{
			for(MyClass parent: parentsOfClass){
				distance += child.getIcIninheritance() - parent.getIcIninheritance();
				child = parent;
				if(parent.getID().equals(so.getID())){
					break;
				}
			}
		}catch(java.lang.NullPointerException e){
//			System.out.println("-----");
		}
		
		
		return distance;
	}
	
}
