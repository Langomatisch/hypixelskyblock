package de.langomatisch.skyblock.coins.provider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.langomatisch.skyblock.coins.CoinsModule;
import de.langomatisch.skyblock.coins.entity.CoinsPlayer;
import de.langomatisch.skyblock.core.CorePlugin;
import lombok.Getter;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class CoinsProvider {

    private CoinsModule coinsModule;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

    public CoinsProvider(CoinsModule coinsModule) {
        this.coinsModule = coinsModule;
    }

    /***
     * retrieves the coins from that particular player
     *
     * @param uuid player uuid
     * @return future with current coins
     */
    public ListenableFuture<Double> getCoins(final UUID uuid) {
        return service.submit(() -> {
            System.out.println("a");
            CoinsPlayer coinsPlayer = coinsModule.getSessionFactory().openSession().get(CoinsPlayer.class, uuid.toString());
            System.out.println("b");
            if(coinsPlayer == null) return 0d;
            return coinsPlayer.getCoins();
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
            Session session = coinsModule.getSessionFactory().openSession();
            CoinsPlayer coinsPlayer = session.get(CoinsPlayer.class, uuid.toString());
            coinsPlayer.setCoins(amount);
            session.saveOrUpdate(coinsPlayer);
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
