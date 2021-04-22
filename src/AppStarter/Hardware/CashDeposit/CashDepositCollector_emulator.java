package AppStarter.Hardware.CashDeposit;

import AppMain.ATM_SS_Starter;
import AppStarter.common.CommonUtility;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class CashDepositCollector_emulator extends CashDepositCollector {

    private final String id;
    private final ATM_SS_Starter atm_ss_starter;
    private Stage MyStage;
    private CashDepositController cashDepositController;
    private final CommonUtility common;

    public CashDepositCollector_emulator(String id, ATM_SS_Starter atm_ss_starter, CommonUtility common) {
        super(id, atm_ss_starter);
        this.id = id;
        this.atm_ss_starter = atm_ss_starter;
        this.common = common;
    }

    public void start() throws IOException {
        Parent root;
        MyStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxml = "Deposit.fxml";
        loader.setLocation(CashDepositCollector_emulator.class.getResource(fxml));
        root = loader.load();
        cashDepositController = loader.getController();
        cashDepositController.initialize(id, atm_ss_starter, log, this, common);
        MyStage.initStyle(StageStyle.DECORATED);
        MyStage.setScene(new Scene(root, 471, 321));
        MyStage.setTitle("Cash Deposit");
        MyStage.setResizable(false);
        MyStage.setOnCloseRequest((WindowEvent event) -> {
            atm_ss_starter.stopApp();
            Platform.exit();
        });

        MyStage.show();
    }

    public void initial(String account) {
        cashDepositController.Initial(account);
    }
    public void stopComponent(){cashDepositController.stopComponent();}
    public void reset(){cashDepositController.reset();}
}
