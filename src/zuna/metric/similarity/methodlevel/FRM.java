package zuna.metric.similarity.methodlevel;

import java.util.ArrayList;

import zuna.model.Element;
import zuna.model.MyClass;
import zuna.model.MyMethod;
import zuna.model.MyPackage;

public class FRM implements MethodLevelMetrics{

	@Override
	public double measure(MyMethod m1, MyMethod m2) {
		ArrayList<MyMethod> out1 = m1.getFanOut();
		ArrayList<MyMethod> in1 = m1.getFanIn();
		
		ArrayList<MyMethod> out2 = m2.getFanOut();
		ArrayList<MyMethod> in2 = m2.getFanIn();
		
		double outEntropy = getMaximumEntory(out1, out2);
		double inEntropy = getMaximumEntory(in1, in2);
		
		if(outEntropy> inEntropy) return outEntropy;
		else return inEntropy;
	}

	private double getMaximumEntory(ArrayList<MyMethod> ext1, ArrayList<MyMethod> ext2){
		double entropy = 0;
		for(MyMethod e1: ext1){
			for(MyMethod e2: ext2){
				double tmpEntropy = this.getEntropy(e1, e2);
				if(entropy<tmpEntropy){
					entropy = tmpEntropy; 
				}
			}
		}
		return entropy;
	}
	
	private double getEntropy(MyMethod e1, MyMethod e2){
		ArrayList<Element> parents4M1 = new ArrayList<Element>();
		ArrayList<Element> parents4M2 = new ArrayList<Element>();
		
		parents4M1 = this.getParents(e1, parents4M1);
		parents4M2 = this.getParents(e2, parents4M2);
		Element so = this.getSubOrdinate(parents4M1, parents4M2);
		return so.getIc();
	}
	
	

	private Element getSubOrdinate(ArrayList<Element> parents4M1, ArrayList<Element> parents4M2){
		Element so = null;
		for(int i = 0 ; i < parents4M1.size(); i++){
			String uri4M1 = parents4M1.get(i).getID();
			for(int k = 0 ; k < parents4M2.size(); k++){
				String uri4M2 = parents4M2.get(k).getID();
				if(uri4M1.equals(uri4M2)){
					so = parents4M2.get(k);
					break;
				}
			}
			
			if(so!=null) break;
		}
		
		return so;
	}
	
	private ArrayList<Element> getParents(Element m1, ArrayList<Element> parents){
		Element e = null;
		
		if(m1 instanceof MyMethod){
			e = ((MyMethod) m1).getParent();
		}else if(m1 instanceof MyClass){
			e = ((MyClass) m1).getParent();
		}else if(m1 instanceof MyPackage){
			e = ((MyPackage) m1).getParent();
		}
		
		if(e!=null){
			parents.add(e);
			this.getParents(e, parents);
		}
		
		return parents;
	}
}
