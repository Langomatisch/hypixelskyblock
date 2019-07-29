package de.langomatisch.skyblock.core.database;

import org.bukkit.configuration.ConfigurationSection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectorClient {
    private ConfigurationSection section;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private Connection mySQLInstance = null;
    private String configurationDatabase = "";
    private String configurationNickAPI = "";
    private String configurationCoinsPointsAPI = "";
    
    public MySQLConnectorClient(ConfigurationSection section ) {
        this.section = section;
        
        this.host = section.getString( "host" );
        this.port = section.getString( "port" );
        this.database = section.getString( "database" );
        this.username = section.getString( "username" );
        this.password = section.getString( "password" );
        this.configurationDatabase = section.getString( "databaseConfiguration" );
        this.configurationCoinsPointsAPI = section.getString( "databaseCoinsPointsAPI" );
        this.configurationNickAPI = section.getString( "databaseNickAPI" );
    }
    
    public Connection getMySQLInstance() {
        try {
            if ( ( this.mySQLInstance == null ) || ( this.mySQLInstance.isClosed() ) ) {
                Class.forName( "com.mysql.jdbc.Driver" );
                this.mySQLInstance = DriverManager.getConnection( "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?autoReconnect=true", this.username, this.password );
            }
        } catch ( ClassNotFoundException | SQLException e ) {
            e.printStackTrace();
        }
        return this.mySQLInstance;
    }
    
    public void close() {
        try {
            if ( ( this.mySQLInstance != null ) || ( !this.mySQLInstance.isClosed() ) ) {
                this.mySQLInstance.close();
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }
    
    public String getConfigurationDatabase() {
        return this.configurationDatabase;
    }
    
    public String getDatabase() {
        return this.database;
    }
    
    public String getConfigurationNickAPI() {
        return this.configurationNickAPI;
    }
    
    public String getConfigurationCoinsPointsAPI() {
        return this.configurationCoinsPointsAPI;
    }
}
