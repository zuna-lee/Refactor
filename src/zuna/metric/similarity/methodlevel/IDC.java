package zuna.metric.similarity.methodlevel;


import java.util.ArrayList;

import zuna.model.MyMethod;



public class IDC {

	public double getIDC(MyMethod m1, MyMethod m2, boolean internal){
		int cdm1=0, cdm2=0; 
		double cdm=0;
		
		ArrayList<MyMethod> fanin1 = new ArrayList<MyMethod>();
		ArrayList<MyMethod> fanin2 = new ArrayList<MyMethod>();
		
		if(internal){
			fanin1 = this.getTotFanin(m1);
			fanin2 = this.getTotFanin(m2);
		}else{
			fanin1 = m1.getFanIn();
			fanin2 = m2.getFanIn();
		}
		
		for(MyMethod fanins: fanin1){
		
			if(fanins.getID().equals(m2.getID())){
				++cdm1;
			}
		
		}
		
		for(MyMethod fanins: fanin2){
			
			if(fanins.getID().equals(m1.getID())){
				++cdm2;
			}
			
		}
		double cdm_1 = ((double) cdm1 / (double) fanin1.size());
		double cdm_2 = ((double) cdm2 / (double) fanin2.size());
		
		if(Double.isNaN(cdm_1) || Double.isInfinite(cdm_1)) cdm_1 = 0.0;
		if(Double.isNaN(cdm_2) || Double.isInfinite(cdm_2)) cdm_2 = 0.0;
		
		if(cdm_1 > cdm_2){
			cdm = cdm_1;
		}else{
			cdm = cdm_2;
		}
		
		return cdm;
	}
	
	public ArrayList<MyMethod> getTotFanin(MyMethod m){
		ArrayList<MyMethod> faninWithinClass = new ArrayList<MyMethod>();
		
		for(MyMethod fanin: m.getFanIn()){
			if(fanin.getParent().getID().equals(m.getParent().getID())){
				faninWithinClass.add(fanin);
			}
		}
	
		return faninWithinClass;
	}
	
	protected String[] getEID(String iid){
		return iid.split("-");
	}
}
