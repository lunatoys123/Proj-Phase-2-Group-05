package AppStarter.Hardware.TouchScreenDisplay;

import AppMain.ATM_SS_Starter;
import AppStarter.common.Msg;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class TouchScreenDisplay_Emulator extends TouchScreenDisplay {
    private static final int WIDTH = 580;
    private static final int HEIGHT = 437;
    private final String id;
    private final ATM_SS_Starter atm_ss_starter;
    private Stage MyStage;
    private TouchScreenDisplayController touchScreenDisplayController;


    public TouchScreenDisplay_Emulator(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.id = id;
        this.atm_ss_starter = atm_ss_starter;
    }

    public void start() throws IOException {
        Parent root;
        MyStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "WelcomePage.fxml";
        loader.setLocation(TouchScreenDisplay_Emulator.class.getResource(fxmlName));
        root = loader.load();
        touchScreenDisplayController = loader.getController();
        touchScreenDisplayController.initialize(id, atm_ss_starter, log, this);
        MyStage.initStyle(StageStyle.DECORATED);
        MyStage.setScene(new Scene(root, WIDTH, HEIGHT));
        MyStage.setResizable(false);
        MyStage.setTitle("TouchScreenDisplay");
        MyStage.setOnCloseRequest((WindowEvent event) -> {
            atm_ss_starter.stopApp();
            Platform.exit();
        });
        MyStage.show();
    }

    public void addstar(String key) {
        touchScreenDisplayController.addstar(key);
    }

    public void removeStar() {
        touchScreenDisplayController.removeStar();
    }

    public void wrongpin(String text) {
        touchScreenDisplayController.wrongPin(text);
    }

    public void addkey(String key) {
        touchScreenDisplayController.addkey(key);
    }

    public void ResetStar() {
        touchScreenDisplayController.resetStar();
    }

    public void countdown(String text) {
        touchScreenDisplayController.countdown(text);
    }

    public void addTransferAmount(String key) {
        touchScreenDisplayController.addAmount(key);
    }

    public void sendTransferInformation() {
        touchScreenDisplayController.sendTransformationInformation();
    }

    public void deleteKey(String action) {
        touchScreenDisplayController.deleteKey(action);
    }

    public void ResetKey(String action) {
        touchScreenDisplayController.resetKey(action);
    }

    public void SetErrorInitial(int ErrorWaitTime, String error){
        touchScreenDisplayController.setErrorInital(ErrorWaitTime,error);
    }

    public void updateErrorTimerText(int errorWaitTime){
        touchScreenDisplayController.updateErrorTimerText(errorWaitTime);
    }

    public void stopComponent(){touchScreenDisplayController.stopComponent();}

    public void DeleteTransferAmount(){touchScreenDisplayController.DeleteTransferAmount();}

    public void ResetTransferAmount(){touchScreenDisplayController.ResetTransferAmount();}

    public void reloadPages(String filename) {
        TouchScreenDisplay_Emulator touchScreenDisplay_emulator = this;
        Platform.runLater(() -> {
            log.info("change scene into: " + filename);

            try {
                Parent root;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(TouchScreenDisplay_Emulator.class.getResource(filename));
                root = loader.load();
                touchScreenDisplayController = loader.getController();
                touchScreenDisplayController.initialize(id, atm_ss_starter, log, touchScreenDisplay_emulator);
                MyStage.setScene(new Scene(root, WIDTH, HEIGHT));

                if (filename.equalsIgnoreCase("GetAccount.fxml")) {
                    System.out.println("Mystate: " + state);
                    //System.out.println("Account Amount: "+accounts_amount);
                    System.out.println("Account: " + accounts);
                    if (accounts != null) {

                        if(state.equalsIgnoreCase("fromTransfer")){
                            String[] accountText = accounts.split("/");
                            if(accountText.length<2){
                                ATMSS_BOX.send(new Msg.Builder(id,Msg.Type.Error,mbox).setFilename("Error.fxml").setError("Not Enough Number for Transfer").setFatal(false).build());
                            }else{
                                touchScreenDisplayController.setState(state);
                                touchScreenDisplayController.setAccountText(accountText);
                            }

                        }else{
                            String[] accountText = accounts.split("/");
                            touchScreenDisplayController.setState(state);
                            touchScreenDisplayController.setAccountText(accountText);
                        }


                    }
                } else if (filename.equalsIgnoreCase("Account_status.fxml")) {
                    System.out.println("Mystate: " + state);
                    System.out.println("Account Amount: " + accounts_amount);
                    System.out.println("Account: " + accounts);
                    touchScreenDisplayController.setState(state);
                    touchScreenDisplayController.InitialGetAccount(accounts_amount, accounts);


                } else if (filename.equalsIgnoreCase("Account_status_for_transfer.fxml")) {

                    if (state.equalsIgnoreCase("withdraw")) {
                        touchScreenDisplayController.setState(state);
                        touchScreenDisplayController.InitialGetAccount(accounts_amount + "", accounts, withdrawAmount + "", state);
                    } else if (state.equalsIgnoreCase("deposit")) {
                        touchScreenDisplayController.setState(state);
                        touchScreenDisplayController.InitialGetAccount(accounts_amount + "", accounts, depositAmount + "", state);
                    } else if (state.equalsIgnoreCase("afterTransfer")) {
                        touchScreenDisplayController.setState(state);
                        touchScreenDisplayController.InitialGetAccount(cardID, Transfer_formAccount, Transfer_ToAccount, TransferAmount, state);
                    }
                } else if (filename.equalsIgnoreCase("ChooseFromAccount.fxml")) {
                    String[] account_Text = accounts.split("/");
                    String FromAccount = Transfer_formAccount;
                    touchScreenDisplayController.setState(state);
                    touchScreenDisplayController.setAccountText(account_Text, FromAccount);
                } else if(filename.equalsIgnoreCase("Error.fxml")){
                    touchScreenDisplayController.setErrorInital(ErrorWaitTime,error);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }


}
