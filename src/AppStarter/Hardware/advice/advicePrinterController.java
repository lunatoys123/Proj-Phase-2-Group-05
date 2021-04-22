package AppStarter.Hardware.advice;

import AppStarter.common.AppStarter;
import AppStarter.common.MBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class advicePrinterController {
    private String id;
    private AppStarter appStarter;
    private Logger logger;
    private advice_Emulator advice_emulator;
    private MBox adviceMbox;
    @FXML
    private TextArea PrinterContent;

    public void initialize(String id, AppStarter appStarter, Logger logger, advice_Emulator advice_emulator) {
        this.id = id;
        this.appStarter = appStarter;
        this.logger = logger;
        this.advice_emulator = advice_emulator;
        this.adviceMbox = appStarter.getThread("advicePrinter").getMbox();
    }

    /**
     * print advice
     * @param numberOfPapers remain number of paper from advice
     * @param cardno cardNumber
     * @param account AccountNumber
     * @param amount AccountAMount
     */
    public void printAdvice(int numberOfPapers, String cardno, String account, double amount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PrinterContent.appendText(
                        "Paper left: " + numberOfPapers
                                + "\n" + dateFormat.format(new Date())
                                + "\ncard ID: " + cardno
                                + "\nRemain Amount of Account [" + account + "]: " + amount + "\n");
            }
        });

    }

    /**
     * print advice
     * @param numOfPapers remain number of papers
     * @param account Account
     * @param account_amount Account Amount
     * @param outAmount withdraw or deposit amount
     * @param action user action
     */
    public void printadviceForTransition(int numOfPapers, String account, int account_amount, int outAmount, String action) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PrinterContent.appendText(
                        "Paper left: " + numOfPapers
                                + "\n" + dateFormat.format(new Date())
                                + "\n" + action + ": " + outAmount
                                + "\nRemain Amount of Account [" + account + "]: " + account_amount + "\n");
            }
        });
    }

    /**
     * print advice
     * @param numOfPapers remain number of papers
     * @param cardID cardNumber
     * @param FromAccount Account start transaction
     * @param outAccount Account receive transaction
     * @param TransferAmount Transfer amount
     * @param action user action
     */
    public void printadviceForTransfer(int numOfPapers, String cardID, String FromAccount, String outAccount, int TransferAmount, String action) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                PrinterContent.appendText(
                        "Paper left: " + numOfPapers
                                + "\n" + dateFormat.format(new Date())
                                + "\n" + action + ": " + action
                                + "\n" + "Transfer From: " + FromAccount
                                + "\n" + "Transfer To: " + outAccount
                                + "\n" + "Transfer Amount: " + TransferAmount);
            }
        });
    }


    public void TakeAdvice(MouseEvent mouseEvent) {
        Platform.runLater(() -> PrinterContent.setText(""));
    }
}
