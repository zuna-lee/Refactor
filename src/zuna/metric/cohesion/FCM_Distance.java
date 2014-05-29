package zuna.metric.cohesion;

import java.util.ArrayList;

import zuna.metric.Metric;
import zuna.metric.classDS.ArchitectureBasedDS;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.Repo;
import zuna.refactoring.ProjectAnalyzer;
import zuna.util.KeyMaker;

public class FCM_Distance extends Metric{

	public FCM_Distance(Repo p) {
		super(p);
	}

	@Override
	public double getMetric(MyClass c) {
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		double cohesion = 0.0;
		double cnt = 0.0;
		
		for(int i = 0 ; i < methods.size()-1 ; i++){
			for(int j = i+1 ; j < methods.size() ; j++){
				cnt+=1.0;
				
				ArrayList<MyMethod> fanout1 = methods.get(i).getFanOut();
				ArrayList<MyMethod> fanout2 = methods.get(j).getFanOut();
				if(fanout1.size()==0 || fanout2.size()==0){
					cohesion += 0;
				}else{
					double similarity = getSimilarity(fanout1, fanout2);
					cohesion += similarity;
				}
			}
		}
		
		if(cnt>0){
			return cohesion = cohesion/cnt;
		}else{
			return 0;
		}
	}
	
	private double getSimilarity(ArrayList<MyMethod> fanout1, ArrayList<MyMethod> fanout2){
		double similarity = 0.0;
		double cnt = 0;
		for(MyMethod out1: fanout1){
			for(MyMethod out2: fanout2){
				String key = "";
				try{
					if(!out1.isLibrary() && !out2.isLibrary()){
						if(out1.getParent().getID().equals(out2.getParent().getID())){
							similarity+=1;
						}else{
							key = KeyMaker.getKey(out1.getParent(), out2.getParent());
							double ds = ArchitectureBasedDS.dsTable.get(key);
							double e = this.weightingFactor(out1.getParent(), out2.getParent());
							similarity += ds * e;
						}
						
						cnt+=1.0;
					}
					
				}catch(java.lang.NullPointerException e){
//					System.out.println(key);
				}
			}
		}
		if(cnt>0){
			return similarity = similarity/cnt;
		}else{
			return 0;
		}
	}
	
	
	private double weightingFactor(MyClass c1, MyClass c2){
		Double e = 0.0, e1 = 0.0;
		
		
		
		e = (double)1/(double)ArchitectureBasedDS.dfTable.get(c1.getID());
		e1 = (double)1/(double)ArchitectureBasedDS.dfTable.get(c2.getID());
		
		if(e.isNaN() || e.isInfinite()){
			e = 0d;
		}
		
		if(e1.isNaN() || e1.isInfinite()){
			e1 = 0d;
		}
		
		if(e>e1){
			return e1;
		}else{
			return e;
		}
	}

}
