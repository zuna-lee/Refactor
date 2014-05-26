package zuna.metric.similarity.methodlevel;

import java.util.ArrayList;

import zuna.metric.similarity.classlevel.Classintimacy;
import zuna.model.MyMethod;


public class EIC extends IDC{
	
	public double getMetric(MyMethod m1, MyMethod m2){
		double eic = 0.0, eic1 = 0.0, eic2 = 0.0;
		MyMethod intermediate1 = this.getIntermediateMethods(m1, m2);
		MyMethod intermediate2 = this.getIntermediateMethods(m1, m2);
		Classintimacy intimacy = new Classintimacy();
		
		if(intermediate1!=null){
			double intm1 = (1 + intimacy.getDistanceOfClasses(m1.getParent(), intermediate1.getParent()))/2;
			eic1 = super.getIDC(m1, intermediate1, false) * super.getIDC(intermediate1, m2, false) * intm1;
		}
		
		if(intermediate2!=null){
			double intm2 = (1 + intimacy.getDistanceOfClasses(m1.getParent(), intermediate2.getParent()))/2;
			eic2 = super.getIDC(m2, intermediate2, false) * super.getIDC(intermediate2, m1, false) * intm2;
		}
		
		
		if(eic1>eic2) eic = eic1;
		else eic = eic2;
		if(Double.isInfinite(eic)) eic = 0.0;
		if(Double.isNaN(eic)) eic = 0.0;
		
		return eic;
	}
	
	private MyMethod getIntermediateMethods(MyMethod m1, MyMethod m2){
		MyMethod intermediate = null; 
		
		ArrayList<MyMethod> outList1 = m1.getFanOut();
		
		for(MyMethod out1: outList1){
			if(!out1.getParent().getID().equals(m1.getParent().getID())){
				ArrayList<MyMethod> outOfOutList1 = out1.getFanOut();
				
				for(MyMethod outOfOut: outOfOutList1){
					if(outOfOut.getID().equals(m2.getID())){
						intermediate= out1;
					}
				}
				
			}
		}
		
		return intermediate;
	}
}
