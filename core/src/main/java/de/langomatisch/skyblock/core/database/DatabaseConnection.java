package de.langomatisch.skyblock.core.database;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.IntStream;

/**
 * @author Yxns
 */
public class DatabaseConnection {
    
    @Getter
    private String database;
    @Getter
    private Connection connection = null;
    @Getter
    private String shortcut;
    @Getter
    private Plugin plugin;
    @Getter
    @Setter
    private String table;
    
    /**
     * Constructor
     *
     * @param plugin     plugin which uses this class
     * @param connection Connection to database
     */
    public DatabaseConnection(Plugin plugin, Connection connection ) {
        this.connection = connection;
        this.shortcut = "[" + plugin.getName() + "]";
        this.plugin = plugin;
    }
    
    /**
     * Constructor
     *
     * @param plugin     plugin which uses this class
     * @param connection Connection to database
     * @param database   Database to connect
     */
    public DatabaseConnection(Plugin plugin, Connection connection, String database ) {
        this( plugin, connection );
        this.database = database;
    }
    
    /**
     * Constructor
     *
     * @param plugin     plugin which uses this class
     * @param connection Connection to database
     * @param database   Database to connect
     * @param table      Table from database
     */
    public DatabaseConnection(Plugin plugin, Connection connection, String database, String table ) {
        this( plugin, connection );
        this.database = database;
        this.table = table;
    }
    
    /**
     * @param sql        the sql statement
     * @param parameters parameters given for the sql statement
     * @return ResultSet from the connection
     * @see PreparedStatement
     */
    public ResultSet prepareStatement( String sql, Object... parameters ) {
        sql = replaceGlobalVariables( sql );
        
        try {
            PreparedStatement statement = this.connection.prepareStatement( sql );
            
            if ( parameters.length > 0 ) {
                IntStream.rangeClosed( 1, parameters.length ).forEach( i -> {
                    try {
                        statement.setObject( i, parameters[ i - 1 ] );
                    } catch ( SQLException e ) {
                        e.printStackTrace();
                    }
                } );
            }
            
            statement.execute();
            return statement.getResultSet();
        } catch ( SQLException e ) {
            System.err.println( this.shortcut + " Failed statement: " + sql );
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * @param sql        the sql statement
     * @param parameters parameters given for the sql statement
     * @see PreparedStatement
     */
    public void updateStatement( String sql, Object... parameters ) {
        sql = replaceGlobalVariables( sql );
        
        try {
            PreparedStatement statement = this.connection.prepareStatement( sql );
            
            if ( parameters.length > 0 ) {
                IntStream.rangeClosed( 1, parameters.length ).forEach( i -> {
                    try {
                        statement.setObject( i, parameters[ i - 1 ] );
                    } catch ( SQLException e ) {
                        e.printStackTrace();
                    }
                } );
            }
            
            statement.executeUpdate();
        } catch ( SQLException e ) {
            System.err.println( this.shortcut + " Failed statement: " + sql );
            e.printStackTrace();
        }
    }
    
    public boolean hasNext( String sql, Object... parameters ) {
        try {
            return this.prepareStatement( sql, parameters ).next();
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void runAsynchronously( Runnable runnable ) {
        Bukkit.getScheduler().runTaskAsynchronously( this.plugin, runnable );
    }
    
    private String replaceGlobalVariables( String s ) {
        return s.replaceAll( "_database", this.database ).replaceAll( "_table", this.table );
    }
}
