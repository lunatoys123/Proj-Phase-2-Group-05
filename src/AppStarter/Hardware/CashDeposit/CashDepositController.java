package AppStarter.Hardware.CashDeposit;

import AppStarter.common.AppStarter;
import AppStarter.common.CommonUtility;
import AppStarter.common.MBox;
import AppStarter.common.Msg;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.logging.Logger;

public class CashDepositController {
    public TextField NumberOfOneHundred;
    public TextField numberOfFiveHundred;
    public TextField NumberOfOneThousand;
    public TextField Total;
    public Button btnConfirmAmount;
    public Button btnReset;
    private String id;
    private AppStarter appStarter;
    private Logger logger;
    private CashDepositCollector_emulator cashDepositCollector_emulator;
    private MBox cashDepositBox;
    private CommonUtility common;
    private String account;

    public void initialize(String id, AppStarter appStarter, Logger logger, CashDepositCollector_emulator cashDepositCollector_emulator, CommonUtility common) {
        this.id = id;
        this.appStarter = appStarter;
        this.logger = logger;
        this.cashDepositCollector_emulator = cashDepositCollector_emulator;
        this.cashDepositBox = appStarter.getThread("CashDepositCollector").getMbox();
        this.common = common;
    }

    /**
     * calculate the total from the text field
     * @param keyEvent key Event
     */
    public void changeTotal(KeyEvent keyEvent) {

        NumberOfOneHundred.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    NumberOfOneHundred.setText(oldValue);
                }

                Total.setText(update() + "");
            }
        });

        numberOfFiveHundred.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    numberOfFiveHundred.setText(oldValue);
                }
                Total.setText(update() + "");
            }
        });

        NumberOfOneThousand.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    NumberOfOneThousand.setText(oldValue);
                }
                Total.setText(update() + "");
            }
        });

    }

    /**
     * enable all component
     * @param account account Number
     */
    public void Initial(String account) {
        numberOfFiveHundred.setEditable(true);
        NumberOfOneHundred.setEditable(true);
        NumberOfOneThousand.setEditable(true);
        btnConfirmAmount.setDisable(false);
        btnReset.setDisable(false);
        this.account = account;
    }

    /**
     * @return update the total and calcuate the total
     */
    public int update() {
        int numOfHundred = NumberOfOneHundred.getText().equals("") ? 0 : Integer.parseInt(NumberOfOneHundred.getText());
        int numOfFiveHundred = numberOfFiveHundred.getText().equals("") ? 0 : Integer.parseInt(numberOfFiveHundred.getText());
        int numOfOneThousand = NumberOfOneThousand.getText().equals("") ? 0 : Integer.parseInt(NumberOfOneThousand.getText());
        int total = numOfHundred * 100 + numOfFiveHundred * 500 + numOfOneThousand * 1000;
        return total;
    }

    /**
     * action of press confirm amount button
     * @param actionEvent button event
     */
    public void ConfirmAmount(ActionEvent actionEvent) {
        cashDepositCollector_emulator.confirmAmount = true;

        int numOfHundred = NumberOfOneHundred.getText().equals("") ? 0 : Integer.parseInt(NumberOfOneHundred.getText());
        int numOfFiveHundred = numberOfFiveHundred.getText().equals("") ? 0 : Integer.parseInt(numberOfFiveHundred.getText());
        int numOfOneThousand = NumberOfOneThousand.getText().equals("") ? 0 : Integer.parseInt(NumberOfOneThousand.getText());

        common.setNumberOfOneHundred(common.getNumberOfOneHundred()+numOfHundred);
        common.setNumberOfFiveHundred(common.getNumberOfFiveHundred()+numOfFiveHundred);
        common.setNumberOfOneThousand(common.getNumberOfOneThousand()+numOfOneThousand);

        System.out.println(common.getNumberOfOneHundred());
        System.out.println(common.getNumberOfFiveHundred());
        System.out.println(common.getNumberOfOneThousand());

        int total = (Total.getText().equals("")) ? 0 : Integer.parseInt(Total.getText());
        cashDepositBox.send(new Msg.Builder(id, Msg.Type.deposit, cashDepositBox).setAccounts(account).setInAmount(total).build());
        btnConfirmAmount.setDisable(true);
        btnReset.setDisable(true);
        numberOfFiveHundred.setEditable(false);
        NumberOfOneHundred.setEditable(false);
        NumberOfOneThousand.setEditable(false);
    }

    /**
     * @param mouseEvent make all Text field to 0
     */
    public void Reset(MouseEvent mouseEvent) {
        NumberOfOneHundred.setText(String.valueOf(0));
        numberOfFiveHundred.setText(String.valueOf(0));
        NumberOfOneThousand.setText(String.valueOf(0));
        Total.setText(String.valueOf(0));
    }

    /**
     * stop all component when error
     */
    public void stopComponent() {
        NumberOfOneHundred.setEditable(false);
        numberOfFiveHundred.setEditable(false);
        NumberOfOneThousand.setEditable(false);
        Total.setEditable(false);
        btnConfirmAmount.setDisable(true);
        btnReset.setDisable(true);

    }

    public void reset() {
        Platform.runLater(() -> {
            btnConfirmAmount.setDisable(true);
            btnReset.setDisable(true);
            numberOfFiveHundred.setEditable(false);
            NumberOfOneHundred.setEditable(false);
            NumberOfOneThousand.setEditable(false);
            Total.setEditable(false);
            numberOfFiveHundred.setText(String.valueOf(0));
            NumberOfOneHundred.setText(String.valueOf(0));
            NumberOfOneThousand.setText(String.valueOf(0));
            Total.setText(String.valueOf(0));
        });

    }
}
