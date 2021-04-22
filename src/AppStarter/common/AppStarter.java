package AppStarter.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AppStarter {

    protected Logger log = null;
    protected String id;
    protected String configPath;
    private ConsoleHandler consoleHandler;
    private FileHandler fileHandler;
    private final Properties properties;
    private final Map<String, AppThread> appThreadMap;

    /**
     * @param id id of the Hardware
     * @param configPath path of Properties file
     *                   initialize the log and properties
     */
    public AppStarter(String id, String configPath) {
        this.properties = new Properties();
        this.id = id;
        this.configPath = configPath;
        this.consoleHandler = null;
        this.fileHandler = null;

        Thread.currentThread().setName(this.id);
        try {
            FileInputStream in = new FileInputStream(configPath);
            properties.load(in);
            in.close();
            consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new LogFormmater());
            fileHandler = new FileHandler("config/" + id + ".log", false);
            fileHandler.setFormatter(new LogFormmater());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        log = Logger.getLogger(id);
        log.addHandler(consoleHandler);
        log.addHandler(fileHandler);
        log.setUseParentHandlers(false);
        log.setLevel(Level.FINER);
        fileHandler.setLevel(Level.parse(properties.getProperty("ConsoleLevel", "INFO")));
        consoleHandler.setLevel(Level.parse(properties.getProperty("FileLogLevel", "INFO")));
        appThreadMap = new HashMap<>();
    }

    protected abstract void startApp();

    protected abstract void stopApp();

    /**
     * @param appThread Hardware component
     *                  add Thread to the map
     */
    public void registerThread(AppThread appThread) {
        log.info("add thread: " + appThread.getId());
        synchronized (appThreadMap) {
            appThreadMap.put(appThread.getId(), appThread);
        }
    }

    /**
     * @param appThread Hardware component
     *                  Remove Thread to the map
     */
    public void UnRegisterThread(AppThread appThread) {
        log.info(" remove thread: " + appThread.getId());
        synchronized (appThreadMap) {
            appThreadMap.remove(appThread.getId());
        }
    }

    /**
     * @param key get the specific Thread
     * @return the specific Thread
     */
    public AppThread getThread(String key) {
        synchronized (appThreadMap) {
            return appThreadMap.get(key);
        }
    }

    /**
     * @return log Properties
     */
    public Logger getLogger() {
        return log;
    }

    /**
     * @return Properties file
     */
    public Properties getProperties() {
        return properties;
    }
}