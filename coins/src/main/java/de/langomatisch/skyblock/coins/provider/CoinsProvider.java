package de.langomatisch.skyblock.coins.provider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.langomatisch.skyblock.coins.CoinsModule;
import de.langomatisch.skyblock.coins.event.PlayerCoinsChangeEvent;
import de.langomatisch.skyblock.core.database.Database;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class CoinsProvider {

    private CoinsModule coinsModule;
    private Database database;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    public CoinsProvider(Database database) {
        this.database = database;
        //TODO: Table creation
    }

    /***
     * retrieves the coins from that particular player
     *
     * @param uuid player uuid
     * @return future with current coins
     */
    public ListenableFuture<Integer> getCoins(final UUID uuid) {
        return service.submit(() -> {
            ResultSet resultSet = (ResultSet) database.query("SELECT coins FROM hypixel.coins WHERE uuid = ?", uuid.toString());
            if (!resultSet.next()) return 0;
            return resultSet.getInt("coins");
        });
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
        return service.submit(() -> {
            Integer coinsNow = getCoins(uuid).get();
            PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(uuid, coinsNow, coinsNow + amount);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                database.update("INSERT INTO hypixel.coins(uuid, coins) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE coins = ?", uuid.toString(), String.valueOf(amount), String.valueOf(amount));
            }
            return null;
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
