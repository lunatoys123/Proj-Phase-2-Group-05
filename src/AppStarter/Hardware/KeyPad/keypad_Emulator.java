package AppStarter.Hardware.KeyPad;

import AppMain.ATM_SS_Starter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class keypad_Emulator extends Keypad {
    private final String id;
    private final ATM_SS_Starter atm_ss_starter;
    private Stage mystage;
    private KeypadController keypadController;

    public keypad_Emulator(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.id = id;
        this.atm_ss_starter = atm_ss_starter;
    }

    public void start() throws IOException {
        Parent root;
        mystage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "Keypad.fxml";
        loader.setLocation(keypad_Emulator.class.getResource(fxmlName));
        root = loader.load();
        keypadController = loader.getController();
        keypadController.initialize(id, atm_ss_starter, log, this);
        mystage.initStyle(StageStyle.DECORATED);
        mystage.setScene(new Scene(root, 285, 256));
        mystage.setTitle("KeyPad");
        mystage.setResizable(false);
        mystage.setOnCloseRequest((WindowEvent event) -> {
            atm_ss_starter.stopApp();
            Platform.exit();
        });
        mystage.show();
    }

    public void setState(boolean state) {
        keypadController.setState(state);
    }

    public void closeWindow() {
        keypadController.closeWindow();
    }

    public void stopComponent(){keypadController.stopComponent();}


}
