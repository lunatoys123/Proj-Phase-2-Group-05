package AppStarter.Hardware.TouchScreenDisplay;

import AppStarter.common.AppStarter;
import AppStarter.common.MBox;
import AppStarter.common.Msg;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class TouchScreenDisplayController {

    private final String AccountInquiryState = "Account Inquiry";
    private final String withdrawState = "withdraw";
    private final String depositState = "deposit";
    private final String fromTransferState = "fromTransfer";
    private final String ToTransferState = "ToTransfer";
    private final String AfterTransfer = "afterTransfer";
    private final String ToReturn = "Toreturn";
    public Label DepositText;
    public Label TimerText;
    public TextField TransferAmountText;
    public Label ErrorText;
    public Label ErrorTimerText;
    private String id;
    private AppStarter appStarter;
    private Logger logger;
    private TouchScreenDisplay_Emulator touchScreenDisplay_emulator;
    private MBox touchScreenMBox;
    private String state;
    @FXML
    private TextField pinTextField;
    @FXML
    private Label WrongPinText;
    @FXML
    private Label PersonalAccountText;

    @FXML
    private Label BussinessAccountText;

    @FXML
    private Label statusText;

    @FXML
    private Label OverSeaAccountText;

    @FXML
    private TextField AmounTextField;

    @FXML
    private Label Account_Transfer_text;

    public void initialize(String id, AppStarter appStarter, Logger logger, TouchScreenDisplay_Emulator touchScreenDisplay_emulator) {
        this.id = id;
        this.appStarter = appStarter;
        this.logger = logger;
        this.touchScreenDisplay_emulator = touchScreenDisplay_emulator;
        this.touchScreenMBox = appStarter.getThread("TouchScreen").getMbox();
    }

    /**
     * add star when user enter the pin
     */
    public void addstar(String key) {
        Platform.runLater(() -> {
            if(key.length() == 2){
                pinTextField.appendText("**");
            }else {
                pinTextField.appendText("*");
            }
        });

    }

    /**
     * Action when user enter the wrong pin
     * @param text text send when wrong pin is detect
     */
    public void wrongPin(String text) {
        Platform.runLater(() -> {
            WrongPinText.setText(text);
            pinTextField.setText("");
        });
    }

    /**
     * append key when user need to enter Transfer amount
     * @param key key input
     */
    public void addAmount(String key) {
        Platform.runLater(() -> {
            TransferAmountText.appendText(key);
        });
    }

    /**
     * send all require information for Transfer action
     */
    public void sendTransformationInformation() {
        String FromAccount = touchScreenDisplay_emulator.Transfer_formAccount;
        String toAccount = touchScreenDisplay_emulator.Transfer_ToAccount;

        System.out.println(FromAccount);
        System.out.println(toAccount);
        System.out.println(TransferAmountText.getText());
        int TransferAmount=TransferAmountText.getText().equalsIgnoreCase("")?0:Integer.parseInt(TransferAmountText.getText());
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.Transfer, touchScreenMBox).setFromAccount(FromAccount).
                setOutAccount(toAccount).setTransferAmount(TransferAmount).build());
        TransferAmountText.setText("");
    }


    /*
    Return to the main surface
    */

    public void Return(MouseEvent mouseEvent) {


        //String account_no = BussinessAccountText.getText().substring(0, 8);

        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.return_to_main, touchScreenMBox).
                setDetails("To return to main").setAction(ToReturn).setFilename("TouchScreenDisplayMainPage.fxml").build());

        System.out.println("114514");

    }

    /**
     * set all available account in touch screen display when user require to choose account
     * @param accountText All account
     */
    public void setAccountText(String[] accountText) {
        if (accountText == null) return;

        Platform.runLater(() -> {
            for (String account : accountText) {
                if (account.contains("Personal")) {
                    PersonalAccountText.setText(account);
                } else if (account.contains("Business")) {
                    BussinessAccountText.setText(account);
                } else if (account.contains("OverSea")) {
                    OverSeaAccountText.setText(account);
                }
            }
        });
    }

    /**
     * set all available account in touch screen display when user require to choose account, except the fromAccount
     * @param accountText All account
     * @param formAccount Account that start Transfer
     */
    public void setAccountText(String[] accountText, String formAccount) {
        if (accountText == null) return;

        Platform.runLater(() -> {
            for (String account : accountText) {
                String accountStr = account.substring(0, 8);
                if (!accountStr.equalsIgnoreCase(formAccount)) {
                    if (account.contains("Personal")) {
                        PersonalAccountText.setText(account);
                    } else if (account.contains("Business")) {
                        BussinessAccountText.setText(account);
                    } else if (account.contains("OverSea")) {
                        OverSeaAccountText.setText(account);
                    }
                }
            }
        });

    }

    /**
     * show Account status on touch Screen display
     * @param amount Account amount
     * @param account Account
     */
    public void InitialGetAccount(int amount, String account) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        statusText.setText(dataFormat.format(new Date()) + "\n"
                + "Remain Amount of " + account + "\n" +
                "Total Amount: " + amount);

    }

    /**
     * show Account status on touch Screen display
     * @param amount Account amount
     * @param account Account number
     * @param outAmount deposit or withdraw number
     * @param Action user action
     */
    public void InitialGetAccount(String amount, String account, String outAmount, String Action) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyy HH:mm");
        Account_Transfer_text.setText(dataFormat.format(new Date()) + "\n"
                + "Remain Amount of " + account + "\n"
                + Action + ": " + outAmount + "\n"
                + "Total Amount: " + amount);

    }

    /**
     * show Account status in touch screen display
     * @param cardNumber  Card Number
     * @param FromAccount Account that start Transfer
     * @param OutAccount Account that receive Transfer
     * @param TransferAmount Transfer Amount
     * @param Action user action
     */
    public void InitialGetAccount(String cardNumber, String FromAccount, String OutAccount, int TransferAmount, String Action) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("dd/MM/yyy HH:mm");
        Account_Transfer_text.setText(dataFormat.format(new Date()) + "\n"
                + "CardID: " + cardNumber + "\n"
                + "Transfer_Amount of " + FromAccount + "\n"
                + " Transfer_Amount to " + OutAccount + "\n"
                + Action + ": " + TransferAmount + "\n");

    }

    public void AccountInquiry(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.AccountInquiry, touchScreenMBox).setDetails("Account Inquiry").setAction(AccountInquiryState).build());
    }

    /**
     * Action when user choose Personal Account in any state
     * @param mouseEvent mouse event
     */
    public void PersonalAccount(MouseEvent mouseEvent) {
        if (PersonalAccountText.getText().equalsIgnoreCase("")) return;
        String account_no = PersonalAccountText.getText().substring(0, 8);
        System.out.println(account_no);

        if (state.equalsIgnoreCase(AccountInquiryState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.GetAmount, touchScreenMBox).setDetails("Get Account Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(withdrawState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.Enter_withdrawal_amount, touchScreenMBox).setDetails("Get Account Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(depositState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.wait_for_deposit, touchScreenMBox).setDetails("Get deposit Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(fromTransferState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.choose_TransferTo_account, touchScreenMBox).
                    setDetails("To TransferTo Account").setAction(ToTransferState).setFromAccount(account_no).setFilename("ChooseFromAccount.fxml").build());
        } else if (state.equalsIgnoreCase(ToTransferState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.choose_Transfer_Amount, touchScreenMBox).
                    setDetails("To Transfer amount").setFromAccount(touchScreenDisplay_emulator.Transfer_formAccount).
                    setOutAccount(account_no).setFilename("ChooseTransferAmount.fxml").build());
        }


    }

    public void BusinessAccount(MouseEvent mouseEvent) {
        if (BussinessAccountText.getText().equalsIgnoreCase("")) return;

        String account_no = BussinessAccountText.getText().substring(0, 8);
        System.out.println(account_no);
        if (state.equalsIgnoreCase(AccountInquiryState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.GetAmount, touchScreenMBox).setDetails("Get Account Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(withdrawState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.Enter_withdrawal_amount, touchScreenMBox).setDetails("Get Account Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(depositState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.wait_for_deposit, touchScreenMBox).setDetails("Get deposit Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(fromTransferState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.choose_TransferTo_account, touchScreenMBox).
                    setDetails("To TransferTo Account").setAction(ToTransferState).setFromAccount(account_no).setFilename("ChooseFromAccount.fxml").build());
        } else if (state.equalsIgnoreCase(ToTransferState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.choose_Transfer_Amount, touchScreenMBox).
                    setDetails("To Transfer amount").setFromAccount(touchScreenDisplay_emulator.Transfer_formAccount).
                    setOutAccount(account_no).setFilename("ChooseTransferAmount.fxml").build());
        }
    }

    /**
     * Action when User choose to print advice
     * @param mouseEvent mouse event
     */
    public void PrintAdvice(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.Get_All_Information_Account_inquiry, touchScreenMBox).build());
    }

    /**
     * Action when user choose overseaAccount in any state
     * @param mouseEvent mouse event
     */
    public void OverseaAccount(MouseEvent mouseEvent) {
        if (OverSeaAccountText.getText().equalsIgnoreCase("")) return;

        String account_no = OverSeaAccountText.getText().substring(0, 8);
        System.out.println(account_no);

        if (state.equalsIgnoreCase(AccountInquiryState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.GetAmount, touchScreenMBox).setDetails("Get Account Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(withdrawState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.Enter_withdrawal_amount, touchScreenMBox).setDetails("Get Account AMount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(depositState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.wait_for_deposit, touchScreenMBox).setDetails("Get deposit Amount").setAccounts(account_no).build());
        } else if (state.equalsIgnoreCase(fromTransferState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.choose_TransferTo_account, touchScreenMBox).
                    setDetails("To TransferTo Account").setAction(ToTransferState).setFromAccount(account_no).setFilename("ChooseFromAccount.fxml").build());
        } else if (state.equalsIgnoreCase(ToTransferState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.choose_Transfer_Amount, touchScreenMBox).
                    setDetails("To Transfer amount").setFromAccount(touchScreenDisplay_emulator.Transfer_formAccount).
                    setOutAccount(account_no).setFilename("ChooseTransferAmount.fxml").build());
        }

    }

    /**
     * stop the service of ATM-SS and all hardware components
     * @param mouseEvent mouse event
     */
    public void EndService(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.EndService, touchScreenMBox).build());
    }

    /**
     * Function that allow use to continue working when in Account Inquiry state
     * @param mouseEvent mouse event
     */
    public void ContinueOperation(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.changeScene, touchScreenMBox).setFilename("TouchScreenDisplayMainPage.fxml").build());
    }

    /**
     * Action when user choose withdraw on touch screen display
     * @param mouseEvent mouse event
     */
    public void withdrawal(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.AccountInquiry, touchScreenMBox).setDetails("withdraw").setAction(withdrawState).build());
    }

    /**
     * set the state of the touch screen display
     * @param state state of touch screen display
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @param key key input
     */
    public void addkey(String key) {
        Platform.runLater(() -> {

            AmounTextField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                        AmounTextField.setText(oldValue);
                    }
                }
            });
            String getAmount= AmounTextField.getText();
            if(getAmount.length()<6){
                AmounTextField.appendText(key);
            }
        });

    }

    /**
     * Function that able user to delete a key when input
     * @param action touch screen display state
     */
    public void deleteKey(String action) {
        Platform.runLater(() -> {

            if (action.equalsIgnoreCase(withdrawState)) {
                String text = AmounTextField.getText();
                text = text.trim();
                if (text.length() > 0) {
                    text = text.substring(0, text.length() - 1);
                    AmounTextField.setText(text);
                }
            }

        });
    }

    /**
     * Function that able the user to earse all the key when input
     * @param action touch screen display state
     */
    public void resetKey(String action){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(action.equalsIgnoreCase(withdrawState)){
                    AmounTextField.setText("");
                }
            }
        });
    }

    /**
     * @param mouseEvent action when user choose withdraw and enter confirm amount on touch Screen display
     */
    public void ConfirmAmount(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.withdrawal, touchScreenMBox).setAccounts(touchScreenDisplay_emulator.accounts).
                setAccountAmount(Integer.parseInt(AmounTextField.getText())).build());
    }

    /**
     * action when user choose to not print advice
     * @param mouseEvent  mouse event
     */
    public void noAdvice(MouseEvent mouseEvent) {

        if (state.equalsIgnoreCase(withdrawState)) {

            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.show_withdraw_money, touchScreenMBox).setOutAmount(touchScreenDisplay_emulator.withdrawAmount).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.alarm, touchScreenMBox).setAction(state).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.withdraw_countdown, touchScreenMBox).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.changeScene, touchScreenMBox).setFilename("Thankyou.fxml").build());
        }
        else if (state.equalsIgnoreCase(depositState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.restart, touchScreenMBox).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.alarm, touchScreenMBox).setAction(state).build());
        } else if (state.equalsIgnoreCase(AfterTransfer)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.restart, touchScreenMBox).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.alarm, touchScreenMBox).setAction(state).build());
        }

    }


    /**
     * action when user choose to print advice
     * @param mouseEvent mouse event
     *
     */
    public void YesAdvice(MouseEvent mouseEvent) {

        if (state.equalsIgnoreCase(withdrawState)) {

            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.show_withdraw_money, touchScreenMBox).setOutAmount(touchScreenDisplay_emulator.withdrawAmount).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.alarm, touchScreenMBox).setAction(state).build());

            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.withdraw_countdown, touchScreenMBox).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.print_advice, touchScreenMBox).setAccounts(touchScreenDisplay_emulator.accounts).
                    setAccountAmount(touchScreenDisplay_emulator.accounts_amount).setOutAmount(touchScreenDisplay_emulator.withdrawAmount).setAction(state).build());
        } else if (state.equalsIgnoreCase(depositState)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.alarm, touchScreenMBox).setAction(state).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.print_advice, touchScreenMBox).setAccounts(touchScreenDisplay_emulator.accounts).
                    setAccountAmount(touchScreenDisplay_emulator.accounts_amount).setInAmount(touchScreenDisplay_emulator.depositAmount).setAction(state).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.restart, touchScreenMBox).build());

        } else if (state.equalsIgnoreCase(AfterTransfer)) {
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.alarm, touchScreenMBox).setAction(state).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.print_advice, touchScreenMBox).setFromAccount(touchScreenDisplay_emulator.Transfer_formAccount).
                    setOutAccount(touchScreenDisplay_emulator.Transfer_ToAccount).setTransferAmount(touchScreenDisplay_emulator.TransferAmount).
                    setCardNum(touchScreenDisplay_emulator.cardID).setAction(state).build());
            touchScreenMBox.send(new Msg.Builder(id, Msg.Type.restart, touchScreenMBox).build());
        }
    }

    /**
     * Action when user choose to deposit on touch screen display
     * @param mouseEvent mouse event
     */
    public void Deposit(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.AccountInquiry, touchScreenMBox).setAction(depositState).setDetails("Deposit state").build());
    }

    /**
     * Perform countdown on touchScreen display
     * @param text count down text
     */
    public void countdown(String text) {
        Platform.runLater(() -> TimerText.setText(text));
    }

    /**
     * action when user choose transfer in touchScreen display
     * @param mouseEvent mouse event
     */
    public void Transfer(MouseEvent mouseEvent) {
        touchScreenMBox.send(new Msg.Builder(id, Msg.Type.AccountInquiry, touchScreenMBox).setAction(fromTransferState).setDetails("transfer").build());

    }

    /**
     * when user enter pin, this function allow user to delete a key
     */
    public void removeStar() {
        Platform.runLater(() -> {
            String text = pinTextField.getText();
            text = text.trim();
            if (text.length() > 0) {
                text = text.substring(0, text.length() - 1);
                pinTextField.setText(text);
            }
        });

    }

    public void resetStar() {
        Platform.runLater(() -> pinTextField.setText(""));
    }



    public void OnChooseAmount(MouseEvent mouseEvent) {

        Label AmountLabel = (Label) mouseEvent.getSource();

        switch (AmountLabel.getId()) {
            case "Amount100":
                AmounTextField.setText(String.valueOf(100));
                break;
            case "Amount200":
                AmounTextField.setText(String.valueOf(200));
                break;
            case "Amount500":
                AmounTextField.setText(String.valueOf(500));
                break;
            case "Amount1000":
                AmounTextField.setText(String.valueOf(1000));
                break;
        }
    }

    public void setErrorInital(int errorWaitTime, String error) {
        Platform.runLater(() -> {
            ErrorText.setText(error);
            ErrorTimerText.setText("Remaining Time: "+errorWaitTime);
        });

    }

    public void updateErrorTimerText(int errorWaitTime){
        Platform.runLater(() -> ErrorTimerText.setText("Remaining Time: "+errorWaitTime));
    }

    public void stopComponent() {

    }

    public void DeleteTransferAmount(){
        Platform.runLater(() -> {
            String amount=TransferAmountText.getText();
            amount=amount.trim();

            if(amount.length()>0){
                amount=amount.substring(0,amount.length()-1);
                TransferAmountText.setText(amount);
            }
        });


    }

    public void ResetTransferAmount(){
        TransferAmountText.setText("");
    }
}
