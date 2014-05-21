package zuna.parser.visitor;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AbstractTypeDeclarationVisitor extends ASTVisitor {
	private ArrayList<TypeDeclaration> types = new ArrayList<TypeDeclaration>();
	private ArrayList<EnumDeclaration> enums = new ArrayList<EnumDeclaration>();
	
	public boolean visit(TypeDeclaration md) {   
		types.add(md); 
		return true;
	}
	
	public boolean visit(EnumDeclaration ed) {
		enums.add(ed);
		return true;
	}
	public ArrayList<TypeDeclaration> getTypes() {
		return types;
	}
	public ArrayList<EnumDeclaration> getEnums() {
		return enums;
	}
}
