package zuna.refactoring.experiment;

import java.util.Hashtable;
import java.util.Vector;

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
		
		if(this.projectName.equals("JHotDraw"))
			this.tracker.trackSVN(300, 789);
		else if(this.projectName.equals("JEdit"))
			this.tracker.trackSVN(20000, 23571);
		else if(this.projectName.equals("JMeter"))
			this.tracker.trackSVN(1500000, 1597614);
		
		Hashtable<String, Vector<Long>> bugFiles = this.tracker.getBugFileIndex();
		
		for(String key: bugFiles.keySet()){
			System.out.println(key  + ":" + bugFiles.get(key).get(0) + ":" + bugFiles.get(key).get(1));
		}
	}

	@Override
	public void doExperiment(Metric metric) {
		
	}
}
