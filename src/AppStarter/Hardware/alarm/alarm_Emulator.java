package AppStarter.Hardware.alarm;

import AppMain.ATM_SS_Starter;
import AppStarter.common.AppStarter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class alarm_Emulator extends alarm{

    private String id;
    private ATM_SS_Starter atm_ss_starter;
    private Stage MyStage;
    private alarmController alarmController;

    public alarm_Emulator(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.id=id;
        this.atm_ss_starter=atm_ss_starter;

    }

    public void start() throws IOException {
        Parent root;
        MyStage=new Stage();
        String fxml="alarm.fxml";
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(alarm_Emulator.class.getResource(fxml));
        root=loader.load();
        alarmController=loader.getController();
        alarmController.initialize(id,atm_ss_starter,log,this);
        MyStage.initStyle(StageStyle.DECORATED);
        MyStage.setScene(new Scene(root,354,400));
        MyStage.setResizable(false);
        MyStage.setTitle("Alarm");
        MyStage.setOnCloseRequest((WindowEvent event)->{
            atm_ss_starter.stopApp();
            Platform.exit();
        });
        MyStage.show();
    }

    public void appendAlarm(String text){
        alarmController.appendAlarm(text);
    }

}
