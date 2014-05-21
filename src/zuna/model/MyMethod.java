package zuna.model;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;


public class MyMethod extends Element implements Cloneable {

	private boolean support;
	private MyClass parent;
	private ArrayList<MyMethod> fanOut = new ArrayList<MyMethod>();
	private ArrayList<MyMethod> fanIn = new ArrayList<MyMethod>();
	private ArrayList<MyParameter> parameters = new ArrayList<MyParameter>();
	private MyIdentifier identifier = new MyIdentifier();
	private boolean Abstract = false;
	private int statementCnt = 0;
	private boolean override;
	private boolean constructor=false;
	private boolean Implemented = true;
	private boolean Public;
	private boolean Private;
	private boolean Protected;
	private boolean Static = true;
	private ArrayList<MyField> reffedField = new ArrayList<MyField>();
	private int noOfParams = 0;
	private MethodDeclaration md;
	private boolean callToParent;
	private Hashtable<String, ITypeBinding> superTypes = new Hashtable<String, ITypeBinding>();
	private String name;
	
	public boolean isCallToParent() {
		return callToParent;
	}
	
	public void setOverride(IMethodBinding methodBind, ITypeBinding typeBind) {
	     /* get super class */
	     ITypeBinding superTypeBind = typeBind.getSuperclass();
	     if(superTypeBind == null)
	          return;

	     IMethodBinding overridenMethodBind = null;
	     if((overridenMethodBind = matchMethodInType(methodBind, superTypeBind)) == null)
	          setOverride(methodBind, superTypeBind);
	     else{
	    	 override = true;
//	    	 System.out.println(methodBind.getKey() + " Overrides " + overridenMethodBind.getKey());
	     }
	     
	     if(!override){
	    	 /* search in super interfaces also */
		     ITypeBinding[] interfaceBinds = typeBind.getInterfaces();
		     for(ITypeBinding interfaceBind : interfaceBinds) {   
		          if((overridenMethodBind = matchMethodInType(methodBind, interfaceBind)) == null)
		               setOverride(methodBind, superTypeBind);
		          else{
		        	  override = true;
//		        	  System.out.println(methodBind.getKey() + " Overrides " + overridenMethodBind.getKey());
		          }
		              
		     } 
	     }
	}
	
	/* now matching the method signature with methods inside the type binding
	 * using erasure of type bind it should work for generic methods as well */ 
	private IMethodBinding matchMethodInType(IMethodBinding methodBind, ITypeBinding typeBind) {
	 
	     /* iterate though all the methods within type bind */
	     for(IMethodBinding tMethodBind : typeBind.getDeclaredMethods()) {
	          if(!tMethodBind.getName().equals(methodBind.getName()))
	               continue;
	          if(tMethodBind.getParameterTypes().length != methodBind.getParameterTypes().length)
	               continue;
	 
	          ITypeBinding[] params1 = tMethodBind.getParameterTypes();
	          ITypeBinding[] param2 = methodBind.getParameterTypes();
	          
	          /* matching erasures instead of raw types */
	          for(int i = 0; i < params1.length; i++) {
	               if(params1[i].getErasure().equals(param2[i].getErasure()))
	                    continue;
	          }
	          return tMethodBind;
	     }
	     return null;
	}
	
	
	public void setCallToParent(MethodDeclaration md) {
		ITypeBinding typeBinding = md.resolveBinding().getDeclaringClass();
		getParentTypes(typeBinding);
		
		md.accept(new ASTVisitor(){
			public boolean visit(MethodInvocation inv){
				if(inv.resolveMethodBinding().getDeclaringClass() != null){
					ITypeBinding usedType = inv.resolveMethodBinding().getDeclaringClass();
					if(superTypes.containsKey(usedType.getKey())){
						callToParent = true;
					}
				}
				
				return true;
			}
			
			public boolean visit(SimpleName inv){
				if(inv.getParent() instanceof MethodInvocation){
					MethodInvocation methodInv = (MethodInvocation)inv.getParent();
					ITypeBinding usedType = methodInv.resolveMethodBinding().getDeclaringClass();
					if(superTypes.containsKey(usedType.getKey())){
						callToParent = true;
					}
				}
				
				return true;
			}
			
		});
	}
	
	private void getParentTypes(ITypeBinding binding){
		ITypeBinding superType = binding.getSuperclass();
		if(superType!=null){
			superTypes.put(superType.getKey(), superType);
			this.getParentTypes(superType);	
		}
	}
	
	public boolean isPublic() {
		return Public;
	}

	public Object clone() throws CloneNotSupportedException {
		
		MyMethod clone = (MyMethod) super.clone();
		clone.parameters = (ArrayList<MyParameter>) parameters.clone();
		clone.statementCnt = statementCnt;
		clone.constructor = (boolean) constructor;
		clone.support = (boolean) support;
		return clone;
	}
	
	public boolean isSupport() {
		return this.getStatementCnt()==1;
	}
	
	public void setSupport(boolean support) {
		this.support = support;
	}


	public void setPublic(boolean public1) {
		Public = public1;
	}

	public boolean isPrivate() {
		return Private;
	}

	public void setPrivate(boolean private1) {
		Private = private1;
	}

	public boolean isProtected() {
		return Protected;
	}

	public void setProtected(boolean protected1) {
		Protected = protected1;
	}

	public int getStatementCnt() {
		return statementCnt;
	}

	public boolean isStatic() {
		return Static;
	}

	public void setStatic(boolean static1) {
		Static = static1;
	}

	public boolean isAbstract() {
		return Abstract;
	}



	public boolean isOverride() {
		return override;
	}



	public boolean isConstructor() {
		return constructor;
	}



	public boolean isImplemented() {
		return Implemented;
	}





