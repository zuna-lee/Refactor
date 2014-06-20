package zuna.metric;

import zuna.model.MyClass;
import zuna.model.MyMethod;

public interface LLDMetric {

	public double getMetric(MyClass c);
	public double getMetric(MyMethod m1, MyMethod m2);
}
