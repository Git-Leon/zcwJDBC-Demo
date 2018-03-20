package com.zipcodewilmington.jdbc.utils.database.dbseed;


import com.zipcodewilmington.jdbc.utils.database.MigrationsTable;

import java.io.IOException;
import java.sql.Connection;

public class WilhemDatabaseSeeder {
    private final Connection connection;

    public WilhemDatabaseSeeder(Connection connection) {
        this.connection = connection;
    }

    public boolean run() {
        MigrationsTable migrationsTable = new MigrationsTable(connection);
        try {
            migrationsTable.importFilesFromResources();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
