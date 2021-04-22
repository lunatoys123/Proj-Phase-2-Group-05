package AppStarter.Hardware;

import AppStarter.common.AppStarter;
import AppStarter.common.AppThread;
import AppStarter.common.MBox;
import AppStarter.common.Msg;

public class HardwareHandler extends AppThread {
    protected MBox ATMSS_BOX = null;

    public HardwareHandler(String id, AppStarter appStarter) {
        super(id, appStarter);
    }

    @Override
    public void run() {
        ATMSS_BOX = appStarter.getThread("ATM_SS").getMbox();
        log.info(id + " Starting thread! ");

        for (boolean quit = false; !quit; ) {
            Msg msg = mbox.Receive();

            switch (msg.getType()) {
                case poll:
                    log.info("Receive poll message from " + msg.getId() + " [" + this.id + "]");
                    //ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.pollAck, mbox).setDetails("Poll Acknowledgement").build());
                    checkHardware();
                    break;
                case Terminate:
                    log.info("Receive Terminate message from " + msg.getId());
                    closeWindow();
                    quit = true;
                    break;
                case stop_component:
                    stopComponent();
                    break;
                default:
                    processMsg(msg);

            }
        }

        appStarter.UnRegisterThread(this);
        log.info("UnRegister thread: " + id);

    }
    protected void stopComponent(){}
    protected void checkHardware() {
        ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.pollAck, mbox).setDetails("Poll Acknowledgement").build());
    }

    protected void processMsg(Msg msg) {
        log.warning("no Message type find in " + this.getClass().getName());
    }

    protected void closeWindow() {
    }
}
