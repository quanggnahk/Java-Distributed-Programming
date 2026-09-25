package App;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.async.AsyncSession;

public class Neo4jDBConnection implements AutoCloseable{
	private final Driver driver;
	 private static final String URI = "bolt://localhost:7687";
	 private static final String DATABASE = "asynchronousapi";
	 private static final String USER = "neo4j";
	 private static final String PASSWORD = "Dqk120825@";
	 public Neo4jDBConnection() {
	 this.driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
	 }
	 public AsyncSession getSession() {
	 return driver.asyncSession(SessionConfig.forDatabase(DATABASE));
	 }
	 @Override
	 public void close() throws Exception {
	 driver.close();
	 }
}