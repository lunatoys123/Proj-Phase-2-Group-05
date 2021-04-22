package AppMain;

import AppStarter.BAMS.BAMS_Emulator;
import AppStarter.Hardware.CardReader.CardReader_Emulator;
import AppStarter.Hardware.CashDeposit.CashDepositCollector_emulator;
import AppStarter.Hardware.CashWithdraw.CashDispenser_Emulator;
import AppStarter.Hardware.KeyPad.keypad_Emulator;
import AppStarter.Hardware.TouchScreenDisplay.TouchScreenDisplay_Emulator;
import AppStarter.Hardware.advice.advice_Emulator;
import AppStarter.Hardware.alarm.alarm_Emulator;
import AppStarter.common.CommonUtility;
import AppStarter.common.Timer;
import javafx.application.Application;
import javafx.stage.Stage;

public class ATM_SS_Emulator extends ATM_SS_Starter {

    public static void main(String[] args) {
        new ATM_SS_Emulator().startApp();
    }

    @Override
    protected void StartAllThread() {
        Emulator.atmSsEmulator = this;
        new Emulator().start();
    }

    private void setTimer(Timer timer) {
        this.timer = timer;
    }

    private void setATM_SS(ATM_SS atm_ss) {
        this.atm_ss = atm_ss;
    }

    private void setCardReader(CardReader_Emulator cardReader) {
        this.CR_Emulator = cardReader;
    }

    private void setkeypad(keypad_Emulator keypad_emulator) {
        this.keypad_emulator = keypad_emulator;
    }

    private void setTouchScreenDisplay(TouchScreenDisplay_Emulator touchScreenDisplay_emulator) {
        this.touchScreenDisplay_emulator = touchScreenDisplay_emulator;
    }

    private void setBAMS(BAMS_Emulator bams_emulator) {
        this.bams_emulator = bams_emulator;
    }

    private void setAdvicePrinter(advice_Emulator advice) {
        this.advice_emulator = advice;
    }

    private void setAlarm(alarm_Emulator alarm_emulator) {
        this.alarm_emulator = alarm_emulator;
    }

    private void setCashDispenser(CashDispenser_Emulator cashDispenser_emulator) {
        this.cashDispenser_emulator = cashDispenser_emulator;
    }

    private void setCashDeposit(CashDepositCollector_emulator cashDepositCollector_emulator) {
        this.cashDepositCollector_emulator = cashDepositCollector_emulator;
    }

    public static class Emulator extends Application {
        public static ATM_SS_Emulator atmSsEmulator;

        CardReader_Emulator cardReader = null;
        keypad_Emulator keypad_emulator = null;
        TouchScreenDisplay_Emulator touchScreenDisplay_emulator = null;
        BAMS_Emulator bams_emulator = null;
        advice_Emulator advice_emulator = null;
        alarm_Emulator alarm_emulator = null;
        CashDispenser_Emulator cashDispenser_emulator = null;
        CashDepositCollector_emulator cashDepositCollector_emulator = null;
        CommonUtility common = null;
        ATM_SS atm_ss = null;
        Timer timer = null;


        public void start() {
            launch();
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
            try {
                common = new CommonUtility(atmSsEmulator);
                timer = new Timer("Timer", atmSsEmulator);
                atm_ss = new ATM_SS("ATM_SS", atmSsEmulator,common);
                cardReader = new CardReader_Emulator("Card_reader", atmSsEmulator);
                keypad_emulator = new keypad_Emulator("Keypad", atmSsEmulator);
                touchScreenDisplay_emulator = new TouchScreenDisplay_Emulator("TouchScreen", atmSsEmulator);
                bams_emulator = new BAMS_Emulator("BAMS", atmSsEmulator);
                advice_emulator = new advice_Emulator("advicePrinter", atmSsEmulator);
                alarm_emulator = new alarm_Emulator("alarm", atmSsEmulator);
                cashDispenser_emulator = new CashDispenser_Emulator("cashDispenser", atmSsEmulator, common);
                cashDepositCollector_emulator = new CashDepositCollector_emulator("CashDepositCollector", atmSsEmulator, common);

                cardReader.start();
                keypad_emulator.start();
                touchScreenDisplay_emulator.start();
                bams_emulator.start();
                advice_emulator.start();
                alarm_emulator.start();
                cashDispenser_emulator.start();
                cashDepositCollector_emulator.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Initialize failed on Emulator");
            }

            atmSsEmulator.setCardReader(cardReader);
            atmSsEmulator.setATM_SS(atm_ss);
            atmSsEmulator.setTimer(timer);
            atmSsEmulator.setkeypad(keypad_emulator);
            atmSsEmulator.setTouchScreenDisplay(touchScreenDisplay_emulator);
            atmSsEmulator.setBAMS(bams_emulator);
            atmSsEmulator.setAdvicePrinter(advice_emulator);
            atmSsEmulator.setAlarm(alarm_emulator);
            atmSsEmulator.setCashDispenser(cashDispenser_emulator);
            atmSsEmulator.setCashDeposit(cashDepositCollector_emulator);

            new Thread(timer).start();
            new Thread(atm_ss).start();
            new Thread(cardReader).start();
            new Thread(keypad_emulator).start();
            new Thread(touchScreenDisplay_emulator).start();
            new Thread(bams_emulator).start();
            new Thread(advice_emulator).start();
            new Thread(alarm_emulator).start();
            new Thread(cashDispenser_emulator).start();
            new Thread(cashDepositCollector_emulator).start();
        }
    }
}
