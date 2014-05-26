package zuna.refactoring.operator;

import zuna.metric.Metric;

public interface Experimentor {

	public void prepareExperiment();
	public void doExperiment(Metric metric);
	public void doExperiment(Metric metric, Metric metric2);
	public void doExperiment(Metric metric, Metric metric2, Metric metric3);
}
