package AppStarter.Hardware.CashWithdraw;

import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.AppStarter;
import AppStarter.common.Msg;
import AppStarter.common.Timer;

public class CashDispenser extends HardwareHandler {

    protected int outAmount;
    private final int withdrawWaitGap;
    private int withdrawWaitSecond;
    protected int minDomainThresHold;

    public CashDispenser(String id, AppStarter appStarter) {
        super(id, appStarter);
        this.withdrawWaitGap = Integer.parseInt(appStarter.getProperties().getProperty("withdrawTimeGap"));
        this.withdrawWaitSecond = Integer.parseInt(appStarter.getProperties().getProperty("withdrawWaitTime"));
        this.minDomainThresHold=Integer.parseInt(appStarter.getProperties().getProperty("minDomainThresHold"));
    }

    public void processMsg(Msg msg) {
        switch (msg.getType()) {
            case show_withdraw_money:
                this.outAmount = msg.getOutAmount();
                reloadPage("withdraw.fxml");
                break;
            case withdraw_countdown:
                System.out.println("withdrawal countdown");
                Timer.setTimer(id, mbox, withdrawWaitGap);
                break;
            case TimesUp:
                System.out.println("Times up");
                withdrawWaitSecond--;

                if (withdrawWaitSecond == 0) {
                    withdrawWaitSecond = Integer.parseInt(appStarter.getProperties().getProperty("withdrawWaitTime"));
                    //ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.EndService, mbox).build());
                    ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.restart,mbox).build());
                } else {
                    Timer.setTimer(id, mbox, withdrawWaitGap);
                    setTime("Time for Retriving cash before close slot: " + withdrawWaitSecond + " seconds");
                }

                break;
            case restart:
                reset();
                break;
        }
    }
    protected void reset() {
    }
    protected void reloadPage(String fxml) {
    }

    protected void setTime(String text) {
    }


}
