package zuna.metric.coupling;

import zuna.model.MyClass;
import zuna.model.MyMethod;

public class ICH{

	public double getMetric(MyClass c1, MyClass c2) {
		double ich1=0.0, ich2 = 0.0;
		
		ich1 = getICH(c1, c2);
		ich2 = getICH(c2, c1);
		
		if(ich1 <= ich2){
			return ich2;
		}else{
			return ich1;
		}
		
	}

	private double getICH(MyClass c1, MyClass c2) {
		double ich = 0.0;
		
		double allParams = this.getAllParams(c2);
		for(MyMethod m: c1.getOwnedMethods()){
			for(MyMethod out : m.getFanOut()){
				if(out.getParent().getID().equals(c2.getID())){
					ich += out.getNoOfParams()+1;
				}
			}
		}
		
		ich= ich/allParams;
		
		if(Double.isNaN(ich)|| Double.isInfinite(ich)){
			return 0.0;
		}else{
			return ich;
		}
	}
	
	public double getAllParams(MyClass c2) {
		double noOfParams = 0.0;
		
		for(MyMethod m: c2.getOwnedMethods()){
			for(MyMethod in: m.getFanIn()){
				if(!in.getParent().getID().equals(c2.getID())){
					noOfParams += m.getNoOfParams()+1;
				}
			}
		}
		
		return noOfParams;
	}

}
