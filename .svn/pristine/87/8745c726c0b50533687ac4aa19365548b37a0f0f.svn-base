package zuna.clustering;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import zuna.metric.LLDMetric;
import zuna.model.MyClass;

public interface Clustering {

	public void clustering(MyClass c, LLDMetric metric, double th);
	public Vector<Vector<String>> getResult();
	public void setWeight(double w_ar, double w_inh, double w_cou);
	public void setMatrix(Hashtable<String, Double> matrix);
	public void setObv(ArrayList<String> obv);
}
