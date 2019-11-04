package de.langomatisch.skyblock.core.database;

import com.google.common.collect.Maps;
import de.langomatisch.skyblock.core.CorePlugin;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import java.util.Map;

public class SessionFactoryProvider {
    private static StandardServiceRegistry registry;

    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;
    private final Class<?>[] classes;

    public SessionFactoryProvider(String databaseUrl, String databaseUser, String databasePassword, Class<?>... classes) {
        this.databaseUrl = databaseUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.classes = classes;
    }

    public static void shutdown() {
        if (SessionFactoryProvider.registry != null)
            StandardServiceRegistryBuilder.destroy(SessionFactoryProvider.registry);
    }

    public SessionFactory get() {
        Thread.currentThread().setContextClassLoader(CorePlugin.class.getClassLoader());
        System.out.println("load hibernate... with user " + databaseUser + "@" + databaseUrl);
        try {

            StandardServiceRegistryBuilder registryBuilder =
                    new StandardServiceRegistryBuilder();

            Map<String, Object> settings = Maps.newHashMap();
            settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
            settings.put(Environment.URL, databaseUrl);
            settings.put(Environment.USER, databaseUser);
            settings.put(Environment.PASS, databasePassword);
            settings.put(Environment.HBM2DDL_AUTO, "update");
            settings.put(Environment.SHOW_SQL, false);
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            settings.put("hibernate.hikari.connectionTimeout", "20000");
            settings.put("hibernate.hikari.minimumIdle", "10");
            settings.put("hibernate.hikari.idleTimeout", "300000");
            settings.put(Environment.USE_SECOND_LEVEL_CACHE, "true");
            settings.put(Environment.USE_QUERY_CACHE, "true");
            settings.put(Environment.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
            settings.put(Environment.ENABLE_LAZY_LOAD_NO_TRANS, "true");
            settings.put(Environment.ORDER_INSERTS, true);
            settings.put(Environment.ORDER_UPDATES, true);

            registryBuilder.applySettings(settings);

            registry = registryBuilder.build();
            MetadataSources sources = new MetadataSources(SessionFactoryProvider.registry);

            for (Class<?> entity : classes) {
                System.out.println("found entity: " + entity.getName());
                sources.addAnnotatedClass(entity);
            }


            Metadata metadata = sources.getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();
        } catch (Exception e) {
            if (SessionFactoryProvider.registry != null) {
                StandardServiceRegistryBuilder.destroy(SessionFactoryProvider.registry);
            }
            e.printStackTrace();
        }

        return null;
    }
}