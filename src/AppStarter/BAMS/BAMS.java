package AppStarter.BAMS;

import ATMSS.BAMSHandler.BAMSHandler;
import ATMSS.BAMSHandler.BAMSInvalidReplyException;
import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.AppStarter;
import AppStarter.common.Msg;

import java.io.IOException;

public class BAMS extends HardwareHandler {
    private static final String urlPrefix = "http://cslinux0.comp.hkbu.edu.hk/comp4107_20-21_grp05/";
    private final int totalWrongpin = 3;
    private BAMSHandler bamsHandler;
    private String card_ID;
    private String account;
    private String Transfer_FromAccount;
    private String Transfer_ToAccount;
    private String pin;
    private int WrongPinCounter = 0;

    public BAMS(String id, AppStarter appStarter) {
        super(id, appStarter);
    }

    public void processMsg(Msg msg) {
        switch (msg.getType()) {
            case cardMessage:
                HandleCardMessage(msg);
                break;
            case AccountInquiry:
                HandleAccountInquiry(msg);
                break;
            case GetAmount:
                this.account = msg.getAccounts();
                HandleAccountAmout(msg);
                break;
            case Get_All_Information_Account_inquiry:
                GetAllAccountInformation();
                break;
            case withdrawal:
                this.account = msg.getAccounts();
                GetWithdrawAcmout(msg.getAccount_Amount());
                break;
            case deposit:
                this.account = msg.getAccounts();
                GetDepositAmount(msg.getInAmount());
                break;
            case Transfer:
                this.Transfer_FromAccount=msg.getFromAccount();
                this.Transfer_ToAccount=msg.getOutAccount();

                Transfer(Transfer_FromAccount,Transfer_ToAccount,msg.getTransferAmount());

                break;
            case restart:
                WrongPinCounter=0;
                card_ID=null;
                account=null;
                Transfer_FromAccount=null;
                Transfer_ToAccount=null;
                pin=null;
                break;
        }
    }

