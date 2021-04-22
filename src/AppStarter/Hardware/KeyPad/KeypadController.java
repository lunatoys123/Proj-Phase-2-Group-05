package AppStarter.Hardware.KeyPad;

import AppStarter.common.AppStarter;
import AppStarter.common.MBox;
import AppStarter.common.Msg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.util.logging.Logger;

public class KeypadController {
    private String id;
    private AppStarter appStarter;
    private Logger logger;
    private keypad_Emulator keypad_emulator;
    private MBox keypadMbox;
    private boolean state;

    public void initialize(String id, AppStarter appStarter, Logger logger, keypad_Emulator keypad_emulator) {
        this.id = id;
        this.appStarter = appStarter;
        this.logger = logger;
        this.keypad_emulator = keypad_emulator;
        this.keypadMbox = appStarter.getThread("Keypad").getMbox();
        this.state = false;
    }


    public void ButtonPressed(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        logger.info("Button pressed: " + btn.getText());
        switch (btn.getText()) {
            case "Enter":
                if (state) {
                    keypadMbox.send(new Msg.Builder(id, Msg.Type.Enter, keypadMbox).setDetails("Press Enter").build());
                }
                break;
            case "Delete":
                if(state){
                    keypadMbox.send(new Msg.Builder(id,Msg.Type.Delete,keypadMbox).build());
                }
                break;
            case "Reset":
                if(state){
                    keypadMbox.send(new Msg.Builder(id,Msg.Type.Reset_key,keypadMbox).build());
                }
                break;
            case "Cancel":
                    break;
            default:
                if (state) {
                    keypadMbox.send(new Msg.Builder(id, Msg.Type.keyPressed, keypadMbox).setKey(btn.getText()).build());
                }
                break;
        }
    }

    /**
     * allow keypad send Message when state is true
     * @param state true of false
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * exit the fxml
     */
    public void closeWindow() {
        Platform.exit();
    }

    /**
     *stop all components when having error
     */
    public void stopComponent() {
        state=false;
    }
}
