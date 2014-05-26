package zuna.metric.cohesion;

import java.util.ArrayList;

import zuna.metric.Metric;
import zuna.metric.similarity.methodlevel.IDC;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.Repo;

public class IDCC extends Metric{

	public IDCC(Repo p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double getMetric(MyClass c) {
		IDC idc = new IDC();
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		double v = 0;
		double cnt = 0;
		for(int i = 0 ; i < methods.size()-1; i++){
			for(int j = i+1 ; j < methods.size(); j++){
				MyMethod m1 = methods.get(i);
				MyMethod m2 = methods.get(j);
				v+=idc.getIDC(m1, m2, true);
				cnt+=1.0;
			}
		}
		
		return v/cnt;
	}

}
