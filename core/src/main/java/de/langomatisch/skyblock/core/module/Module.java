package de.langomatisch.skyblock.core.module;

import de.langomatisch.skyblock.core.CorePlugin;
import de.langomatisch.skyblock.language.LanguageHandler;
import de.langomatisch.skyblock.language.SimpleLanguageHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Logger;

@Getter
@Setter(AccessLevel.PACKAGE)
public abstract class Module {

    private CorePlugin corePlugin;
    private static ModuleLoader moduleLoader = ModuleLoader.getInstance();
    private String name;
    private String author;
    private double version;
    private String[] dependencies;
    private File file;
    private String main;
    private CommandMap commandMap;
    private File dataFolder, configFile;
    private FileConfiguration config;
    private LanguageHandler languageHandler;
    private Logger logger;

    public Module() {
        corePlugin = CorePlugin.getInstance();
        final Field bukkitCommandMap;
        try {
            bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.logger = CorePlugin.getInstance().getLogger();
    }

    /**
     * setup the config for this module
     */
    void setupConfig() {
        dataFolder = new File(file, name);
        dataFolder.mkdirs();

        System.out.println(dataFolder);

        configFile = new File(dataFolder, "config.yml");
        try {
            if(configFile.createNewFile()) {
                getLogger().info("config created");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        System.out.println(dataFolder.toString());
        this.languageHandler = new SimpleLanguageHandler(this.getDataFolder());
    }

    /**
     * this method saves the config
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * will be called when this module gets enabled
     */
    public void onEnable() {

    }

    /**
     * will be called when the module gets disabled
     */
    public void onDisable() {

    }

    /**
     * will be called when the module first gets loaded
     */
    public void onLoad() {
    }

    protected static <M> M getModule(String s) {
        for (Module module : moduleLoader.getLoadedModules()) {
            if (module.getName().equalsIgnoreCase(s)) {
                return (M) module;
            }
        }
        return null;
    }

    protected void registerCommand(Command command) {
        commandMap.register(command.getName(), command);
    }

    protected void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, CorePlugin.getInstance());
    }

    public void registerListener(String path) {
        try {
            for (Class<?> aClass : Class.forName(path).getClasses()) {
                if (aClass.isInstance(Listener.class)) {
                    registerListener((Listener) aClass.newInstance());
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
