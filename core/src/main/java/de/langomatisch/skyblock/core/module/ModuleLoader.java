package de.langomatisch.skyblock.core.module;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Getter
public class ModuleLoader {

    @Getter
    private static ModuleLoader instance;

    private Yaml yaml = new Yaml();
    private String path;
    private Map<String, Module> toLoad = new HashMap<>();
    private List<Module> loadedModules = new ArrayList<>();

    public ModuleLoader(String path) {
        long l = System.currentTimeMillis();
        instance = this;
        this.path = path;
        new File(path).mkdirs();
        loadModulesFromFolder();
        while (!toLoad.isEmpty()) {
            List<Module> toRemove = new ArrayList<>();
            for (Module module : toLoad.values()) {
                for (String s : module.getDependencies()) {
                    if (toLoad.containsKey(s)) continue;
                }
                module.onEnable();
                loadedModules.add(module);
                toRemove.add(module);
            }
            for (Module module : toRemove) {
                toLoad.remove(module.getName());
            }
        }
        System.out.println("loaded " + loadedModules.size() + " modules in " + (System.currentTimeMillis() - l) + "ms");
    }

    private void loadModulesFromFolder() {
        for (File file : Objects.requireNonNull(new File(path).listFiles())) {
            if (file.getName().endsWith(".jar")) {
                try {
                    ZipFile javaFile = new ZipFile(file);
                    ZipEntry entry = javaFile.getEntry("module.yml");
                    FileConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(javaFile.getInputStream(entry)));
                    String main = config.getString("main");
                    Module module = loadIntoRuntime(file, main);
                    String name = config.getString("name");
                    module.setName(name);
                    module.setAuthor(config.getString("author"));
                    module.setVersion(config.getDouble("version"));
                    module.setDependencies(config.getStringList("dependencies").toArray(new String[0]));
                    module.setFile(file.getParentFile());
                    module.setMain(main);
                    module.onLoad();
                    module.setupConfig();
                    toLoad.put(module.getName(), module);

                    File file1 = new File(path, "/" + name + "/locales/");
                    if(!file1.exists()) {
                        ZipEntry entry1 = javaFile.getEntry("locale/");
                        if (entry1.isDirectory()) {
                            if (!file1.exists()) file1.mkdirs();
                            Files.copy(javaFile.getInputStream(entry1), file1.toPath());
                        }
                    }

                    javaFile.close();
                } catch (IOException | IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Module loadIntoRuntime(File file, String mainClass) throws NoSuchMethodException, MalformedURLException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        URLClassLoader loader = (URLClassLoader) ModuleLoader.class.getClassLoader();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURL.setAccessible(true);
        addURL.invoke(loader, file.toURI().toURL());
        Class<?> main = loader.loadClass(mainClass);
        return (Module) main.getConstructor().newInstance();
    }

}
