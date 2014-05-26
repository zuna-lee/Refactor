package zuna.refactoring.experiment;

import zuna.metric.Metric;

public interface Experiment {

	public void prepareExperiment();
	public void doExperiment(Metric metric);
}
