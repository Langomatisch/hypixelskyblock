package de.langomatisch.skyblock.coins.provider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.langomatisch.skyblock.coins.CoinsModule;
import de.langomatisch.skyblock.core.database.DatabaseConnection;
import de.langomatisch.skyblock.core.database.MySQLConnectorClient;
import lombok.Getter;

import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class CoinsProvider {

    private CoinsModule coinsModule;
    private MySQLConnectorClient client;
    private DatabaseConnection connection;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    public CoinsProvider(CoinsModule module) {
        this.coinsModule = module;
        this.client = new MySQLConnectorClient(coinsModule.getConfig().getConfigurationSection("mysql"));
        this.connection = new DatabaseConnection(coinsModule.getCorePlugin(), client.getMySQLInstance());
        //TODO: Table creation
    }

    /***
     * retrieves the coins from that particular player
     *
     * @param uuid player uuid
     * @return future with current coins
     */
    public ListenableFuture<Integer> getCoins(final UUID uuid) {
        executorService.execute(new Runnable() {
            public void run() {
                service.submit(new Callable<Integer>() {
                    public Integer call() throws Exception {
                        ResultSet resultSet = connection.prepareStatement("SELECT coins FROM players.coins WHERE uuid = ?", uuid.toString());
                        if (!resultSet.next()) return 0;
                        return resultSet.getInt("coins");
                    }
                });
            }
        });
        return null;
    }

    /***
     * sets the coins for that particular player.
     *
     * WARNING: old coins gonna be deleted
     * @param uuid player uuid
     * @param amount coins you want to set the player.
     * @return callback on finished.
     */
    public ListenableFuture<Void> setCoins(UUID uuid, int amount) {
        return service.submit(new Callable<Void>() {
            public Void call() {
                connection.updateStatement("");
                return null;
            }
        });
    }

    /***
     * adds coins to that players purse
     *
     * @param uuid player uuid
     * @param amount coins you want to add
     * @return callback on finished
     */
    public ListenableFuture<Void> addCoins(UUID uuid, int amount) {
        return null;
    }

    /***
     * removes the coins for that particular player
     *
     * @param uuid player uuid
     * @param amount coins you want to remove
     * @return callback on finished
     */
    public ListenableFuture<Void> removeCoins(UUID uuid, int amount) {
        return null;
    }

    /***
     * TODO: Coming in Future
     * get coins from a particular {@link Date}
     *
     * @param uuid player uuid
     * @param date date you want the coins from
     * @return coins from that particular date
     */
    public ListenableFuture<Integer> getCoinsFromDate(UUID uuid, Date date) {
        return null;
    }

}
