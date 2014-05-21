package zuna.metric.classDS;

import zuna.model.MyClass;
import zuna.model.MyPackage;
import zuna.refactoring.ProjectAnalyzer;

public class InformationContents4System {

	private static int se=0;

	public void calculateIC(){
		se=0;
		MyPackage root = ProjectAnalyzer.project.getPackageList().get("ROOT");
		this.noOfChild(root);
		double max = se;
		double maxIC = -(Math.log((double)1/max)/ Math.log(2));
		
		for(String key: ProjectAnalyzer.project.getPackageList().keySet()){
			MyPackage p = ProjectAnalyzer.project.getPackageList().get(key);
			if(!p.getID().equals("")){
				se=0;
				this.noOfChild(p);
//				System.out.println(p.getID() + ":" +  max + ":" + se + ":" + -(Math.log((double)se/max)/ Math.log(2))/maxIC);
				p.setIc(-(Math.log((double)se/max)/ Math.log(2))/maxIC);
			}
		}
	}
	
	private void noOfChild(MyPackage p){
		se++;
		for(String key: p.getPackageChildren().keySet()){
			this.noOfChild(p.getPackageChildren().get(key));
		}
		
		for(String key: p.getClassChildren().keySet()){
			MyClass c = p.getClassChildren().get(key);
			if(c.getOutterClassUri().equals("java.lang.Object")){
				se++;
			}
		}
	}
}