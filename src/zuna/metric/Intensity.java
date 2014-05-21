package zuna.metric;

import java.util.ArrayList;

import zuna.model.MyClass;
import zuna.model.MyField;
import zuna.model.MyMethod;


public class Intensity {

	public static double getIntensity(MyClass c){
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		boolean ref, inv;
		int interaction = 0;
		int totalInteraction = totalMethods(methods);
		
		for(MyMethod m: methods){
			
			if(!m.isOverride()){
			
				ArrayList<MyField> f = m.getReferencedField();
				ArrayList<MyMethod> out = m.getFanOut();
				ref = getInternalInteractionsViaField(f, c);
				inv = getInternalInteractionsViaMethod(out, c);
				if(ref||inv) ++ interaction;
				
			}
			
		}
		
		return (double)interaction/(double)totalInteraction;
	}
	
	private static int totalMethods(ArrayList<MyMethod> methods){
		int total = 0;
		
		for(MyMethod m: methods){
			if(!m.isOverride()) total++;
		}
		
		return total;
	}
	
	private static boolean getInternalInteractionsViaField(ArrayList<MyField> fields, MyClass c){
		boolean flg = false;
		for(MyField f: fields){
			if(f.getParent().getID().equals(c.getID())){
				flg = true;
				break;
			}
		}
		return flg;
	}
	
	private static boolean getInternalInteractionsViaMethod(ArrayList<MyMethod> methods, MyClass c){
		boolean flg = false;
		for(MyMethod m: methods){
			if(m.getParent().getID().equals(c.getID())){
				flg=true;
				break;
			}
		}
		return flg;
	}
	
}
