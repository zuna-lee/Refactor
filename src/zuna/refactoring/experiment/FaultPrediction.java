package zuna.refactoring.experiment;

import java.util.Hashtable;

import zuna.metric.Metric;
import zuna.model.Repo;
import zuna.refactoring.experiment.svn.SVNTracker;


public class FaultPrediction implements Experiment {
	private Repo project;
	private SVNTracker tracker;
	private String projectName;
	
	public FaultPrediction(Repo project2){
		this.project = project2;
		this.projectName = project2.getName();
	}
	
	public FaultPrediction(String projectName){
		this.projectName = projectName;
	}
	
	@Override
	public void prepareExperiment() {
		this.tracker = new SVNTracker(projectName);
		this.tracker.init();
		this.tracker.connectToSVN();
		this.tracker.trackSVN(30, SVNTracker.HEAD);
		Hashtable<String, Integer> bugFiles = this.tracker.getBugFileIndex();
		
		for(String key: bugFiles.keySet()){
			System.out.println(key  + ":" + bugFiles.get(key));
		}
	}

	@Override
	public void doExperiment(Metric metric) {
		
	}
}
