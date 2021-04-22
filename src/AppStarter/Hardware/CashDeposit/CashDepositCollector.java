package AppStarter.Hardware.CashDeposit;

import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.AppStarter;
import AppStarter.common.Msg;
import AppStarter.common.Timer;

public class CashDepositCollector extends HardwareHandler {
    protected int depositWaitGap;
    protected int depositWaitTime;
    protected boolean confirmAmount = false;
    private int timerID;


    public CashDepositCollector(String id, AppStarter appStarter) {
        super(id, appStarter);
        this.depositWaitGap = Integer.parseInt(appStarter.getProperties().getProperty("depositTimeGap"));
        this.depositWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("DepositaryWaitTime"));
    }

    public void processMsg(Msg msg) {
        switch (msg.getType()) {
            case wait_for_deposit:
                confirmAmount = false;
                this.timerID = Timer.setTimer(id, mbox, depositWaitTime);
                this.depositWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("DepositaryWaitTime"));
                initial(msg.getAccounts());
                break;
            case TimesUp:
                depositWaitTime--;

                if (!confirmAmount) {
                    if (depositWaitTime == 0) {
                        this.depositWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("DepositaryWaitTime"));
                        ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.DepositProblem, mbox).setError("Hardware failure:　no response on deposit").setFatal(false).build());
                    } else {
                        this.timerID = Timer.setTimer(id, mbox, depositWaitGap);
                        ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.send_Timer_Text, mbox).setTimer(depositWaitTime + "").build());
                    }
                }
                break;
            case deposit:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.deposit, mbox).setAccounts(msg.getAccounts()).setInAmount(msg.getInAmount()).build());
                break;
            case restart:
                reset();
                break;
        }
    }
    protected void reset() {
    }
    protected void initial(String account) {

    }

}
