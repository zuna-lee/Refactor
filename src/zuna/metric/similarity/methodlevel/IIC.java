package zuna.metric.similarity.methodlevel;

import java.util.ArrayList;

import zuna.model.MyMethod;


public class IIC extends IDC{

	private static boolean isConn = false;
	private static ArrayList<MyMethod> intermediate = new ArrayList<MyMethod>();
	
	public double getMetric(MyMethod m1, MyMethod m2){
		double iic = 1.0;
		isConn = false;
		intermediate.clear();
		
		if(m1.getID().equals(m2.getID())){
			return 0.0;
		}
		
		boolean indirect = isConnected(m1, m2);
		boolean direct = this.isDirectlyConnected(m1, m2);
		boolean isConn = false;
		
		if(indirect && !direct){
			isConn = true;
		}
		if(isConn){
			MyMethod prev = m1;
			
			for(MyMethod tmp: intermediate){
				iic*=super.getIDC(prev, tmp, true);
				prev = tmp;
			}
			
			return iic;
		}else{
			return 0.0;
		}
	}
	
	private boolean isDirectlyConnected(MyMethod m1, MyMethod m2){
		ArrayList<MyMethod> outs = m1.getFanOut();
		ArrayList<MyMethod> outs2 = m2.getFanOut();
		
		for(MyMethod out: outs){
			if(out.getID().equals(m2.getID())){
				return true;
			}
		}
		
		for(MyMethod out: outs2){
			if(out.getID().equals(m1.getID())){
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isConnected(MyMethod m, MyMethod m2){
		
		if(!isConn && !contains(m)){
			ArrayList<MyMethod> outs = m.getFanOut();
			for(MyMethod out: outs){
				if(out.getParent().getID().equals(m.getParent().getID())){
					
					intermediate.add(out);
					if(out.getID().equals(m2.getID())){
						isConn = true;
					}
					
					if(!m.getID().equals(out.getID())){
						this.isConnected(out, m2);
					}

				}
			}
		}
		
		return isConn;
	}
	
	
	private boolean contains(MyMethod m){
		for(MyMethod inter: intermediate){
			if(inter.getID().equals(m.getID())){
				return true;
			}	
		}
		
		return false;
	}
}
