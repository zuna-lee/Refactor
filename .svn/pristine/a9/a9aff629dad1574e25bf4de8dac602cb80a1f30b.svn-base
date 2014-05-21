package zuna.metric.classDS;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.refactoring.ProjectAnalyzer;

public class ClassCouplingBasedDS  extends DS{

	public static Hashtable<String, Double> dsTable = new Hashtable<String, Double>();
	public static double max = -1;
	
	public ClassCouplingBasedDS(){
		if(max == -1) this.getTable();
	}

	private double getDistance(MyClass c1, MyClass c2){
		double mmc = this.measureMMC(c1, c2);
		double mar = this.measureMAR(c1, c2);
		double tot = this.tot(c1, c2);
		
		double ds = (mmc+mar)/tot;
		if(Double.isInfinite(ds)|| Double.isNaN(ds)) return 0.0;
		else return ds;
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
	
	private void getTable(){
		int cnt = 0;
		ArrayList<Double> data = new ArrayList<Double>();
		double sum = 0;
		
		for(String key: ProjectAnalyzer.project.getClassList().keySet()){
			for(String key2: ProjectAnalyzer.project.getClassList().keySet()){
				if(!key.equals(key2)){
					MyClass c1 = ProjectAnalyzer.project.getClass(key);
					MyClass c2 = ProjectAnalyzer.project.getClass(key2);
					
					if(c1!=null && c2!=null// & !c1.isInterface() && !c2.isInterface()
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
	
	@Override
	public double DS(MyClass c1, MyClass c2) {
		if(c1.getID().equals(c2.getID())) return 1.0;
		else return dsTable.get(super.getKey(c1, c2));//super.normalization(getDistance(c1, c2));
	}

	private double tot(MyClass c1, MyClass c2){
		ArrayList<MyMethod> method1 = c1.getOwnedMethods();
		ArrayList<MyMethod> method2 = c2.getOwnedMethods();
		double tot = 0;
		
		for(MyMethod m: method1){
			tot+=m.getParameters().size();
		}
		
		for(MyMethod m: method2){
			tot+=m.getParameters().size();
		}
		
		tot+=c1.getOwendField().size();
		tot+=c2.getOwendField().size();
		
		
		return tot;
	}
	
	private double measureMAR(MyClass c1, MyClass c2) {
		ArrayList<MyMethod> method1 = c1.getOwnedMethods();
		ArrayList<MyMethod> method2 = c2.getOwnedMethods();
		
		Hashtable<String, MyField> overlap = new Hashtable<String, MyField>();
		double mar = 0;
		
		getReferredInBetween(c2, method1, overlap);
		getReferredInBetween(c1, method2, overlap);
		mar = overlap.size();
		
		return mar;
	}

	private void getReferredInBetween(MyClass c, ArrayList<MyMethod> methods,
			Hashtable<String, MyField> overlap) {
		for(MyMethod m: methods){
			ArrayList<MyField> referred = m.getReferencedField();
			for(MyField o1: referred){
				if(c.getID().equals(o1.getParent().getID())){
					overlap.put(o1.getID(), o1);
				}
			}
		}
	}

	private double measureMMC(MyClass c1, MyClass c2) {
		ArrayList<MyMethod> methods1 = c1.getOwnedMethods();
		ArrayList<MyMethod> methods2 = c2.getOwnedMethods();
		Hashtable<String, MyMethod> overlap = new Hashtable<String, MyMethod>();
		double mmc = 0;
		
		getCalleeInBetween(c2, methods1, overlap);
		getCalleeInBetween(c1, methods2, overlap);
		
		for(String key: overlap.keySet()){
			MyMethod ov = overlap.get(key);
			mmc+=ov.getParameters().size();
		}
		
		return mmc;
	}

	private void getCalleeInBetween(MyClass c, ArrayList<MyMethod> methods, Hashtable<String, MyMethod> overlap) {
		for(MyMethod m: methods){
			ArrayList<MyMethod> fanout = m.getFanOut();
			for(MyMethod o1: fanout){
				if(c.getID().equals(o1.getParent().getID())){
					overlap.put(o1.getID(), o1);
				}
			}
		}
	}

}
