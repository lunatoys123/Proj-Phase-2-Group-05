package AppStarter.common;

import java.util.logging.Logger;

//Handle all Hardware thread
public abstract class AppThread implements Runnable {

    protected String id;
    protected AppStarter appStarter;
    protected Logger log;
    protected MBox mbox;

    public AppThread(String id, AppStarter appStarter) {
        this.id = id;
        this.appStarter = appStarter;
        log = appStarter.getLogger();
        mbox = new MBox(id, log);
        appStarter.registerThread(this);
        log.fine(id + "created!");
    }

    public String getId() {
        return id;
    }

    public MBox getMbox() {
        return mbox;
    }
}
