package AppStarter.Hardware.CashWithdraw;

import AppMain.ATM_SS;
import AppMain.ATM_SS_Starter;
import AppStarter.common.CommonUtility;
import AppStarter.common.Msg;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class CashDispenser_Emulator extends CashDispenser {
    private final int Width = 600;
    private final int height = 400;
    private final String id;
    private final ATM_SS_Starter atm_ss_starter;
    private Stage MyStage;
    private CashDispenserController cashDispenserController;
    private final CommonUtility common;

    public CashDispenser_Emulator(String id, ATM_SS_Starter atm_ss_starter, CommonUtility common) {
        super(id, atm_ss_starter);
        this.id = id;
        this.atm_ss_starter = atm_ss_starter;
        this.common = common;
    }

    public void start() throws IOException {
        Parent root;
        MyStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxml = "withdraw.fxml";
        loader.setLocation(CashDispenser_Emulator.class.getResource(fxml));
        root = loader.load();
        cashDispenserController = loader.getController();
        cashDispenserController.initialize(id, atm_ss_starter, log, this, common);
        MyStage.initStyle(StageStyle.DECORATED);
        MyStage.setScene(new Scene(root, Width, height));
        MyStage.setTitle("Cash Dispenser");
        MyStage.setResizable(false);
        MyStage.setOnCloseRequest((WindowEvent event) -> {
            atm_ss_starter.stopApp();
            Platform.exit();
        });
        MyStage.show();

    }

    public void reloadPage(String fxml) {
        CashDispenser_Emulator cashDispenser_emulator = this;

        Platform.runLater(() -> {

            try {
                Parent root;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(CashDispenser_Emulator.class.getResource(fxml));
                root = loader.load();
                cashDispenserController = loader.getController();
                cashDispenserController.initialize(id, atm_ss_starter, log, cashDispenser_emulator, common);
                MyStage.setScene(new Scene(root, Width, height));

                cashDispenserController.classify(outAmount);
                cashDispenserController.initial();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public void reset(){cashDispenserController.reset();}
    public void setTime(String text) {
        cashDispenserController.setText(text);
    }
    public void stopComponent(){cashDispenserController.stopComponent();}
    public void checkHardware(){
        System.out.println(cashDispenserController.checkHardware());
       if(!cashDispenserController.checkHardware()){
           ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.Error,mbox).setError("Cash Dispenser don't have enough money for This ATM")
                   .setFilename("Error.fxml").setFatal(true).build());
       }else{
           ATMSS_BOX.send(new Msg.Builder(id, Msg.Type.pollAck, mbox).setDetails("Poll Acknowledgement").build());
       }
    }


}
