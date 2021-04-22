package AppStarter.Hardware.TouchScreenDisplay;

import AppMain.ATM_SS;
import AppMain.ATM_SS_Starter;
import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.Msg;
import AppStarter.common.Timer;

public class TouchScreenDisplay extends HardwareHandler {
    private final int AccountWaitGap;
    protected String accounts;
    protected int accounts_amount;
    protected int withdrawAmount;
    protected int depositAmount;
    protected int TransferAmount;
    protected String cardID;
    protected String state;
    protected String Transfer_formAccount;
    protected String Transfer_ToAccount;
    protected int ErrorWaitTime;
    protected String error;
    private int AccountWaitTime;
    private final int ErrorWaitTimeGap;
    private boolean fatal;
    /**
     * @param id name of the hardware
     * @param atm_ss_starter
     * Initialize of TouchScreen display
     */
    public TouchScreenDisplay(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.AccountWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("AccountInquiryWaitTime"));
        this.AccountWaitGap = Integer.parseInt(appStarter.getProperties().getProperty("AcoountInquiryTimeGap"));
        this.ErrorWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("ErrorWaitTime"));
        this.ErrorWaitTimeGap = Integer.parseInt(appStarter.getProperties().getProperty("ErrorWaitTimeGap"));
    }


    /**
     * Receive the message and send message due to message type and action
     * @param msg Message Received
     */
    public void processMsg(Msg msg) {
        switch (msg.getType()) {
            case changeScene:
            case LoginSuccess:
                reloadPages(msg.getFilename());
                break;
            case add_key:
                addstar(msg.getkey());
                break;
            case WrongPin:
            case retainCard:
                wrongpin(msg.getDetails());
                break;
            case return_to_main:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.return_to_main, mbox).setAction(msg.getAction()).build());
                break;
            case AccountInquiry:
                //log.info("Receive Account Inquiry Message from "+msg.getId());
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.AccountInquiry, mbox).setAction(msg.getAction()).build());
                break;
            case GetAccount:
                this.accounts = msg.getAccounts();
                this.state = msg.getAction();
                reloadPages(msg.getFilename());
                break;
            case GetAmount:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.GetAmount, mbox).setDetails("Get Account").setAccounts(msg.getAccounts()).build());
                break;
            case showAccountStatus:
                if (msg.getAction().equalsIgnoreCase("Account Inquiry")) {
                    this.accounts_amount = msg.getAccount_Amount();
                    this.accounts = msg.getAccounts();
                    this.state = msg.getAction();
                } else if (msg.getAction().equalsIgnoreCase("withdraw")) {
                    this.accounts_amount = msg.getAccount_Amount();
                    this.accounts = msg.getAccounts();
                    this.withdrawAmount = msg.getOutAmount();
                    this.state = msg.getAction();
                } else if (msg.getAction().equalsIgnoreCase("deposit")) {
                    this.accounts_amount = msg.getAccount_Amount();
                    this.accounts = msg.getAccounts();
                    this.depositAmount = msg.getInAmount();
                    this.state = msg.getAction();
                } else if (msg.getAction().equalsIgnoreCase("afterTransfer")) {
                    this.cardID = msg.getCardnum();
                    this.Transfer_formAccount = msg.getFromAccount();
                    this.Transfer_ToAccount = msg.getOutAccount();
                    this.TransferAmount = msg.getTransferAmount();
                    this.state = msg.getAction();
                }
                reloadPages(msg.getFilename());
                break;
            case Get_All_Information_Account_inquiry:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.Get_All_Information_Account_inquiry, mbox).build());
                break;
            case EndService:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.EndService, mbox).build());
                break;
            case Enter_withdrawal_amount:
                this.accounts = msg.getAccounts();
                reloadPages("EnterAmount.fxml");
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.Enter_withdrawal_amount, mbox).setDetails("Enter Amount").setAccounts(msg.getAccounts()).build());
                break;
            case add_key_Enter_amount:
                addkey(msg.getkey());
                break;
            case withdrawal:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.withdrawal, mbox).setAccounts(msg.getAccounts()).setAccountAmount(msg.getAccount_Amount()).build());
                break;
            case show_withdraw_money:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.show_withdraw_money, mbox).setOutAmount(msg.getOutAmount()).build());
                break;
            case alarm:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.alarm, mbox).setAction(msg.getAction()).build());
                break;
            case withdraw_countdown:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.withdraw_countdown, mbox).build());
                break;
            case print_advice:
                if (msg.getAction().equalsIgnoreCase("withdraw")) {
                    ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setAction(msg.getAction()).
                            setAccounts(msg.getAccounts()).setAccountAmount(msg.getAccount_Amount()).setOutAmount(msg.getOutAmount()).build());
                } else if (msg.getAction().equalsIgnoreCase("deposit")) {
                    ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setAction(msg.getAction()).
                            setAccounts(msg.getAccounts()).setAccountAmount(msg.getAccount_Amount()).setInAmount(msg.getInAmount()).build());
                } else if (msg.getAction().equalsIgnoreCase("afterTransfer")) {
                    ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setAction(msg.getAction()).
                            setCardNum(msg.getCardnum()).setFromAccount(msg.getFromAccount()).
                            setOutAccount(msg.getOutAccount()).setTransferAmount(msg.getTransferAmount()).build());
                }
                break;
            case wait_for_deposit:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.wait_for_deposit, mbox).setAccounts(msg.getAccounts()).build());
                break;
            case send_Timer_Text:
                countdown(msg.getTimer());
                break;
            case choose_TransferTo_account:
                this.Transfer_formAccount = msg.getFromAccount();
                this.state = msg.getAction();
                reloadPages(msg.getFilename());
                break;
            case choose_Transfer_Amount:
                this.Transfer_formAccount = msg.getFromAccount();
                this.Transfer_ToAccount = msg.getOutAccount();

                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.release_key, mbox).setState(true).build());
                reloadPages(msg.getFilename());
                break;
            case add_key_Transfer_amount:
                addTransferAmount(msg.getkey());
                break;
            case SendTransfer:
                sendTransferInformation();
                break;
            case Transfer:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.Transfer, mbox).setFromAccount(msg.getFromAccount()).
                        setOutAccount(msg.getOutAccount()).setTransferAmount(msg.getTransferAmount()).build());
                break;
            case AccountInquiry_countdown:

                Timer.setTimer(id, mbox, AccountWaitGap);
                break;
            case TimesUp:
                if(state!=null) {

                    if (state.equalsIgnoreCase("Account Inquiry")) {
                        AccountWaitTime--;

                        if (AccountWaitTime == 0) {
                            AccountWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("AccountInquiryWaitTime"));
                            ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.restart, mbox).build());
                        } else {
                            Timer.setTimer(id, mbox, AccountWaitGap);
                        }
                    } else {
                        ErrorWaitTime--;
                        System.out.println("Remaining Time: "+ErrorWaitTime);
                        if (ErrorWaitTime == 0) {
                            ErrorWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("ErrorWaitTime"));
                            if(fatal) {
                                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.EndService, mbox).build());
                            }else{
                                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.restart,mbox).build());
                            }
                        } else {
                            Timer.setTimer(id, mbox, ErrorWaitTimeGap);
                            updateErrorTimerText(ErrorWaitTime);
                        }
                    }
                }else{
                    //Take care of error from no state
                    ErrorWaitTime--;
                    System.out.println("Remaining Time: "+ErrorWaitTime);
                    if (ErrorWaitTime == 0) {
                        ErrorWaitTime = Integer.parseInt(appStarter.getProperties().getProperty("ErrorWaitTime"));
                        ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.EndService, mbox).build());
                    } else {
                        Timer.setTimer(id, mbox, ErrorWaitTimeGap);
                        updateErrorTimerText(ErrorWaitTime);
                    }
                }
                break;
            case Delete_Key:
                removeStar();
                break;
            case Reset_key:
                ResetStar();
                break;
            case Delete_key_Enter_amount:
                deleteKey(msg.getAction());
                break;
            case Reset_key_Enter_amount:
                ResetKey(msg.getAction());
                break;
            case error_Timer:
                this.error = msg.getError();
                this.fatal=msg.isFatal();
                reloadPages(msg.getFilename());
                Timer.setTimer(id, mbox, ErrorWaitTimeGap);
                break;
            case Delete_key_Transfer_amount:
                DeleteTransferAmount();
                break;
            case Reset_key_Transfer_amount:
                ResetTransferAmount();
                break;
            case restart:
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.restart,mbox).build());
                break;

        }
    }

    protected void ResetTransferAmount() {
    }

    protected void DeleteTransferAmount() {
    }

    protected void updateErrorTimerText(int errorWaitTime) {
    }

    protected void SetErrorInitial(int ErrorWaitTime, String error) {
    }

    protected void ResetKey(String action) {
    }

    protected void sendTransferInformation() {
    }

    protected void reloadPages(String filename) {

    }

    protected void removeStar() {
    }

    protected void addstar(String key) {

    }

    public void wrongpin(String text) {
    }

    protected void deleteKey(String action) {
    }

    protected void addkey(String key) {
    }

    protected void ResetStar() {
    }

    protected void addTransferAmount(String key) {

    }

    protected void countdown(String text) {
    }
}
