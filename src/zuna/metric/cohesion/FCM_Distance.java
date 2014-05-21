package zuna.metric.cohesion;

import zuna.metric.Metric;
import zuna.model.MyClass;
import zuna.model.Repo;

public class FCM_Distance extends Metric{

	public FCM_Distance(Repo p) {
		super(p);
	}

	@Override
	public double getMetric(MyClass c) {
		return 0;
	}

}