	public void setParent(MyClass parent) {
		this.parent = parent;
	}

	
	
	public int getNoOfParams() {
		return noOfParams;
	}



	public void setNoOfParams(int noOfParams) {
		this.noOfParams = noOfParams;
	}





	public void addReffedField(MyField reffedField) {
		this.reffedField.add(reffedField);
	}



	public ArrayList<MyMethod> getFanIn() {
		return fanIn;
	}


	public void setFanOut(ArrayList<MyMethod> fanOut) {
		this.fanOut = fanOut;
	}

	public void setFanIn(ArrayList<MyMethod> fanIn) {
		this.fanIn = fanIn;
	}

	public ArrayList<MyParameter> getParameters() {
		return parameters;
	}


	public void setParameters(ArrayList<MyParameter> parameters) {
		this.parameters = parameters;
	}
	
	public int getParameterCount() {
		
		return parameters.size();
	}
	
	public MyIdentifier getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(MyIdentifier identifier) {
		this.identifier = identifier;
	}

	public String toStringParameters() {
		
		String str = "";
		
		for (MyParameter myParameter : parameters) {
			
			str += myParameter.toString();
			str += "\t";
		}
		
		return str;
	}
	
	public String toStringParameters2() {
		
		String str = "";
		
		for (MyParameter myParameter : parameters) {
			
			str += myParameter.getType();
			str += ".";
			str += myParameter.getName();
			str += "\t";
			str += myParameter.getType();
			str += "\t";
			str += myParameter.getName();
			str += "\t";
		}
		
		return str;
	}
	
	public String toStringIdentifiers() {
		
		return identifier.toString();
	}
	
	public void setAbstract(boolean abstract1) {
		Abstract = abstract1;
	}

	public void setOverride(boolean override) {
		this.override = override;
	}

	public void setConstructor(boolean constructor) {
		this.constructor = constructor;
	}

	public void setImplemented(boolean implemented) {
		Implemented = implemented;
	}

	@SuppressWarnings("unchecked")
	public MyMethod(MethodDeclaration methodDecl, IMethodBinding md, MyClass parent) {
		super(MyMethod.makeMethodFullName(md), parent == null? false : parent.isLibrary());
		
		this.name = md.getName();
		this.parent = parent;
		this.md = methodDecl;
		super.setModifier(methodDecl.modifiers());
		if(parent != null) {
			parent.addMethod(this);
		}
	}
	
	
	public MyMethod(String id, String name, boolean lib) {
		super(id, lib);
		this.name = name;
	}
	
	public MyMethod(String id, boolean lib) {
		super(id, lib);
	}
	

	
	
	public MethodDeclaration getMd() {
		return md;
	}



	public void addFanOutMethod(MyMethod fanOutMethod) {
		this.fanOut.add(fanOutMethod);
	}

	@Override
	public void display() {
		System.out.println("Method [" + getID() + "]");
		for(MyMethod method : fanOut) {
			System.out.println("fanOut [" + method.getID()  + "]");
		}
		for (MyMethod method : fanIn) {
			System.out.println("fanIn [" + method.getID()  + "]");
		}
	}
	
	public ArrayList<MyMethod> getFanOut() {
		return this.fanOut;
	}
	
	public void addFanInMethod(MyMethod fanInMethod) {
		if(!this.fanIn.contains(fanInMethod)){
			this.fanIn.add(fanInMethod);	
		}
		
	}
	
	public MyClass getParent() {
		return parent;
	}
	
	public static String makeMethodFullName(IMethodBinding md) {
		String className = null;
		
		if(md.getDeclaringClass() == null) {
			className = "Anonymous";
		} else {
			className = md.getDeclaringClass().getQualifiedName();
			if( className.contains("<") ) {
				int start, last;
				String tempName = null;
				start = className.indexOf("<");
				last = className.indexOf(">");
				tempName= className.substring(0, start);
				tempName += className.substring(last + 1, className.length());
				className = tempName;
			}
		}
		
		String methodFullname =  className + "." + md.getName() + "(";
		
		ITypeBinding[] para = md.getParameterTypes();
		
		for (int i = 0; i < para.length; i++) {
			//ITypeBinding[] test = para[i]className.();
			/*String test = para[i].toString();
			if(test.contains(" extends ")) {
				String[] extendClassFullName = test.split(" extends ");
				String[] extendClassName = extendClassFullName[1].split("\\.");
				methodFullname += extendClassName[extendClassName.length];
			} else {
				methodFullname += para[i].getName() + " " ;				
			}*/
			methodFullname += para[i].getName() + " " ;	
		}
		return methodFullname + ")";
	}

	public static String makeMethodSignature(IMethodBinding imb) {
		String name = "";
		
		String justName = imb.getName().trim();
		ITypeBinding[] para = imb.getParameterTypes();
		
		name = justName + "-";
		for(ITypeBinding t : para){
			name += t.getName().trim() + "-" ;
		}
		
		return name;
	}
	
	public static String makeMethodSignature(MethodDeclaration md) {
		String name = "";
		String justName = md.getName().toString();
		List param = md.parameters();
		
		name = justName + "-";
		Iterator iter = param.iterator();
		while(iter.hasNext()){
			name += iter.next().toString().split(" ")[0] + "-" ;
		}
		
		return name;
	}

	private static String makeNestedClassName(String qualifiedName) {

		//parameter type�� inner class�� ���. ������ class�?
		String[] classNames = qualifiedName.split("\\.");
		if(classNames.length > 0) {
			qualifiedName = classNames[classNames.length-1];			
		}
		return qualifiedName;
	}



	public void setStatementCnt(int count) {
		this.statementCnt = count;
	}


	public ArrayList<MyField> getReferencedField() {
		return this.reffedField ;
	}
}
