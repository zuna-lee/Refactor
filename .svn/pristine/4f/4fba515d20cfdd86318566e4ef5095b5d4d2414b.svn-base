package zuna.clustering.HAC;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import zuna.clustering.Clustering;
import zuna.clustering.HAC.agglomeration.AgglomerationMethod;
import zuna.clustering.HAC.agglomeration.WeightedAverageLinkage;
import zuna.clustering.HAC.dendrogram.Dendrogram;
import zuna.clustering.HAC.dendrogram.DendrogramBuilder;
import zuna.clustering.HAC.dendrogram.DendrogramNode;
import zuna.clustering.HAC.dendrogram.MergeNode;
import zuna.clustering.HAC.dendrogram.ObservationNode;
import zuna.clustering.HAC.experiment.DissimilarityMeasure;
import zuna.clustering.HAC.experiment.DissimilarityMeasureImpl;
import zuna.clustering.HAC.experiment.Experiment;
import zuna.clustering.HAC.experiment.ExperimentImpl;
import zuna.metric.LLDMetric;
import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;
import zuna.refactoring.ProjectAnalyzer;

public class HierarchicalClustering implements Clustering{

	private double w_ar=-1;
	private double w_inh=-1;
	private double w_cou=-1;
	private Vector<Vector<String>> result = new Vector<Vector<String>>();
	private Vector<ObservationNode> cluster;

	private ArrayList<String> obv = new ArrayList<String>();
	private Hashtable<String, Double> matrix  = new Hashtable<String, Double>();
	
	public Vector<Vector<String>> getResult() {
		return result;
	}

	public ArrayList<String> getObv() {
		return obv;
	}

	public void setObv(ArrayList<String> obv) {
		this.obv = obv;
	}

	public Hashtable<String, Double> getMatrix() {
		return matrix;
	}

	public void setMatrix(Hashtable<String, Double> matrix) {
		this.matrix = matrix;
	}

	public void setWeight(double w_ar, double w_inh, double w_cou){
		this.w_ar = w_ar;
		this.w_inh = w_inh;
		this.w_cou = w_cou;
	}
	
	public void clustering(MyClass c, LLDMetric metric, double th) {
		// start
		Dendrogram dendrogram = null;
		
		try{
			Experiment experiment = new ExperimentImpl(obv);
			DissimilarityMeasure dissimilarityMeasure = new DissimilarityMeasureImpl(matrix);
			
			AgglomerationMethod agglomerationMethod = new WeightedAverageLinkage();
			DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations(), obv);

			HierarchicalAgglomerativeClusterer clusterer = 
					new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod);
			clusterer.cluster(dendrogramBuilder);
			
			dendrogram = dendrogramBuilder.getDendrogram();
			Vector<Vector<ObservationNode>> setOfClusters = new Vector<Vector<ObservationNode>>();
			this.findMergeNodeSimilarThan(setOfClusters, th, dendrogram.getRoot());
			this.makeClusteringResult(setOfClusters);
			this.moveAttribute(c.getOwendField());
			
		}catch(NullPointerException e){
			
		}
	}
	
	private void makeClusteringResult(Vector<Vector<ObservationNode>> setOfClusters){
		
		this.result.clear();
		for(Vector<ObservationNode> cluster: setOfClusters){
			Vector<String> clusters = new Vector<String>();
			for(ObservationNode node: cluster){
				clusters.add(node.getId());
			}
				this.result.add(clusters);
		}

	}
	
	private void moveAttribute(ArrayList<MyField> fields){
		for(MyField f: fields){
			int idx = getMostReferredClusterIndex(result, f.getID());
			if(idx>-1){
				this.result.get(idx).add(f.getID());
			}else if(idx==-1){
//				this.result.get(this.getMaxSizeClusterIdx(this.result)).add(f.getID());
			}
		}
	}
	
	public int getMaxSizeClusterIdx(Vector<Vector<String>> result){
		int size = 0;
		int idx = -1;
		for(int i = 0 ; i < result.size() ; i++){
			if(result.get(i).size()>size){
				size = result.get(i).size();
				idx = i;
			}
		}
		
		return idx;
	}
	
	public int getMostReferredClusterIndex(Vector<Vector<String>> result, String fieldID){
		int maxReferredIdx = -1;
		int maxReferredCnt = 0;
		
		for(int i = 0 ; i < result.size() ; i++){
			Vector<String> cluster = result.get(i);
			int cnt = 0;
			for(String key: cluster){
				MyMethod m = ProjectAnalyzer.project.getMethodList().get(key);
				if(m!=null){
					if(this.isRefer(fieldID, m)){
						cnt++;
					}
				}
			}
			if(cnt>maxReferredCnt) {
				maxReferredCnt = cnt;
				maxReferredIdx = i;
			}
		}
		
		return maxReferredIdx;
	}
	
	private boolean isRefer(String fieldID, MyMethod m){
		ArrayList<MyField> referredFields = m.getReferencedField();
		for(MyField f: referredFields){
			if(f.getID().equals(fieldID)){
				return true;
			}
		}
		
		return false;
	}
	private void findMergeNodeSimilarThan(
			Vector<Vector<ObservationNode>> setOfClusters, double similarity,
			DendrogramNode node) {
		
		if (node==null) {
				;
		} else if (node instanceof ObservationNode) {
			cluster = new Vector<ObservationNode>();
			cluster.add((ObservationNode)node);
			setOfClusters.add(cluster);
		} else if (node instanceof MergeNode) {
			if(similarity > ((MergeNode) node).getDissimilarity())
			{
				cluster = new Vector<ObservationNode>();
				makeCluster(node);
				setOfClusters.add(cluster);
			}else
			{
				findMergeNodeSimilarThan(setOfClusters,similarity,node.getLeft());
				findMergeNodeSimilarThan(setOfClusters,similarity,node.getRight());
			}
		}
	}
	
	private void print(DendrogramNode root, String space){

		if(root.getLeft() instanceof ObservationNode)
			System.out.println(space+((ObservationNode)root.getLeft()).getId());
		else{
			print(root.getLeft(), space + "    ");
		}
		
		if(root.getRight() instanceof ObservationNode)
			System.out.println(space+((ObservationNode)root.getRight()).getId());
		else{
			print(root.getRight(), space + "    ");
		}
		
	}
	private void makeCluster(DendrogramNode node) {
        if (node instanceof ObservationNode) {
        	cluster.add((ObservationNode)node);
        } else if (node instanceof MergeNode) {
        	makeCluster(((MergeNode)node).getLeft());
        	makeCluster(((MergeNode)node).getRight());
        }
	}
}
