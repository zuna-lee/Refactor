package zuna.model;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MyClass extends Element implements Cloneable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8195160897380045654L;
	private MyPackage parent;
	private String name;
	private TypeDeclaration declaration;
	private CompilationUnit cu;
	private ArrayList<MyMethod> ownedMethods = new ArrayList<MyMethod>();
	private ArrayList<MyField> owendField = new ArrayList<MyField>();
	
	private boolean enumuration = false;
	private boolean isAbstract = false;
	private boolean isInterface = false;
	private boolean completelyCohesive=false;
	private double icIninheritance=0.0;
	private double noOfCalls = 0.0;
	
	private String outterClassUri = "java.lang.Object";
	private MyClass superClass; 
	private ArrayList<MyClass> childClasses = new ArrayList<MyClass>();
	private ArrayList<MyClass> implementedClasses = new ArrayList<MyClass>();
	private ArrayList<MyClass> interfaces = new ArrayList<MyClass>();
	
	
	
	public String getName() {
		return name;
	}

	public double getNoOfCalls() {
		return noOfCalls;
	}

	public void addNoOfCalls() {
		++this.noOfCalls;
	}

	public double getIcIninheritance() {
		return icIninheritance;
	}

	public void setIcIninheritance(double icIninheritance) {
		this.icIninheritance = icIninheritance;
	}

	public MyClass(String fullName, TypeDeclaration declaration, CompilationUnit cu, MyPackage parent) {
		super( fullName, parent.isLibrary());
		this.name = declaration.getName().getIdentifier();
		this.cu = cu;
		this.declaration = declaration;
		isAbstract = declaration.isInterface() || Modifier.isAbstract(declaration.getModifiers());		
		isInterface = declaration.isInterface();
		
		this.setParent(parent);
		
	}
	
	public MyClass(String fullName, boolean lib) {
		super(fullName, lib);
		String[] token = fullName.split("\\.");
		this.name = token[token.length-1];
	}
	
	public ArrayList<MyClass> getChildClasses() {
		return childClasses;
	}

	public Object clone() throws CloneNotSupportedException {
		
		MyClass clone = (MyClass) super.clone();
		clone.ownedMethods = (ArrayList<MyMethod>) ownedMethods.clone();
		clone.owendField = (ArrayList<MyField>) owendField.clone();
		
		return clone;
	}

	public void addChildClasses(MyClass childClass) {
		this.childClasses.add(childClass);
	}



	public ArrayList<MyClass> getImplementedClasses() {
		return implementedClasses;
	}

	public ArrayList<MyClass> getInterface(){
		return this.interfaces;
	}

	public void addInterface(MyClass itfc){
		this.interfaces.add(itfc);
	}
	
	public void addImplementedClasses(MyClass implementedClass) {
		this.implementedClasses.add(implementedClass);
	}

	public void setInterfaces(ArrayList<MyClass> interfaces) {
		this.interfaces = interfaces;
	}



	public boolean isAbstract() {
		return isAbstract;
	}



	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}



	public boolean isInterface() {
		return isInterface;
	}



	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}



	public boolean isCompletelyCohesive() {
		return completelyCohesive;
	}



	public void setCompletelyCohesive(boolean completelyCohesive) {
		this.completelyCohesive = completelyCohesive;
	}





	public MyClass(String fullName, MyPackage parent) {
		super( fullName, parent.isLibrary());
		this.setParent(parent);
		String[] token = fullName.split("\\.");
		this.name = token[token.length-1];
	}
	
	

	public boolean isEnumuration() {
		return enumuration;
	}



	public void setEnumuration(boolean enumuration) {
		this.enumuration = enumuration;
	}



	public String getOutterClassUri() {
		return outterClassUri;
	}





	public MyClass getSuperClass() {
		return superClass;
	}





	public void setSuperClass(MyClass superClass) {
		this.superClass = superClass;
	}





	public CompilationUnit getCu() {
		return cu;
	}

	public TypeDeclaration getDeclaration() {
		return declaration;
	}


	public MyClass(ITypeBinding bindingClass, MyPackage parent) {
		super(bindingClass.getQualifiedName(), parent.isLibrary());
		isAbstract = bindingClass.isInterface() || Modifier.isAbstract(bindingClass.getModifiers());		
		isInterface = declaration.isInterface();
		
		this.setParent(parent);
	}

	public MyClass(String fullName, EnumDeclaration declartion, MyPackage parent) {
		super( fullName, parent.isLibrary());
		try{
			
			this.setParent(parent);
			this.setEnumuration(true);
		}catch(java.lang.NullPointerException e){
			
		}
	}
	
	private void setParent(MyPackage parent) {
		this.parent = parent;
		if(this.parent != null) {
			parent.addClassChild(this);
		}
	}
	
	public void setOutterClassUri(String uri) {
		this.outterClassUri = uri;
	}
	public ArrayList<MyField> getOwendField() {
		return owendField;
	}

	public void setOwendField(ArrayList<MyField> owendField) {
		this.owendField = owendField;
	}

	public void addOwendField(MyField field) {
		if(!this.owendField.contains(field)){
			this.owendField.add(field);
		}
	}


	public ArrayList<MyMethod> getOwnedMethods() {
		return ownedMethods;
	}

	public void addMethod(MyMethod ownedMethod) {
		this.ownedMethods.add(ownedMethod);
	}
	
	public void setOwnedMethods(ArrayList<MyMethod> ownedMethods) {
		this.ownedMethods = ownedMethods;
	}

	public MyPackage getParent() {
		return parent;
	}

	@Override
	public void display() {
		super.display();
		for (MyMethod method : ownedMethods) {
			System.out.print("ownedMethods [");
			method.display();
		}
	}

	

}
