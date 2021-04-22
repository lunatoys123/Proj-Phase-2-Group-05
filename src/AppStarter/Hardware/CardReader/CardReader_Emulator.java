package AppStarter.Hardware.CardReader;

import AppMain.ATM_SS_Starter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class CardReader_Emulator extends CardReader{
    private String id;
    private ATM_SS_Starter atm_ss_starter;
    private Stage myStage;
    private CardController cardController;


    public CardReader_Emulator(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.atm_ss_starter=atm_ss_starter;
        this.id=id;
    }

    public void start() throws IOException {
        Parent root;
        myStage=new Stage();
        FXMLLoader loader=new FXMLLoader();
        String fxmlName="CardReader.fxml";
        loader.setLocation(CardReader_Emulator.class.getResource(fxmlName));
        root=loader.load();
        cardController=loader.getController();
        cardController.initialize(id,atm_ss_starter,log,this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root,320,400));
        myStage.setTitle("Card Reader");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event)->{
            atm_ss_starter.stopApp();
            Platform.exit();
        });
        myStage.show();

    }

    public void addMessage(String text){
        cardController.addMessage(text);
    }

    public void disableButton(){cardController.disableButton();}

    public void stopComponent(){
        cardController.stopComponent();
    }
    public void restart(){cardController.restart();}
}
