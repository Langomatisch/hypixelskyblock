package de.langomatisch.skyblock.coins;

import de.langomatisch.skyblock.coins.command.CoinsCommand;
import de.langomatisch.skyblock.coins.command.SetCoinsCommand;
import de.langomatisch.skyblock.coins.entity.CoinUser;
import de.langomatisch.skyblock.coins.provider.CoinsProvider;
import de.langomatisch.skyblock.coins.repository.CoinUserRepository;
import de.langomatisch.skyblock.core.CorePlugin;
import de.langomatisch.skyblock.core.database.SessionFactoryProvider;
import de.langomatisch.skyblock.core.module.Module;
import lombok.Getter;
import org.hibernate.SessionFactory;

@Getter
public class CoinsModule extends Module {

    private CorePlugin corePlugin;
    private CoinsProvider coinsProvider;
    private SessionFactory sessionFactory;
    private CoinUserRepository repository;

    @Override
    public void onEnable() {
        this.corePlugin = CorePlugin.getInstance();
        registerCommand(new CoinsCommand(this));
        registerCommand(new SetCoinsCommand(this));
        sessionFactory = new SessionFactoryProvider("jdbc:mysql://localhost:3306/hypixel","root","", CoinUser.class).get();
        repository = new CoinUserRepository(sessionFactory);
        coinsProvider = new CoinsProvider(this, repository);
    }

    @Override
    public void onDisable() {

    }

}
