package zuna.metric.classDS;

import zuna.model.MyClass;
import zuna.refactoring.ProjectAnalyzer;

public class InformationContentsInInheritance {
	private static int se=0;
	
	public void calculateIC(){
		se=0;
		MyClass root = ProjectAnalyzer.project.getClassList().get("ROOT");
		this.noOfChild(root);
		this.noOfImplemented(root);
		
		double max = se;
		double maxIC = -(Math.log((double)1/max)/ Math.log(2));
		
		for(String key: ProjectAnalyzer.project.getClassList().keySet()){
			MyClass c = ProjectAnalyzer.project.getClassList().get(key);
			if(!c.getID().equals("")){
				se=1;
				this.noOfChild(c);
				this.noOfImplemented(c);
//				System.out.println(c.getID() + ":" +  max + ":" + se + ":" + -(Math.log((double)se/max)/ Math.log(2))/maxIC);
				c.setIcIninheritance(-(Math.log((double)se/max)/ Math.log(2))/maxIC);
			}
		}
	}
	
	private void noOfChild(MyClass c){
		
		for(MyClass child: c.getChildClasses()){
			if(child!=null){
				if(child.getID().equals("java.lang.Object") || child.getOutterClassUri().equals("java.lang.Object")){
					se++;
					noOfChild(child);
				}
			}
			
		}
	}
	
	private void noOfImplemented(MyClass c){
		
		for(MyClass child: c.getImplementedClasses()){
			if(child.getID().equals("java.lang.Object") || child.getOutterClassUri().equals("java.lang.Object")){
				se++;
				noOfChild(child);
			}
		}
	}
}
