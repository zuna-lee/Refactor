package zuna.util;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

import zuna.model.MyClass;
import zuna.model.MyMethod;

public class MethodFilter {

	public static int getOwnedNormalMethods(MyClass c){
		ArrayList<MyMethod> methods = c.getOwnedMethods();
		int cnt=0;
		for(MyMethod m: methods){
			if(isTarget(m)) cnt++; 
		}
		
		return cnt;
	}
	public static boolean isTarget(MyMethod m){
		boolean abst = Modifier.isAbstract(m.getMd().resolveBinding().getModifiers());
		return !(m.getStatementCnt()<1 || abst || m.getMd().isConstructor() 
				|| m.isOverride() || m.isSupport() || m.isCallToParent());
	}
}