    /**
     * @param transfer_fromAccount Account of Transfer Account
     * @param transfer_toAccount Account Transfer money to
     * @param transferAmount TransferAmount
     * Handle Transfer Action
     */
    private void Transfer(String transfer_fromAccount, String transfer_toAccount, int transferAmount) {

        if(card_ID!=null){
            if(bamsHandler==null){
                bamsHandler=new BAMSHandler(urlPrefix,log);
            }

            try {
                double transfer_Amount=bamsHandler.transfer(card_ID,"cred",transfer_fromAccount,transfer_toAccount,transferAmount+"");
                System.out.println(transfer_Amount);
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.showAccountStatus,mbox).setCardNum(this.card_ID).setFromAccount(transfer_fromAccount).
                        setOutAccount(transfer_toAccount).setTransferAmount(transferAmount).build());
            } catch (BAMSInvalidReplyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param account_amount account of Deposit
     * Handle Deposit Action
     */
    private void GetDepositAmount(int account_amount) {

        System.out.println("Receive information of Deposit");
        System.out.println(account_amount);
        if (card_ID != null && account != null) {

            if (bamsHandler == null) {
                bamsHandler = new BAMSHandler(urlPrefix, log);
            }

            try {
                double deposit = bamsHandler.deposit(card_ID, account, "cred", account_amount + "");
                System.out.println(deposit);

                double enquiryAmount = bamsHandler.enquiry(this.card_ID, account, "cred");
                System.out.println(enquiryAmount);

                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.showAccountStatus, mbox).setCardNum(card_ID).setAccounts(account).setCardNum(card_ID).
                        setAccountAmount((int) enquiryAmount).setInAmount((int) deposit).build());

            } catch (BAMSInvalidReplyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param amount withdrawal amount
     * Handle withdraw action
     */
    private void GetWithdrawAcmout(int amount) {
        System.out.println("Receive information of withdraw");
        System.out.println(card_ID);
        System.out.println(account);
        if (card_ID != null && account != null) {
            if (bamsHandler == null) {
                bamsHandler = new BAMSHandler(urlPrefix, log);
            }

            try {
                double outAmount = bamsHandler.withdraw(card_ID, account, "cred", amount + "");
                System.out.println(outAmount);

                if(outAmount==-1){
                    ATMSS_BOX.send(new Msg.Builder(id,Msg.Type.Error,mbox).setError("withdrawal amount exceed Account Amount").setFilename("Error.fxml").build());
                }else {

                    double enquiryAmount = bamsHandler.enquiry(this.card_ID, account, "cred");
                    System.out.println(enquiryAmount);

                    ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.showAccountStatus, mbox).setAccounts(account).setCardNum(card_ID).
                            setAccountAmount((int) enquiryAmount).setOutAmount((int) outAmount).build());
                }
            } catch (BAMSInvalidReplyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get all the information of selected Account
     */
    private void GetAllAccountInformation() {

        if (card_ID != null && account != null) {
            if (bamsHandler == null) {
                bamsHandler = new BAMSHandler(urlPrefix, log);
            }
            try {
                double amount = bamsHandler.enquiry(this.card_ID, account, "cred");
                System.out.println(amount);
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.print_advice, mbox).setAccounts(account).setCardNum(card_ID).setAccountAmount((int) amount).build());
                showStatus("Get All information of :"+card_ID+"\n"
                +"Account: "+account);
            } catch (BAMSInvalidReplyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param msg message Receive
     * Handle enquiry action
     */
    private void HandleAccountAmout(Msg msg) {
        if (card_ID != null && account != null) {
            if (bamsHandler == null) {
                bamsHandler = new BAMSHandler(urlPrefix, log);
            }

            try {
                double amount = bamsHandler.enquiry(this.card_ID, account, "cred");
                System.out.println(amount);
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.showAccountStatus, mbox).setCardNum(card_ID).setAccountAmount((int) amount).setAccounts(account).build());
                showStatus("Send out cardID: " + card_ID
                        + "\nSend out Account_no: " + account + "\nsend out Amount: " + amount);
            } catch (BAMSInvalidReplyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    protected void showStatus(String Line) {
    }

    /**
     * @param msg message Received
     *   Handle Account Inquiry Action
     */
    private void HandleAccountInquiry(Msg msg) {
        if (card_ID != null) {

            if (bamsHandler == null) {
                bamsHandler = new BAMSHandler(urlPrefix, log);
            }

            try {
                String accounts = bamsHandler.getAccounts(card_ID, "cred1");
                accounts = accounts.substring(0, accounts.length() - 1);
                //System.out.println("getAccounts: "+accounts);
                //System.out.println(accounts.substring(0,accounts.length()-1));
                ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.GetAccount, mbox).setAccounts(accounts).build());
                showStatus("Receive Accounts " + accounts);
            } catch (BAMSInvalidReplyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param msg Message Receive
     *            Handle Login Action
     */
    private void HandleCardMessage(Msg msg) {
        if (msg.getDetails().equalsIgnoreCase("Card ID")) {
            log.info("Receive cardMessage: [" + msg.getCardnum() + "]");
            this.card_ID = msg.getCardnum();
            showStatus("Receive cardID: " + card_ID);
        } else if (msg.getDetails().equalsIgnoreCase("pin")) {
            log.info("Receive pin: [ " + msg.getPin() + "]");
            this.pin = msg.getPin();
            showStatus("Receive pin: " + pin);
        }

        if (card_ID != null && pin != null) {
            bamsHandler = new BAMSHandler(urlPrefix, log);
            try {
                System.out.println("login");
                String cred = bamsHandler.login(card_ID, pin);
                System.out.println("Cred: " + cred);

                if (cred.equalsIgnoreCase("Credential")) {
                    ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.LoginSuccess, mbox).setDetails("Login Success").build());
                    showStatus("Login successfully");
                } else if(cred.equalsIgnoreCase("Wrong card")){
                    ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.ejectCard,mbox).setDetails("Eject Card: ").setCardNum(card_ID).build());
                }else {

                    if (WrongPinCounter == totalWrongpin) {
                        ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.retainCard, mbox).setDetails("retain card").build());
                        WrongPinCounter = 1;
                        showStatus("Retain cardID [" + card_ID + "]");
                    } else {
                        ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.WrongPin, mbox).setDetails("Wrong pin , remain " + (totalWrongpin - WrongPinCounter) + " chances").build());
                        showStatus("wrong Login in, Remaining chance " + (totalWrongpin - WrongPinCounter));
                        WrongPinCounter++;
                    }
                }
            } catch (BAMSInvalidReplyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
