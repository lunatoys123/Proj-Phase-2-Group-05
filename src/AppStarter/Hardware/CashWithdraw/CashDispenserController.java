package AppStarter.Hardware.CashWithdraw;

import AppMain.ATM_SS;
import AppStarter.common.AppStarter;
import AppStarter.common.CommonUtility;
import AppStarter.common.MBox;
import AppStarter.common.Msg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

public class CashDispenserController {

    public TextField Domain100;
    public TextField Domain500;
    public TextField Domain1000;
    public Label withdrawTime;
    public Button TakeOneThousand;
    public Button TakeFiveHundred;
    public Button TakeOneHundred;
    private String id;
    private AppStarter appStarter;
    private Logger logger;
    private CashDispenser_Emulator cashDispenser_emulator;
    private MBox cashMbox;
    private CommonUtility common;

    public void initialize(String id, AppStarter appStarter, Logger logger, CashDispenser_Emulator cashDispenser_emulator, CommonUtility common) {
        this.id = id;
        this.appStarter = appStarter;
        this.logger = logger;
        this.cashDispenser_emulator = cashDispenser_emulator;
        this.cashMbox = appStarter.getThread("cashDispenser").getMbox();
        this.common = common;
    }

    /**
     * classify the amount into number of each Domain
     * @param amount amount of withdrawal
     */
    public void classify(int amount) {

        int num_of_1000 = amount / 1000;
        amount %= 1000;

        int num_of_500 = amount / 500;
        amount %= 500;

        int num_of_100 = amount / 100;

        Domain100.setText(num_of_100 + "");
        Domain500.setText(num_of_500 + "");
        Domain1000.setText(num_of_1000 + "");

        common.setNumberOfOneThousand(common.getNumberOfOneHundred() - num_of_100);
        common.setNumberOfFiveHundred(common.getNumberOfFiveHundred() - num_of_500);
        common.setNumberOfOneHundred(common.getNumberOfOneThousand() - num_of_1000);

        System.out.println(common.getNumberOfOneHundred());
        System.out.println(common.getNumberOfFiveHundred());
        System.out.println(common.getNumberOfOneThousand());

    }

    public void setText(String text) {
        Platform.runLater(() -> withdrawTime.setText(text));
    }


    /**
     * Action of button press
     * @param actionEvent button event
     */
    public void ButtonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();

        String btnId = btn.getId();

        switch (btnId) {
            case "TakeOneHundred":
                Domain100.setText(String.valueOf(0));
                break;
            case "TakeFiveHundred":
                Domain500.setText(String.valueOf(0));
                break;
            case "TakeOneThousand":
                Domain1000.setText(String.valueOf(0));
                break;
        }
    }

    /**
     * Enable all Component
     */
    public void initial() {
        TakeOneThousand.setDisable(false);
        TakeFiveHundred.setDisable(false);
        TakeOneHundred.setDisable(false);
    }

    /**
     * stop all components when error
     */
    public void stopComponent() {
        TakeOneThousand.setDisable(true);
        TakeFiveHundred.setDisable(true);
        TakeOneHundred.setDisable(true);

        Domain100.setEditable(false);
        Domain500.setEditable(false);
        Domain1000.setEditable(false);

    }

    public int DomainTotal(){
        int TotalForOneHundred=100*common.getNumberOfOneHundred();
        int TotalForFiveHundred=500*common.getNumberOfFiveHundred();
        int TotalForOneThousand=1000* common.getNumberOfOneThousand();
        return TotalForOneHundred+TotalForFiveHundred+TotalForOneThousand;
    }

    /**
     * check the hardware have errors or not
     * @return True of false
     */
    public boolean checkHardware() {
        long total= common.Total();
        if(total< cashDispenser_emulator.minDomainThresHold){
            return false;
        }
        return true;
    }

    public void reset() {
        Platform.runLater(() -> {
            TakeOneThousand.setDisable(true);
            TakeFiveHundred.setDisable(true);
            TakeOneHundred.setDisable(true);
            withdrawTime.setText("");
            Domain100.setEditable(false);
            Domain500.setEditable(false);
            Domain1000.setEditable(false);
        });
    }
}
