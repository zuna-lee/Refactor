package zuna.metric.classDS;

import java.util.ArrayList;
import java.util.Hashtable;

import zuna.model.MyClass;
import zuna.refactoring.ProjectAnalyzer;

public abstract class DS {

	private double max;
	private double min;
	
	public abstract double DS(MyClass c1, MyClass c2);
	
	protected MyClass getOutterClass(MyClass inner){
		
			MyClass outter = ProjectAnalyzer.project.getClassList().get(inner.getOutterClassUri());
			if(!outter.getOutterClassUri().equals("java.lang.Object")){
				outter = ProjectAnalyzer.project.getClassList().get(outter.getOutterClassUri());
			}
		
		return outter;
	}
	

	private void getMaxAndMin(Hashtable<String, Double> table){
		max = 0;
		min =1000000;
		for(String key : table.keySet()){
			double data = table.get(key);
			if(data>max) max = data;
			if(data<min) min = data;
		}
		
	}
	protected Hashtable<String, Double> normalization(Hashtable<String, Double> dsTable, double avg, double sdv){

		for(String key : dsTable.keySet()){
			dsTable.get(key);
			dsTable.put(key, (dsTable.get(key)-avg)/sdv);
		}
		this.getMaxAndMin(dsTable);
		
		for(String key : dsTable.keySet()){
			double data = (dsTable.get(key)+Math.abs(min)) / (max+Math.abs(min));
			dsTable.put(key, data);
		}
		return dsTable;
	}
	
	protected String getKey(MyClass c1, MyClass c2){
		return c1.getID() + ":" + c2.getID();
	}
	
	protected double getStdev(ArrayList<Double> data, double avg) {
		double ss = 0.0;
		
		for(double d: data){
			ss += Math.pow(d-avg, 2);
		}
		
		if(data.size()>0) return Math.sqrt(ss/(double)data.size());
		else return 0.0;
	}
	
}
