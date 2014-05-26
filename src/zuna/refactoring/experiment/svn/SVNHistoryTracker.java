package zuna.refactoring.experiment.svn;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;


public class SVNHistoryTracker {
	
	private static SVNRepository repository = null;

	public boolean connectToSVN(String url, String name, String password) {
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
			if(repository == null) return false;
			ISVNAuthenticationManager authManager =
	                   SVNWCUtil.createDefaultAuthenticationManager(name, password);
			repository.setAuthenticationManager(authManager);
		} catch (SVNException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void log(String[] paths, long startRevision, long endRevision,
			boolean changedPath, boolean strictNode, long limit, ISVNLogEntryHandler handler) {
		
		try {
			repository.log(paths, startRevision, endRevision, changedPath, strictNode, limit, handler);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		
	}
	
}
