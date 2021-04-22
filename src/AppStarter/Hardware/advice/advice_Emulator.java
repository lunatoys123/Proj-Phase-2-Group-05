package AppStarter.Hardware.advice;

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

public class advice_Emulator extends advicePrinter {
    private final String id;
    private Stage MyStage;
    private advicePrinterController advicePrinterController;
    private final ATM_SS_Starter atm_ss_starter;

    public advice_Emulator(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.id = id;
        this.atm_ss_starter = atm_ss_starter;
    }

    public void start() throws IOException {
        Parent root;
        MyStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxml = "advicePrinter.fxml";
        loader.setLocation(advice_Emulator.class.getResource(fxml));
        root = loader.load();
        advicePrinterController = loader.getController();
        advicePrinterController.initialize(id, atm_ss_starter, log, this);
        MyStage.initStyle(StageStyle.DECORATED);
        MyStage.setScene(new Scene(root, 387, 400));
        MyStage.setResizable(false);
        MyStage.setTitle("Advice Printer");
        MyStage.setOnCloseRequest((WindowEvent event) -> {
            atm_ss_starter.stopApp();
            Platform.exit();
        });
        MyStage.show();
    }

    /**
     * print advice for Account Inquiry
     * @param numberOfPapers Remain Papers in advice
     * @param cardnum cardNumber
     * @param account accountNumber
     * @param amount AccountAmount
     */
    public void printadvice(int numberOfPapers,String cardnum,String account,double amount){
        advicePrinterController.printAdvice(numberOfPapers,cardnum,account,amount);
    }

    /**
     * print advice for withdraw or deposit
     * @param numOfPapers Remain number of Advice
     * @param account Account number
     * @param account_amount Account amount
     * @param outAmount withdraw amount of deposit amount
     * @param action detect is user is withdraw or deposit
     */
    public void printadviceForTransition(int numOfPapers,String account,int account_amount,int outAmount,String action){
        advicePrinterController.printadviceForTransition(numOfPapers,account,account_amount,outAmount,action);
    }


    /**
     * print advice for Transfer action
     * @param numOfPapers Remain number of Advice
     * @param cardID cardnumber
     * @param FromAccount Transfer from Account
     * @param outAccount Transfer To Account
     * @param TransferAmount transfer amount between two account
     * @param action user action for Transfer
     */
    public void printadviceForTransfer(int numOfPapers,String cardID,String FromAccount,String outAccount,int TransferAmount,String action){
        advicePrinterController.printadviceForTransfer(numOfPapers,cardID,FromAccount,outAccount,TransferAmount,action);
    }

    /**
     * check the advice is out of paper or not
     */
    public void checkHardware(){
        if(numOfPapers==0){
            ATMSS_BOX.send(new Msg.Builder(id,Msg.Type.Error,mbox).setError("Out OF Papaer for advice").setFilename("Error.fxml").
                    setFatal(true).build());
            System.out.println("execute out of papers");
        }else{
            ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.pollAck, mbox).setDetails("Poll Acknowledgement").build());
        }
    }


}
