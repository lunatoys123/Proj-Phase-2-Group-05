package AppStarter.common;

/**
 * The class that Handle Message
 */
public class Msg {

    private final String id;
    private final Type type;
    private final MBox sender;
    private final String details;
    private final String cardnum;
    private final String key;
    private final String filename;
    private final String pin;
    private final boolean state;
    private final String Accounts;
    private final int Account_Amount;
    private final int inAmount;
    private final String Action;
    private final int outAmount;
    private final String Timer;
    private final String FromAccount;
    private final String outAccount;
    private final int transferAmount;
    private final String error;
    private final boolean fatal;

    private Msg(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.sender = builder.sender;
        this.details = builder.details;
        this.cardnum = builder.cardnum;
        this.key = builder.key;
        this.filename = builder.filename;
        this.pin = builder.pin;
        this.state = builder.state;
        this.Accounts = builder.Accounts;
        this.Account_Amount = builder.Account_Amount;
        this.Action = builder.Action;
        this.outAmount = builder.outAmount;
        this.Timer = builder.Timer;
        this.inAmount = builder.inAmount;
        this.FromAccount=builder.FromAccount;
        this.outAccount=builder.OutAccount;
        this.transferAmount= builder.transferAmount;
        this.error=builder.error;
        this.fatal=builder.fatal;
    }

    public Type getType() {
        return type;
    }

   /* public Msg(String id,Type type,MBox sender,String details){
        this.id=id;
        this.type=type;
        this.sender=sender;
        this.details=details;
    }*/

    public String getId() {
        return id;
    }

    public MBox getSender() {
        return sender;
    }

    public String getDetails() {
        return details;
    }

    public String getFilename() {
        return filename;
    }

    public String getkey() {
        return key;
    }

    public String getCardnum() {
        return cardnum;
    }

    public String getPin() {
        return pin;
    }

    public String cardNumString() {
        return id + " : " + details + " [" + cardnum + "]";
    }

    public boolean isState() {
        return state;
    }

    public String getAccounts() {
        return Accounts;
    }

    public int getAccount_Amount() {
        return Account_Amount;
    }

    public String getAction() {
        return Action;
    }

    public int getOutAmount() {
        return outAmount;
    }

    public String getTimer() {
        return Timer;
    }

    public int getInAmount() {
        return inAmount;
    }

    public String toString() {
        return id + " : " + details;
    }

    public String getFromAccount() {
        return FromAccount;
    }

    public String getOutAccount() { return outAccount; }

    public boolean getState(){
        return state;
    }

    public int getTransferAmount() {
        return transferAmount;
    }

    public String getError(){
        return error;
    }

    public boolean isFatal() {
        return fatal;
    }

    /**
     * Declare Type of Message to make Hardware classify the message
     */
    public enum Type {
        poll,        //send poll Message
        pollAck,     //send poll Acknowledgment
        insertCard, //CardReader insert card
        password_validation, //Check the card
        setupTimer, //setTimer for poll
        CancelTimer, //cancel Timer
        TimesUp,
        Tick,
        error,
        keyPressed,
        changeScene,
        add_key,
        add_key_Enter_amount, //when enter the amount
        add_key_Transfer_amount,
        Delete_Key,
        Delete_key_Enter_amount,
        Delete_key_Transfer_amount,
        Reset_key,
        Reset_key_Enter_amount,
        Reset_key_Transfer_amount,
        cardMessage,
        LoginSuccess, //when login into BAMS is successful
        WrongPin, // when user enter wrong pin
        release_key,
        retainCard,
        ejectCard,
        Terminate, //when the application close
        AccountInquiry, //Enter the Account Inquiry state
        AccountInquiry_countdown, //invoke the count down when print advice
        GetAccount,  //Get the account
        GetAmount, //Get the Account amount
        showAccountStatus, //when get Account Amount and show status
        print_advice, //when user want to print advice
        Get_All_Information_Account_inquiry, // invoke when print advice ,need all information (Account inquiry)
        alarm,
        EndService,
        withdrawal,   // when user require withdrawal
        Enter_withdrawal_amount, //when user need to enter withdrawal amount
        show_withdraw_money, //when this application need to show status after withdraw money
        withdraw_countdown,
        wait_for_deposit, //make touchscreen display wait for deposit
        send_Timer_Text, //send deposit timer to touchscreen display
        deposit, //when user want to deposit
        choose_TransferTo_account, //when user select the account that want to Transfer to
        choose_Transfer_Amount, //when user input the transfer amount
        SendTransfer,
        Transfer,
        Enter, //when use press enter
        Delete,
        Error, //when user don't have enough account for transfer
        error_Timer,
        stop_component,
        restart,
        return_to_main,//type for you to return to the main surface
        DepositProblem

    }

    /**
     * Builder class for Message ,enable to put different components of Message
     */
    public static class Builder {

        private final String id;
        private final Type type;
        private final MBox sender;
        private String details;
        private String cardnum;
        private String key;
        private String filename;
        private String pin;
        private boolean state;
        private String Accounts;
        private int Account_Amount;
        private String Action;
        private int outAmount;
        private String Timer;
        private int inAmount;
        private String FromAccount;
        private String OutAccount;
        private int transferAmount;
        private String error;
        private boolean fatal;



        public Builder(String id, Type type, MBox sender) {
            this.id = id;
            this.type = type;
            this.sender = sender;
        }

        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder setCardNum(String cardNum) {
            this.cardnum = cardNum;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder setPin(String pin) {
            this.pin = pin;
            return this;
        }

        public Builder setState(boolean state) {
            this.state = state;
            return this;
        }

        public Builder setAccounts(String accounts) {
            this.Accounts = accounts;
            return this;
        }

        public Builder setAccountAmount(int account_Amount) {
            this.Account_Amount = account_Amount;
            return this;
        }

        public Builder setAction(String Action) {
            this.Action = Action;
            return this;
        }

        public Builder setOutAmount(int outAmount) {
            this.outAmount = outAmount;
            return this;
        }

        public Builder setTimer(String Timer) {
            this.Timer = Timer;
            return this;
        }

        public Builder setInAmount(int InAmount) {
            this.inAmount = InAmount;
            return this;
        }

        public Builder setFromAccount(String FromAccount){
            this.FromAccount=FromAccount;
            return this;
        }

        public Builder setOutAccount(String outAccount){
            this.OutAccount=outAccount;
            return this;
        }

        public Builder setTransferAmount(int transferAmount){
            this.transferAmount=transferAmount;
            return this;
        }

        public Builder setError(String error){
            this.error=error;
            return this;
        }

        public Builder setFatal(boolean fatal){
            this.fatal=fatal;
            return this;
        }

        public Msg build() {
            return new Msg(this);
        }


    }
}
