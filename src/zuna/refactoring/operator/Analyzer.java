package zuna.refactoring.operator;

import org.eclipse.core.resources.IProject;

import zuna.refactoring.ProjectAnalyzer;

public abstract class Analyzer {
	
	protected Analyzer(){
		
	}
	protected Analyzer(IProject iproject){
		this.getProject(iproject);
	}
	
	protected void getProject(IProject iproject){
		ProjectAnalyzer.analyze(iproject);
	}
	
	public abstract void analyze();
}
