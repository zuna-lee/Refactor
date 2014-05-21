package zuna.metric;

import zuna.model.MyClass;
import zuna.model.Repo;

public abstract class Metric {
	protected Repo p;
	protected int methodPairCnt;
	protected Metric(Repo p){
		this.p = p;
	}
	
	public abstract double getMetric(MyClass c); 
	public int getMethodCnt(){
		return this.methodPairCnt;
	}
	protected String getKey(String id1, String id2){
		return id1 + "-" + id2;
	}

}
