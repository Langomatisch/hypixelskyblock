package de.langomatisch.skyblock.core.database;

public interface Database<T> {

    /**
     * Connects to the database
     */
    void connect();

    /**
     * Disconnects from the database if possible
     */
    void disconnect();

    /**
     * fetches results from the database
     * @param query query to get what you need
     * @param replacements for safe prepared statements
     * @return database data response
     */
    T query(String query, String... replacements);

    /**
     * a query which doesn't give any data back
     * @param query query to update/create data what you need
     */
    void update(String query, String... replacements);

    /**
     * check if the database is connected
     * @return true if connected
     */
    boolean isConnected();

}
