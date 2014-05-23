package zuna.metric.cohesion.c3;

import java.io.IOException;
import java.sql.SQLException;

import tml.storage.Repository;

public class TMLRepository extends Repository{

	public TMLRepository(String luceneIndexPath) throws IOException, SQLException {
		super(luceneIndexPath);
	}
	
	
	
}
