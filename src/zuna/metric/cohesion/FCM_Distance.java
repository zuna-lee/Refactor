package zuna.metric.cohesion;

import java.util.ArrayList;

import zuna.metric.Metric;
import zuna.metric.classDS.ArchitectureBasedDS;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.Repo;
import zuna.util.KeyMaker;

public class FCM_Distance extends Metric{

	public FCM_Distance(Repo p) {
		super(p);
	}

	@Override
	public double getMetric(MyClass c) {
		this.initDS();
		ArrayList<MyMethod> methods = new ArrayList<MyMethod>();
		double cohesion = 0.0;
		double cnt = 0.0;
		
		for(int i = 0 ; i < methods.size()-1 ; i++){
			for(int j = i+1 ; j < methods.size() ; j++){
				cnt+=1.0;
				
				ArrayList<MyMethod> fanout1 = methods.get(i).getFanOut();
				ArrayList<MyMethod> fanout2 = methods.get(j).getFanOut();
				cohesion = getSimilarity(fanout1, fanout2);
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
				String key = KeyMaker.getKey(out1.getParent(), out2.getParent());
				similarity += ArchitectureBasedDS.dsTable.get(key);
				cnt+=1.0;
			}
		}
		if(cnt>0){
			return similarity = similarity/cnt;
		}else{
			return 0;
		}
		
	}
	
	private void initDS(){
		new ArchitectureBasedDS();
	}

}
