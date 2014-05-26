package zuna.metric.classDS;

import java.util.SortedSet;
import java.util.TreeSet;

import zuna.model.Element;
import zuna.model.MyClass;
import zuna.model.MyPackage;
import zuna.refactoring.ProjectAnalyzer;


public class InformationContents {

	private int totEntities;
	private static int se=0;
	private static SortedSet<Double> ICTable = new TreeSet<Double>();
	public static double maxIC = 0.0;

	public InformationContents(){
		se=0;
		ICTable.clear();
		maxIC = 0.0;
		this.calculateIC();
	}

	public int getTotEntities() {
		return totEntities;
	}

	public void setTotEntities(int totEntities) {
		this.totEntities = totEntities;
	}

	public void calculateIC(){
		this.totEntities = this.calculTotalEntities() + 1;
		for(String key: ProjectAnalyzer.project.getPackageList().keySet()){
			InformationContents.se=0;
			double se = this.getNoOfSubEntities(ProjectAnalyzer.project.getPackageList().get(key));
			ProjectAnalyzer.project.getPackageList().get(key).setIc(-Math.log( se / this.totEntities));
			ProjectAnalyzer.project.getPackageList().get(key).setSe(se);
		}
		
		for(String key: ProjectAnalyzer.project.getClassList().keySet()){
			
			InformationContents.se=0;
			MyClass curClass = ProjectAnalyzer.project.getClass(key);
			if(curClass.getOwnedMethods().size()!=0){
				double se = this.getNoOfSubEntities(ProjectAnalyzer.project.getClass(key));
				double entropy = -Math.log( se / this.totEntities);
				
				InformationContents.ICTable.add(entropy);
				ProjectAnalyzer.project.getClassList().get(key).setIc(entropy);
				ProjectAnalyzer.project.getClassList().get(key).setSe(se);
				
			}
		}
		
		InformationContents.maxIC = InformationContents.ICTable.last();
//		System.out.println(ProjectAnalyzer.project.getPackage("ROOT").getIc() + ":::" + this.totEntities + ":::" +
//				this.getNoOfSubEntities(ProjectAnalyzer.project.getPackageList().get("ROOT")));
		
	}
	
	private int getNoOfSubEntities(Element e) {
		
		if(e instanceof MyPackage){
			MyPackage p = (MyPackage) e;
			for(String key: p.getPackageChildren().keySet()){
				++InformationContents.se;
				this.getNoOfSubEntities(p.getPackageChildren().get(key));
			}
			for(String key: p.getClassChildren().keySet()){
				++InformationContents.se;
				this.getNoOfSubEntities(p.getClassChildren().get(key));
			}
		}else if(e instanceof MyClass){
			MyClass c = (MyClass) e;
			InformationContents.se += c.getOwnedMethods().size();
		}
		
		return InformationContents.se;
	}
	
	private int calculTotalEntities() {
		return this.totEntities = ProjectAnalyzer.project.getPackageList().size() + 
				ProjectAnalyzer.project.getClassList().size() + ProjectAnalyzer.project.getMethodList().size();
	}
}
