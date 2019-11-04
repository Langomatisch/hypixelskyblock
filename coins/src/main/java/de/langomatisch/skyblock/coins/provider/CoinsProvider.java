package de.langomatisch.skyblock.coins.provider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.langomatisch.skyblock.coins.CoinsModule;
import de.langomatisch.skyblock.coins.entity.CoinUser;
import de.langomatisch.skyblock.coins.event.PlayerCoinsChangeEvent;
import de.langomatisch.skyblock.coins.repository.CoinUserRepository;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class CoinsProvider {

    private CoinsModule coinsModule;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    private CoinUserRepository repository;

    public CoinsProvider(CoinsModule coinsModule, CoinUserRepository repository) {
        this.coinsModule = coinsModule;
        this.repository = repository;
    }

    /***
     * retrieves the coins from that particular player
     *
     * @param uuid player uuid
     * @return future with current coins
     */
    public ListenableFuture<Double> getCoins(final UUID uuid) {
        return service.submit(() -> {
            CoinUser coinUser = repository.findById(uuid).get();
            if (coinUser == null) {
                coinUser = new CoinUser();
                coinUser.setUuid(uuid);
                coinUser.setCoins(0);
                repository.create(coinUser);
            }
            return coinUser.getCoins();
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
    public ListenableFuture<Void> setCoins(UUID uuid, double amount) {
        return service.submit(() -> {
            CoinUser coinsPlayer = repository.findById(uuid).get();
            PlayerCoinsChangeEvent event = new PlayerCoinsChangeEvent(uuid, coinsPlayer.getCoins(), amount);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return null;
            coinsPlayer.setCoins(amount);
            repository.update(coinsPlayer);
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
