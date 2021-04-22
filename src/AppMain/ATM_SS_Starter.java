package AppMain;

import AppStarter.BAMS.BAMS_Emulator;
import AppStarter.Hardware.CardReader.CardReader_Emulator;
import AppStarter.Hardware.CashDeposit.CashDepositCollector_emulator;
import AppStarter.Hardware.CashWithdraw.CashDispenser_Emulator;
import AppStarter.Hardware.KeyPad.keypad_Emulator;
import AppStarter.Hardware.TouchScreenDisplay.TouchScreenDisplay_Emulator;
import AppStarter.Hardware.advice.advice_Emulator;
import AppStarter.Hardware.alarm.alarm_Emulator;
import AppStarter.common.AppStarter;
import AppStarter.common.CommonUtility;
import AppStarter.common.Msg;
import AppStarter.common.Timer;

public class ATM_SS_Starter extends AppStarter {

    protected ATM_SS atm_ss;
    protected Timer timer;
    protected CardReader_Emulator CR_Emulator;
    protected keypad_Emulator keypad_emulator;
    protected TouchScreenDisplay_Emulator touchScreenDisplay_emulator;
    protected BAMS_Emulator bams_emulator;
    protected advice_Emulator advice_emulator;
    protected alarm_Emulator alarm_emulator;
    protected CashDispenser_Emulator cashDispenser_emulator;
    protected CashDepositCollector_emulator cashDepositCollector_emulator;
    protected CommonUtility common;

    public ATM_SS_Starter() {
        super("ATM_SS", "config/configuration.cfg");

    }

    public static void main(String[] args) {
        new ATM_SS_Starter().startApp();
    }

    protected void startApp() {
        log.info("======================");
        log.info("Start the Application");
        log.info("======================");

        StartAllThread();
    }

    protected void StartAllThread() {
        try {
            common = new CommonUtility(this);
            timer = new Timer("Timer", this);
            atm_ss = new ATM_SS("ATM_SS", this,common);
            CR_Emulator = new CardReader_Emulator("Card_reader", this);
            keypad_emulator = new keypad_Emulator("Keypad", this);
            touchScreenDisplay_emulator = new TouchScreenDisplay_Emulator("TouchScreen", this);
            bams_emulator = new BAMS_Emulator("BAMS", this);
            advice_emulator = new advice_Emulator("advicePrinter", this);
            alarm_emulator = new alarm_Emulator("alarm", this);
            cashDispenser_emulator = new CashDispenser_Emulator("cashDispenser", this, common);
            cashDepositCollector_emulator = new CashDepositCollector_emulator("CashDepositCollector", this, common);
        } catch (Exception e) {
            System.out.println("Initialize class not successful");
            e.printStackTrace();
        }

        new Thread(timer).start();
        new Thread(atm_ss).start();
        new Thread(CR_Emulator).start();
        new Thread(keypad_emulator).start();
        new Thread(touchScreenDisplay_emulator).start();
        new Thread(bams_emulator).start();
        new Thread(advice_emulator).start();
        new Thread(alarm_emulator).start();
        new Thread(cashDispenser_emulator).start();
        new Thread(cashDepositCollector_emulator).start();
    }

    public void stopApp() {
        atm_ss.getMbox().send(new Msg.Builder(atm_ss.getId(), Msg.Type.Terminate, atm_ss.getMbox()).setDetails("Terminate now").build());
        timer.getMbox().send(new Msg.Builder(timer.getId(), Msg.Type.Terminate, timer.getMbox()).setDetails("Terminate now").build());
        CR_Emulator.getMbox().send(new Msg.Builder(CR_Emulator.getId(), Msg.Type.Terminate, CR_Emulator.getMbox()).setDetails("Terminate now").build());
        keypad_emulator.getMbox().send(new Msg.Builder(keypad_emulator.getId(), Msg.Type.Terminate, keypad_emulator.getMbox()).setDetails("Terminate now").build());
        touchScreenDisplay_emulator.getMbox().send(new Msg.Builder(touchScreenDisplay_emulator.getId(), Msg.Type.Terminate, touchScreenDisplay_emulator.getMbox()).setDetails("Terminate now").build());
        bams_emulator.getMbox().send(new Msg.Builder(bams_emulator.getId(), Msg.Type.Terminate, bams_emulator.getMbox()).setDetails("Terminate now").build());
        advice_emulator.getMbox().send(new Msg.Builder(advice_emulator.getId(), Msg.Type.Terminate, advice_emulator.getMbox()).setDetails("Terminate now").build());
        alarm_emulator.getMbox().send(new Msg.Builder(alarm_emulator.getId(), Msg.Type.Terminate, alarm_emulator.getMbox()).setDetails("Terminate now").build());
        cashDispenser_emulator.getMbox().send(new Msg.Builder(cashDispenser_emulator.getId(), Msg.Type.Terminate, cashDispenser_emulator.getMbox()).setDetails("Terminate now").build());
        cashDepositCollector_emulator.getMbox().send(new Msg.Builder(cashDepositCollector_emulator.getId(), Msg.Type.Terminate, cashDepositCollector_emulator.getMbox()).setDetails("Terminate now").build());
    }

}
