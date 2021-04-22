package AppStarter.Hardware.CardReader;


import AppMain.ATM_SS_Starter;
import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.AppStarter;
import AppStarter.common.Msg;

public class CardReader extends HardwareHandler {
    protected boolean hasCard=false;
    protected boolean ejectCard=false;
    public CardReader(String id, AppStarter appStarter){
        super(id,appStarter);

    }

    @Override
    protected void processMsg(Msg msg) {
        switch (msg.getType()){
            case insertCard:
                ATMSS_BOX.send(new Msg.Builder(id,Msg.Type.password_validation,mbox).setDetails("Require Checking Card").setCardNum(msg.getCardnum()).build());
                hasCard=true;
                disableButton();
                break;
            case retainCard:
            case ejectCard:
                addMessage(msg.getDetails());
                break;
            case restart:
                hasCard=false;
                ejectCard=false;
                restart();
                break;

        }
    }

    protected void restart() {
    }

    protected void addMessage(String text){ }
    protected void disableButton(){}
}
