package AppStarter.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * class of Handle Message passing
 *
 */
public class MBox {

    private final String id;
    private final Logger log;
    private final List<Msg> mqueue = new ArrayList<>();

    public MBox(String id, Logger log) {
        this.id = id;
        this.log = log;
    }

    /**
     * send Message
     * @param msg Message Received
     */
    public final synchronized void send(Msg msg) {
        mqueue.add(msg);
        log.finest("id: " + id + " send: [ " + msg + "] ");
        notify();
    }

    public final synchronized Msg Receive() {
        while (mqueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Msg msg = mqueue.remove(0);
        log.finest("id: " + id + " Receive: [ " + msg + "] ");
        return msg;
    }

    /**
     * @return get the id of the mbox
     */
    public String getId() {
        return id;
    }
}
