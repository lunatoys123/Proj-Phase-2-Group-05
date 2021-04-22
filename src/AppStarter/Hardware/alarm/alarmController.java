package AppStarter.Hardware.alarm;

import AppStarter.common.AppStarter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

public class alarmController {

    private String id;
    private AppStarter appStarter;
    private Logger logger;
    private alarm_Emulator alarm_emulator;
    @FXML
    private TextArea alarmLog;

    public void initialize(String id,AppStarter appStarter,Logger logger, alarm_Emulator alarm_emulator){
        this.id=id;
        this.appStarter=appStarter;
        this.logger=logger;
        this.alarm_emulator=alarm_emulator;
    }

    /**
     * Perform countdown action in alarm
     * @param text receive text
     */
    public void appendAlarm(String text){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                alarmLog.appendText(text+"\n");
            }
        });

    }
}
