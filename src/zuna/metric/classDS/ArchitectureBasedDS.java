package zuna.metric.classDS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import zuna.model.Element;
import zuna.model.MyClass;
import zuna.refactoring.ProjectAnalyzer;
import zuna.util.KeyMaker;

public class ArchitectureBasedDS extends ICBasedDS{

	public static Hashtable<String, Double> dsTable = new Hashtable<String, Double>();
	public static Hashtable<String, Double> dfTable = new Hashtable<String, Double>();
	
	public ArchitectureBasedDS(){
		dsTable.clear();
		this.getDSTable();
		this.getDFTable();
		
		System.out.println("Architecture Based DS Table..." + ":" + dsTable.size());
	}
	
	private double getDistance(MyClass c1, MyClass c2){
		ArrayList<Element> parents4M1 = new ArrayList<Element>();
		ArrayList<Element> parents4M2 = new ArrayList<Element>();
		
		super.getParents(c1, parents4M1);
		super.getParents(c2, parents4M2);
		
		Element so = super.getSubOrdinate(parents4M1, parents4M2);
		
		if(so==null || so.getID().equals("ROOT")) return 0.0;
		else return so.getIc();
	}
	
	@Override
	public double DS(MyClass c1, MyClass c2) {
		if(c1.getID().equals(c2.getID())) return 1.0;
		else return dsTable.get(KeyMaker.getKey(c1, c2));//super.normalization(getDistance(c1, c2));
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
	
	private void getDFTable(){
		HashMap<String, MyClass> classes = ProjectAnalyzer.project.getClassList();
		
		for(String key: classes.keySet()){
			MyClass c = classes.get(key);
			dfTable.put(c.getID(), 0d);
			for(String key2: classes.keySet()){
				if(!key.equals(key2)){
					MyClass ext = classes.get(key);
					HashSet<MyClass> useClasses = ext.getUseClasses();
					for(MyClass use: useClasses){
						if(use.getID().equals(c.getID())){
							double count = dfTable.get(c.getID());
							dfTable.put(c.getID(), ++count);
							break;
						}
					}
				}
			}
			
		}
	}
	
	private void getDSTable(){
		int cnt = 0;
		ArrayList<Double> data = new ArrayList<Double>();
		double sum = 0;
		
		for(String key: ProjectAnalyzer.project.getClassList().keySet()){
			for(String key2: ProjectAnalyzer.project.getClassList().keySet()){
				if(!key.equals(key2)){
					MyClass c1 = ProjectAnalyzer.project.getClass(key);
					MyClass c2 = ProjectAnalyzer.project.getClass(key2);
					
					if(c1!=null && c2!=null// & !c1.isInterface() && !c2.isInterface()
							&& !dsTable.containsKey(KeyMaker.getKey(c1, c2))
							&& !c1.isLibrary() && !c2.isLibrary()){
						double ds = this.getDistance(c1, c2);
						
						ArchitectureBasedDS.dsTable.put(KeyMaker.getKey(c1, c2), ds);
						ArchitectureBasedDS.dsTable.put(KeyMaker.getKey(c2, c1), ds);
						data.add(ds);
						sum+=ds;
						cnt++;
					}
				}
			}
			
		}
		
		double avg = sum/(double) cnt;
		double sdv = this.getStdev(data, avg);
		dsTable = super.normalization(dsTable, avg, sdv);
	}

	
}
