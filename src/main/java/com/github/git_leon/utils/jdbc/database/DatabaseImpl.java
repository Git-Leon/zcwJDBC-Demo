package com.github.git_leon.utils.jdbc.database;

import com.github.git_leon.utils.jdbc.connection.ConnectionBuilder;
import com.github.git_leon.utils.jdbc.connection.ConnectionWrapper;
import com.github.git_leon.utils.jdbc.executor.StatementExecutor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;

/**
 * Created by leon on 3/13/18.
 * implementation of `DatabaseInterface` to provide some default implementations oriented around construction
 * ensures this implementation can be decorated via compostion of DatabaseImpl and inheritance of DatabaseInterface
 */
public class DatabaseImpl implements DatabaseInterface {

    private final String name;
    private final EntityManager entityManager;
    private final ConnectionBuilder connectionBuilder;
    private Connection connection;

    public DatabaseImpl(ConnectionBuilder connectionBuilder, String name) {
        Connection connection = connectionBuilder.build();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(name);

        this.name = name;
        this.entityManager = emf.createEntityManager();
        this.connection = connection;
        this.connectionBuilder = connectionBuilder;
    }

    @Override
    public synchronized Connection getConnection() {
        ConnectionWrapper connectionWrapper = new ConnectionWrapper(connection);
        if (connectionWrapper.isClosed()) {
            this.connection = connectionBuilder.build();
        }
        return connection;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ConnectionBuilder getConnectionBuilder() {
        return connectionBuilder;
    }

    @Override
    public StatementExecutor getStatementExecutor() {
        return new StatementExecutor(getConnection());
    }

    @Override
    public ConnectionWrapper getConnectionWrapper() {
        return new ConnectionWrapper(getConnection());
    }

    @Override
    public DatabaseTable getTable(String tableName) {
        return new DatabaseTable(this, tableName);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

}
