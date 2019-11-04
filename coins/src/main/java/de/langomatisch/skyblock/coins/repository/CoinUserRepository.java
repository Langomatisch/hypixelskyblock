package de.langomatisch.skyblock.coins.repository;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.langomatisch.skyblock.coins.entity.CoinUser;
import de.langomatisch.skyblock.core.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.UUID;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class CoinUserRepository implements DataRepository<UUID, CoinUser> {
    private final SessionFactory sessionFactory;

    private ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

    public ListenableFuture<Boolean> containsId(UUID id) {
        return executorService.submit(() -> {
            CoinUser coinUser = sessionFactory.openSession().get(CoinUser.class, id);
            return coinUser != null;
        });
    }

    @Override
    public ListenableFuture<CoinUser> findById(UUID id) {
        return executorService.submit(() -> sessionFactory.openSession().get(CoinUser.class, id));
    }

    @Override
    public ListenableFuture<CoinUser> update(CoinUser user) {
        return executorService.submit(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.merge(user);
                transaction.commit();
            }

            return user;
        });
    }

    @Override
    public ListenableFuture<CoinUser> create(CoinUser user) {
        return executorService.submit(() -> {
            try (Session session = this.sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();
                session.saveOrUpdate(user);
                transaction.commit();
            }

            return user;
        });
    }
}
