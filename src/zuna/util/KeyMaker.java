package zuna.util;

import zuna.model.MyClass;
import zuna.model.MyMethod;

public class KeyMaker {

	
	public static String getKey(MyMethod m1, MyMethod m2){
		return m1.getID() + ":" + m2.getID();
	}
	
	public static String getKey(MyClass c1, MyClass c2){
		return c1.getID() + ":" + c2.getID();
	}
	
	public static String getKeyInMutation(MyClass c1, MyClass c2){
		return c1.getID() + "+" + c2.getID();
	}
	
	public static String getKey(String m1, String m2){
		return m1 + ":" + m2;
	}
	
}
