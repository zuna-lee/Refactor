package zuna.metric.classDS;

import java.util.ArrayList;

import zuna.model.Element;
import zuna.model.MyClass;
import zuna.model.MyPackage;

public abstract class ICBasedDS extends DS {

	protected ArrayList<Element> getParents(Element m1, ArrayList<Element> parents){
		Element e = null;

		if(m1 instanceof MyClass){
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
	
	protected Element getSubOrdinate(ArrayList<Element> parents4M1, ArrayList<Element> parents4M2){
		Element so = null;
		for(int i = 0 ; i < parents4M1.size(); i++){
			String uri4M1 = parents4M1.get(i).getID();
			for(int k = 0 ; k < parents4M2.size(); k++){
				String uri4M2 = parents4M2.get(k).getID();
				if(uri4M1.equals(uri4M2)){
					so = parents4M2.get(k);
					return so;
				}
			}
		}
		return null;
	}
}
