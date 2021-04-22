package AppStarter.Hardware.KeyPad;

import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.AppStarter;
import AppStarter.common.Msg;

public class Keypad extends HardwareHandler {


    public Keypad(String id, AppStarter appStarter) {
        super(id, appStarter);
    }

    @Override
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case keyPressed:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.keyPressed, mbox).setKey(msg.getkey()).build());
                log.info("send keyPressed to ATM-SS");
                break;
            case Enter:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.Enter, mbox).setDetails("Press Enter").build());
                break;
            case release_key:
                setState(msg.isState());
                break;
            case Delete:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.Delete,mbox).build());
                break;
            case Reset_key:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.Reset_key,mbox).build());
                break;
        }
    }

    protected void setState(boolean state) {
    }

    protected void closeWindow() {

    }
}
