package AppStarter.BAMS;

import AppMain.ATM_SS_Starter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class BAMS_Emulator extends BAMS {

    private final String id;
    private final ATM_SS_Starter atm_ss_starter;
    private Stage stage;
    private BAMSController bamsController;

    public BAMS_Emulator(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.id = id;
        this.atm_ss_starter = atm_ss_starter;
    }

    /**
     * @throws IOException
     * start the fxml
     */
    public void start() throws IOException {
        Parent root;
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String FXMLName = "BAMS.fxml";
        loader.setLocation(BAMS_Emulator.class.getResource(FXMLName));
        root = loader.load();
        bamsController = loader.getController();
        bamsController.initialize(id, atm_ss_starter, log, this);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("BAMS");
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent event) -> {
            atm_ss_starter.stopApp();
            Platform.exit();
        });
        stage.show();


    }

    /**
     * @param Line text to append to BAMS log
     *  add text to BAMS log
     */
    public void showStatus(String Line) {
        bamsController.showStatus(Line);
    }
}
