package zuna.clustering.HAC.experiment;

import java.util.ArrayList;
import java.util.Hashtable;

public class DissimilarityMeasureImpl implements DissimilarityMeasure{

	private Hashtable<String, Double> matrix = new Hashtable<String, Double>();
	public DissimilarityMeasureImpl(Hashtable<String, Double> matrix){
		this.matrix= matrix;
	}
	public double computeDissimilarity(Experiment experiment, int observation1, int observation2) {
		ExperimentImpl impl;
		
		if(experiment instanceof ExperimentImpl){
			impl = (ExperimentImpl) experiment;
			ArrayList<String> obv = impl.getObv();
			String c1 = obv.get(observation1);
			String c2 = obv.get(observation2);
			return matrix.get(this.getKey(c1, c2));
		}else{
			return 0;
		}
	}
	
	private String getKey(String c1, String c2){
		return c1 + ":" + c2;
	}
	
}
