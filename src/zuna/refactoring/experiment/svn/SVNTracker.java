package zuna.refactoring.experiment.svn;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNRevision;

import zuna.refactoring.ProjectAnalyzer;


public class SVNTracker{

	
	private static Hashtable<Long, Vector<String>> logs = new Hashtable<Long, Vector<String>>();
	public static final int HEAD = -1;
	
	private static SVNHistoryTracker tracker;
	private String prjName;
	private String url;
	private String path;
	private int pathLength;
	
	public SVNTracker(String prjName) {
		this.prjName=prjName;
		this.init();
	}
	
	public void init(){
		if(prjName.startsWith("ArgoUML")) url = "http://argouml.tigris.org/svn/argouml/trunk";
		else if(prjName.startsWith("JEdit")) url = "svn://svn.code.sf.net/p/jedit/svn";
		else if(prjName.startsWith("JHotDraw")) url = "svn://svn.code.sf.net/p/jhotdraw/svn";
		else if(prjName.startsWith("JMeter")) url = "http://svn.apache.org/repos/asf/jmeter/trunk";
		else if(prjName.startsWith("Refactoring")) url = "https://163.239.200.152/svn/Refactoring/";
		
		if(prjName.startsWith("ArgoUML")) {
			path = "/src.*";
		}else if(prjName.startsWith("JEdit")) {
			path = "/jEdit/trunk.*";
			pathLength = path.length()-1;
		}else if(prjName.startsWith("JHotDraw")) {
			path = "/trunk/jhotdraw7/src/main/java.*";
			pathLength = path.length()-1;
		}else if(prjName.startsWith("JMeter")) {
			path = "/src.*";
		}else if(prjName.startsWith("Refactoring")) {
			path = "/src.*";
		}
	}
	
	public boolean connectToSVN() {
		tracker = new SVNHistoryTracker();
		DAVRepositoryFactory.setup();
		String name="";
		String password ="";
		name = "zuna";
		password = "tjrkdSELAB913";
		
		return tracker.connectToSVN(url, name, password);
	}

	public void trackSVN(long startRevision, long endRevision) {
		String[] paths = { "" };
		if(endRevision==-1) endRevision = SVNRevision.HEAD.getNumber();
		boolean changedPath = true;
		boolean strictNode = false;
		long limit = -1;
		
		ISVNLogEntryHandler handler = new ISVNLogEntryHandler() 
		{
			@Override
			public void handleLogEntry(SVNLogEntry entry) throws SVNException 
			{
				String message = entry.getMessage();
				
//				System.out.println(entry.getRevision() + ": " + message);
				if(this.isBugFixChange(message))
				{
					Vector<String> javafiles = filterJava(entry.getChangedPaths());
		
					if(javafiles.size()>1)
					{
						logs.put(entry.getRevision(), javafiles);
					}
				}
					
					
			}
			
			private boolean isBugFixChange(String message){
				if(message.toLowerCase().contains("bug") || message.toLowerCase().contains("fix") || message.toLowerCase().contains("error")
						 || message.toLowerCase().contains("fixes")|| message.toLowerCase().contains("findbugs")){
					return true;
				}
				return false;
			}
		};
		tracker.log(paths, startRevision, endRevision, changedPath, strictNode,
				limit, handler);
	}
	
	
	private Vector<String> filterJava(Map<String, SVNLogEntryPath> map) {
		Vector<String> result = new Vector<String>();
		
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			SVNLogEntryPath value = map.get(key);
			
			if (value.getPath().matches(".*\\.java") && value.getType()=='M') {
				String fileName = "";
				if(prjName.equals("ArgoUML") || prjName.startsWith("Refactoring")){
					fileName = this.getIDArgoUML(value.getPath());
				}else if (prjName.startsWith("PicketLink")){
					fileName = this.getIDPicketLink(value.getPath());
				}else{
					fileName = this.getID(value.getPath());
				}
				
				if(ProjectAnalyzer.project.getClassList().containsKey(fileName)) 
					result.add(fileName);
			}
		}
		
		return result;
	
	}

	private String getID(String fileName){
		
		String fileID=fileName;
		fileID = fileID.substring(pathLength, fileID.length()-5);
		fileID = fileID.replace("/", ".");
		
		return fileID;
	}
	
	private String getIDArgoUML(String fileName){
		String fileID=fileName;
		String[] token = fileName.split("/src/");
		
		fileID = token[token.length-1].substring(0, token[token.length-1].length()-5).replace("/", ".");
		return fileID;
	}

	private String getIDPicketLink(String fileName){
		String fileID=fileName;
		String[] token = fileName.split("/src/main/java/");
		
		fileID = token[token.length-1].substring(0, token[token.length-1].length()-5).replace("/", ".");
		return fileID;
	}
	
	
	
	public Hashtable<String, Integer> getBugFileIndex(){
		Hashtable<String, Integer> bugFiles = new Hashtable<String, Integer>();
		
		for(Long rev :logs.keySet()){
			Vector<String> changedFiles = logs.get(rev);
			for(String changedFile: changedFiles){
				if(!bugFiles.containsKey(changedFile)){
					bugFiles.put(changedFile, 1);
				}else{
					int count = bugFiles.get(changedFile);
					bugFiles.put(changedFile, ++count);
				}
			}
		}
		return bugFiles;
	}
}
