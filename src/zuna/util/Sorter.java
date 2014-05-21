package zuna.util;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

public class Sorter {

	public static ArrayList<String> sort(Hashtable<String, Vector<Double>> metrics, int idx){
		ArrayList<String> sortedMetrics = new ArrayList<String>();
		while(sortedMetrics.size() < metrics.size()){
			double max = 0;
			String maxKey = "";
			for(String key: metrics.keySet()){
				double s = metrics.get(key).get(idx);
				if(max<s && !sortedMetrics.contains(key)){
					maxKey = key;
					max = s;
				}
			}
			sortedMetrics.add(maxKey);
		}
		return sortedMetrics;
	}
}
