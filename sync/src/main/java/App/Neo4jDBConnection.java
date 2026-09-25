package App;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class Neo4jDBConnection implements AutoCloseable{
	@Override
	public void close() throws Exception {
		driver.close();
	}

	private Driver driver;
	private static final String URI = "bolt://localhost:7687";
	private static final String DATABASE = "synchronousapi";
	private static final String USER = "neo4j";
	private static final String PASSWORD = "Dqk120825@";
	
	public Neo4jDBConnection() {
		this.driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
	}
	
	public Session getSession() {
		return driver.session(SessionConfig.forDatabase(DATABASE));
	}
}
