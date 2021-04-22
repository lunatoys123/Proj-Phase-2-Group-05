package AppMain;

import AppStarter.common.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATM_SS extends AppThread {
    /**
     * Time of sending Polling Message
     */
    private final int pollingTime;
    private final ATM_SS_Starter atm_ss_starter;
    private final CommonUtility common;
    /**
     * Message box of cardReader
     */
    protected MBox cardBox;
    /**
     * Message box of keypad
     */
    protected MBox keypadBox;
    /**
     * Message box of touchscreen display
     */
    protected MBox touchScreenBox;
    /**
     * Message box of bams
     */
    protected MBox bamsBox;
    /**
     * Message box of Timer
     */
    protected MBox timerBox;
    /**
     * Message box of advicce
     */
    protected MBox adviceBox;

    /**
     * Message box of alarm
     */
    protected MBox alarmBox;
    /**
     * Message box for cashDispenser
     */
    protected MBox cashDispenserBox;
    /**
     * Message box for cashDepositCollector
     */
    protected MBox cashDepositBox;
    /**
     * receive key press
     */
    List<String> Buffer;
    /**
     * stage of ATM-SS
     */
    private StageATM_SS stageATM_ss;
    private boolean startPolling;
    private final boolean debug;
    private final Map<String, Boolean> pollingMap;
    private int pollingCounter;

    /**
     * @param id id of the ATM-SS
     * @param appStarter Class of Handle Thread
     * @param common CommonUtility class
     */
    public ATM_SS(String id, AppStarter appStarter, CommonUtility common) {
        super(id, appStarter);
        this.pollingTime = Integer.parseInt(appStarter.getProperties().getProperty("pollingTime"));
        Buffer = new ArrayList<>();
        stageATM_ss = StageATM_SS.initial;
        this.atm_ss_starter = (ATM_SS_Starter) appStarter;
        this.common = common;
        this.startPolling = false;
        this.pollingMap = new HashMap<>();
        this.debug = false;
        this.pollingCounter = 0;
    }

    /**
     *  Thread action, control of Receive and sand message
     *
     */
    @Override
    public void run() {
        cardBox = appStarter.getThread("Card_reader").getMbox();
        keypadBox = appStarter.getThread("Keypad").getMbox();
        touchScreenBox = appStarter.getThread("TouchScreen").getMbox();
        bamsBox = appStarter.getThread("BAMS").getMbox();
        timerBox = appStarter.getThread("Timer").getMbox();
        adviceBox = appStarter.getThread("advicePrinter").getMbox();
        alarmBox = appStarter.getThread("alarm").getMbox();
        cashDispenserBox = appStarter.getThread("cashDispenser").getMbox();
        cashDepositBox = appStarter.getThread("CashDepositCollector").getMbox();


        Timer.setTimer(id, mbox, pollingTime);
        //log.info(id+" Starting");

        boolean quit = false;
        while (!quit) {
            Msg msg = mbox.Receive();
            //log.info("Test Message: "+msg);

            switch (msg.getType()) {
                case TimesUp:
                    if (startPolling == false) {
                        Timer.setTimer(id, mbox, pollingTime);
                        cardBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        keypadBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        bamsBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        alarmBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        adviceBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        cashDispenserBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        cashDepositBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                        ResetMap(pollingMap);
                    } else {
                        if (debug) pollingCounter++;
                        System.out.println("pollingCounter: " + pollingCounter);
                        System.out.println("start polling: " + startPolling);
                        if (pollingCounter == 3) {
                            //print(pollingMap);
                            Timer.setTimer(id, mbox, pollingTime);
                            cardBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            keypadBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            touchScreenBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            bamsBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            alarmBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            adviceBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            cashDispenserBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            ResetMap(pollingMap);
                        } else if (NoPollingMessageMiss(pollingMap)) {
                            //print(pollingMap);
                            Timer.setTimer(id, mbox, pollingTime);
                            cardBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            keypadBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            touchScreenBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            bamsBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            alarmBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            adviceBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            cashDispenserBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());
                            cashDepositBox.send(new Msg.Builder(id, Msg.Type.poll, mbox).setDetails("Poll Message").build());

                            ResetMap(pollingMap);
                        } else {
                            getMbox().send(new Msg.Builder(id, Msg.Type.Error, mbox).
                                    setError("Not all Hardware response to poll Message").setFilename("Error.fxml").build());
                        }

                    }
                    //send poll message to all message box when time arrived
                    break;
                case pollAck:
                    startPolling = true;
                    pollingMap.put(msg.getSender().getId(), true);
                    log.info("Receive poll ack from " + msg.getSender().getId());
                    break;
                case password_validation:
                    log.info("Receive check card message from " + msg.getId() + " card Number: [" + msg.getCardnum() + "]");
                    stageATM_ss = StageATM_SS.login;
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.changeScene, mbox).setFilename("EnterPassword.fxml").build());
                    bamsBox.send(new Msg.Builder(id, Msg.Type.cardMessage, mbox).setDetails("Card ID").setCardNum(msg.getCardnum()).build());
                    keypadBox.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(true).build());
                    break;
                case keyPressed:
                    log.info("Receive key Pressed: " + msg.getkey());
                    if (stageATM_ss == StageATM_SS.login) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.add_key, mbox).setDetails("add star").setKey(msg.getkey()).build());
                    } else if (stageATM_ss == StageATM_SS.withdrawal) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.add_key_Enter_amount, mbox).setDetails("add_key").setKey(msg.getkey()).build());
                    } else if (stageATM_ss == StageATM_SS.Transfer) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.add_key_Transfer_amount, mbox).setDetails("add_key_Transfer_amount").setKey(msg.getkey()).build());
                    }
                    Buffer.add(msg.getkey());
                    break;
                case Enter:

                    String content = "";
                    for (String s : Buffer) {
                        content += s;
                    }
                    log.info(content);

                    if (stageATM_ss == StageATM_SS.login) {
                        bamsBox.send(new Msg.Builder(id, Msg.Type.cardMessage, mbox).setDetails("pin").setPin(content).build());
                        keypadBox.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(false).build());
                    } else if (stageATM_ss == StageATM_SS.withdrawal) {
                        //System.out.println("Send withdraw message to BAMS");
                        keypadBox.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(false).build());
                    } else if (stageATM_ss == StageATM_SS.Transfer) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.SendTransfer, mbox).build());
                        keypadBox.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(false).build());
                    }

                    Buffer = new ArrayList<>();
                    break;
                case Delete:
                    if (Buffer.size() > 0) {
                        if(Buffer.get(Buffer.size() - 1).length() > 1){
                            Buffer.set(Buffer.size() - 1, Buffer.get(Buffer.size() - 1).substring(0,1));
                        }else {
                            Buffer.remove(Buffer.size() - 1);
                        }
                        String content2 = "";
                        for (String s : Buffer) {
                            content2 += s;
                        }
                        log.info(content2);

                    }
                    if (stageATM_ss == StageATM_SS.login) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.Delete_Key, mbox).setDetails("Delete star").build());
                    } else if (stageATM_ss == StageATM_SS.withdrawal) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.Delete_key_Enter_amount, mbox).setDetails("Delete key").setAction("withdraw").build());
                    } else if(stageATM_ss == StageATM_SS.Transfer){
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.Delete_key_Transfer_amount, mbox).build());
                    }
                    break;
                case Reset_key:
                    Buffer.clear();
                    String content2 = "";
                    for (String s : Buffer) {
                        content2 += s;
                    }
                    log.info(content2);

                    if (stageATM_ss == StageATM_SS.login) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.Reset_key, mbox).build());
                    } else if (stageATM_ss == StageATM_SS.withdrawal) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.Reset_key_Enter_amount, mbox).setAction("withdraw").build());
                    } else if(stageATM_ss == StageATM_SS.Transfer){
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.Reset_key_Transfer_amount, mbox).build());
                    }
                    break;
                case return_to_main:
                case LoginSuccess:
                    log.info("Receive Login success");
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.LoginSuccess, mbox).setDetails("LoginSuccess").setFilename("TouchScreenDisplayMainPage.fxml").build());
                    break;
                case WrongPin:
                    log.info("Receive wrong pin");
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.WrongPin, mbox).setDetails(msg.getDetails()).build());
                    keypadBox.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(true).build());
                    break;
                case Terminate:
                    quit = true;
                    break;
                case retainCard:
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.retainCard, mbox).setDetails("Card has been retain").build());
                    cardBox.send(new Msg.Builder(id, Msg.Type.retainCard, mbox).setDetails("Card has been retain").build());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Terminate();
                    quit = true;
                    break;
                case AccountInquiry:
                    if (msg.getAction().equalsIgnoreCase("Account Inquiry")) {
                        stageATM_ss = StageATM_SS.AccountInquiry;
                    } else if (msg.getAction().equalsIgnoreCase("withdraw")) {
                        stageATM_ss = StageATM_SS.withdrawal;
                    } else if (msg.getAction().equalsIgnoreCase("deposit")) {
                        stageATM_ss = StageATM_SS.deposit;
                    } else if (msg.getAction().equalsIgnoreCase("fromTransfer")) {
                        stageATM_ss = StageATM_SS.Transfer;
                    }
                    bamsBox.send(new Msg.Builder(id, Msg.Type.AccountInquiry, mbox).build());
                    break;
                case GetAccount:
                    log.info("Get Account message received");
                    if (stageATM_ss == StageATM_SS.AccountInquiry) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.GetAccount, mbox).setFilename("GetAccount.fxml").setAccounts(msg.getAccounts()).setAction("Account Inquiry").build());
                    } else if (stageATM_ss == StageATM_SS.withdrawal) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.GetAccount, mbox).setFilename("GetAccount.fxml").setAccounts(msg.getAccounts()).setAction("withdraw").build());
                    } else if (stageATM_ss == StageATM_SS.deposit) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.GetAccount, mbox).setFilename("GetAccount.fxml").setAccounts(msg.getAccounts()).setAction("deposit").build());
                    } else if (stageATM_ss == StageATM_SS.Transfer) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.GetAccount, mbox).setFilename("GetAccount.fxml").setAccounts(msg.getAccounts()).setAction("fromTransfer").build());
                    }
                    break;
                case GetAmount:
                    bamsBox.send(new Msg.Builder(id, Msg.Type.GetAmount, mbox).setDetails("Get Amount").setAccounts(msg.getAccounts()).build());
                    break;
                case showAccountStatus:
                    if (stageATM_ss == StageATM_SS.AccountInquiry) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.showAccountStatus, mbox).
                                setAccountAmount(msg.getAccount_Amount()).setAccounts(msg.getAccounts()).setFilename("Account_status.fxml").setAction("Account Inquiry").build());
                    } else if (stageATM_ss == StageATM_SS.withdrawal) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.showAccountStatus, mbox).setAccountAmount(msg.getAccount_Amount()).
                                setAccounts(msg.getAccounts()).setOutAmount(msg.getOutAmount()).setFilename("Account_status_for_transfer.fxml").setAction("withdraw").build());
                    } else if (stageATM_ss == StageATM_SS.deposit) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.showAccountStatus, mbox).setAccountAmount(msg.getAccount_Amount()).
                                setAccounts(msg.getAccounts()).setCardNum(msg.getCardnum()).setInAmount(msg.getInAmount()).
                                setAction("deposit").setFilename("Account_status_for_transfer.fxml").build());
                    } else if (stageATM_ss == StageATM_SS.Transfer) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.showAccountStatus, mbox).setCardNum(msg.getCardnum()).
                                setFromAccount(msg.getFromAccount()).setOutAccount(msg.getOutAccount()).setTransferAmount(msg.getTransferAmount()).
                                setAction("afterTransfer").setFilename("Account_status_for_transfer.fxml").build());
                    }
                    break;
                case Get_All_Information_Account_inquiry:
                    bamsBox.send(new Msg.Builder(id, Msg.Type.Get_All_Information_Account_inquiry, mbox).build());
                    break;
                case print_advice:

                    if (stageATM_ss == StageATM_SS.AccountInquiry) {
                        adviceBox.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setAction("Account Inquiry").setCardNum(msg.getCardnum()).setAccounts(msg.getAccounts()).setAccountAmount(msg.getAccount_Amount()).build());
                        alarmBox.send(new Msg.Builder(id, Msg.Type.alarm, mbox).setDetails("Alarming from " + msg.getId() + ":").setAction("Account Inquiry").build());
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.AccountInquiry_countdown, mbox).build());
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.changeScene, mbox).setFilename("Thankyou.fxml").build());
                    } else if (stageATM_ss == StageATM_SS.withdrawal) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.changeScene, mbox).setFilename("Thankyou.fxml").build());
                        adviceBox.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setAccounts(msg.getAccounts()).setAccountAmount(msg.getAccount_Amount()).setOutAmount(msg.getOutAmount()).setAction(msg.getAction()).build());
                    } else if (stageATM_ss == StageATM_SS.deposit) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.changeScene, mbox).setFilename("Thankyou.fxml").build());
                        adviceBox.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setAccounts(msg.getAccounts()).setAccountAmount(msg.getAccount_Amount()).setInAmount(msg.getInAmount()).setAction(msg.getAction()).build());
                    } else if (stageATM_ss == StageATM_SS.Transfer) {
                        touchScreenBox.send(new Msg.Builder(id, Msg.Type.changeScene, mbox).setFilename("Thankyou.fxml").build());
                        adviceBox.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setFromAccount(msg.getFromAccount()).
                                setOutAccount(msg.getOutAccount()).setCardNum(msg.getCardnum()).setTransferAmount(msg.getTransferAmount()).
                                setAction(msg.getAction()).build());
                    }
                    break;
                case EndService:
                case ejectCard:
                    cardBox.send(new Msg.Builder(id, Msg.Type.ejectCard, mbox).setDetails("eject card").setCardNum(msg.getCardnum()).build());
                    stopComponent();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Terminate();
                    break;
                case withdrawal:
                    if (msg.getAccount_Amount() > common.Total()) {
                        getMbox().send(new Msg.Builder(id, Msg.Type.Error, mbox).setError("withdraw money exceed the total of each domain combine").
                                setFilename("Error.fxml").build());
                    } else {
                        bamsBox.send(new Msg.Builder(id, Msg.Type.withdrawal, mbox).setAccounts(msg.getAccounts()).setAccountAmount(msg.getAccount_Amount()).build());
                    }
                    break;
                case Enter_withdrawal_amount:
                    keypadBox.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(true).build());
                    break;
                case show_withdraw_money:
                    cashDispenserBox.send(new Msg.Builder(id, Msg.Type.show_withdraw_money, mbox).setOutAmount(msg.getOutAmount()).build());
                    break;
                case alarm:
                    alarmBox.send(new Msg.Builder(id, Msg.Type.alarm, mbox).setAction(msg.getAction()).setDetails("Alarming from " + msg.getId() + ":").build());
                    break;
                case withdraw_countdown:
                    cashDispenserBox.send(new Msg.Builder(id, Msg.Type.withdraw_countdown, mbox).build());
                    break;
                case wait_for_deposit:
                    cashDepositBox.send(new Msg.Builder(id, Msg.Type.wait_for_deposit, mbox).setAccounts(msg.getAccounts()).build());
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.changeScene, mbox).setFilename("waitForDeposit.fxml").build());
                    break;
                case send_Timer_Text:
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.send_Timer_Text, mbox).setTimer(msg.getTimer()).build());
                    break;
                case deposit:
                    bamsBox.send(new Msg.Builder(id, Msg.Type.deposit, mbox).setAccounts(msg.getAccounts()).setInAmount(msg.getInAmount()).build());
                    break;
                case release_key:
                    keypadBox.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(msg.getState()).build());
                    break;
                case Transfer:
                    bamsBox.send(new Msg.Builder(id, Msg.Type.Transfer, mbox).setFromAccount(msg.getFromAccount())
                            .setOutAccount(msg.getOutAccount()).setTransferAmount(msg.getTransferAmount()).build());
                    break;
                case Error:
                    //cardBox.send(new Msg.Builder(id, Msg.Type.ejectCard, mbox).setDetails("eject card").setCardNum(msg.getCardnum()).build());
                    /*try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.error_Timer, mbox).setFilename(msg.getFilename()).setError(msg.getError()).setFatal(msg.isFatal()).build());
                    stopComponent();
                    break;
                case restart:
                    cardBox.send(new Msg.Builder(id, Msg.Type.restart,mbox).build());
                    bamsBox.send(new Msg.Builder(id,Msg.Type.restart,mbox).build());
                    cashDispenserBox.send(new Msg.Builder(id, Msg.Type.restart,mbox).build());
                    cashDepositBox.send(new Msg.Builder(id,Msg.Type.restart,mbox).build());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cardBox.send(new Msg.Builder(id, Msg.Type.ejectCard, mbox).setDetails("eject card\n").build());
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.changeScene,mbox).setFilename("WelcomePage.fxml").build());
                    stageATM_ss=StageATM_SS.initial;
                    break;
                case DepositProblem:
                    touchScreenBox.send(new Msg.Builder(id, Msg.Type.error_Timer,mbox).setError(msg.getError()).setFatal(msg.isFatal()).setFilename("Error.fxml").build());

                    break;

            }
        }

        appStarter.UnRegisterThread(this);
        log.info("UnRegister Thread : " + id);
    }

    public void stopComponent(){
        adviceBox.send(new Msg.Builder(id, Msg.Type.stop_component, mbox).build());
        alarmBox.send(new Msg.Builder(id, Msg.Type.stop_component, mbox).build());
        cardBox.send(new Msg.Builder(id, Msg.Type.stop_component, mbox).build());
        cashDispenserBox.send(new Msg.Builder(id, Msg.Type.stop_component, mbox).build());
        cashDepositBox.send(new Msg.Builder(id, Msg.Type.stop_component, mbox).build());
        keypadBox.send(new Msg.Builder(id, Msg.Type.stop_component, mbox).build());
    }

    private void print(Map<String, Boolean> pollingMap) {

        for (Map.Entry<String, Boolean> key : pollingMap.entrySet()) {
            System.out.println(key.getKey() + " " + key.getValue());
        }
    }

    private boolean NoPollingMessageMiss(Map<String, Boolean> pollingMap) {
        for (Map.Entry<String, Boolean> key : pollingMap.entrySet()) {
            if (key.getValue() == false) return false;
        }
        return true;
    }

    private void ResetMap(Map<String, Boolean> pollingMap) {
        pollingMap.put(cardBox.getId(), false);
        pollingMap.put(keypadBox.getId(), false);
        pollingMap.put(touchScreenBox.getId(), false);
        pollingMap.put(bamsBox.getId(), false);
        pollingMap.put(alarmBox.getId(), false);
        pollingMap.put(adviceBox.getId(), false);
        pollingMap.put(cashDispenserBox.getId(), false);
        pollingMap.put(cashDepositBox.getId(), false);
    }

    /**
     *  Terminate all Thread
     *
     */
    public void Terminate() {
        mbox.send(new Msg.Builder(mbox.getId(), Msg.Type.Terminate, mbox).setDetails("Terminate now").build());
        timerBox.send(new Msg.Builder(timerBox.getId(), Msg.Type.Terminate, timerBox).setDetails("Terminate now").build());
        cardBox.send(new Msg.Builder(cardBox.getId(), Msg.Type.Terminate, cardBox).setDetails("Terminate now").build());
        keypadBox.send(new Msg.Builder(keypadBox.getId(), Msg.Type.Terminate, keypadBox).setDetails("Terminate now").build());
        touchScreenBox.send(new Msg.Builder(touchScreenBox.getId(), Msg.Type.Terminate, touchScreenBox).setDetails("Terminate now").build());
        bamsBox.send(new Msg.Builder(bamsBox.getId(), Msg.Type.Terminate, bamsBox).setDetails("Terminate now").build());
        adviceBox.send(new Msg.Builder(adviceBox.getId(), Msg.Type.Terminate, adviceBox).setDetails("Terminate now").build());
        alarmBox.send(new Msg.Builder(alarmBox.getId(), Msg.Type.Terminate, alarmBox).setDetails("Terminate now").build());
        cashDispenserBox.send(new Msg.Builder(cardBox.getId(), Msg.Type.Terminate, cashDispenserBox).setDetails("Terminate now").build());
        cashDepositBox.send(new Msg.Builder(cashDepositBox.getId(), Msg.Type.Terminate, cashDepositBox).setDetails("Terminate now").build());
    }

    public String stageName() {
        return stageATM_ss.name();
    }

    public enum StageATM_SS {
        initial,
        login,
        AccountInquiry,
        withdrawal,
        deposit,
        Transfer
    }
}
