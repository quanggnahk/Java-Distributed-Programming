package App;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.reactive.RxSession;

public class Neo4jDBConnection implements AutoCloseable {
    private final Driver driver;
    private static final String URI = "bolt://localhost:7687";
    private static final String DATABASE = "reactivestreamapi";
    private static final String USER = "neo4j";
    private static final String PASSWORD = "Dqk120825@";

    public Neo4jDBConnection() {
        this.driver = GraphDatabase.driver(URI, AuthTokens.basic(USER, PASSWORD));
    }

    public RxSession getSession() {
        return driver.rxSession(SessionConfig.forDatabase(DATABASE));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }
}