package AppStarter.Hardware.CardReader;

import AppStarter.common.AppStarter;
import AppStarter.common.MBox;
import AppStarter.common.Msg;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

public class CardController {

    public TextArea Card_MessagePane;
    public Button btnCardInsert;
    private String id;
    private Logger logger;
    private MBox cardReaderBox;
    @FXML
    private TextField card_number_TextField;
    private CardReader_Emulator cardReader_emulator;

    public void initialize(String id, AppStarter appStarter, Logger logger, CardReader_Emulator cardReader_emulator) {
        this.id = id;
        this.logger = logger;
        this.cardReaderBox = appStarter.getThread("Card_reader").getMbox();
        this.cardReader_emulator=cardReader_emulator;
    }

    /**
     * Determine the action after button press in CardReader.fxml
     * @param actionEvent button event
     */
    public void buttonPressed(ActionEvent actionEvent) {
        if(card_number_TextField.getText().equalsIgnoreCase("")){
            Card_MessagePane.appendText("card Number cannot be empty \n");
            return;
        }
        Button btn = (Button) actionEvent.getSource();
        if ("Card Insert".equals(btn.getText())) {
            logger.info("card insert button pressed");
            cardReaderBox.send(new Msg.Builder(id, Msg.Type.insertCard, cardReaderBox).setCardNum(card_number_TextField.getText()).setDetails("Card Insert").build());
            Card_MessagePane.appendText("Card Inserted: " + card_number_TextField.getText() + "\n");
        }
    }

    /**
     * @param text add Message to cardReader
     */
    public void addMessage(String text) {
        Platform.runLater(() -> {
            if(!cardReader_emulator.ejectCard){
                Card_MessagePane.appendText(text);
                cardReader_emulator.ejectCard=true;
            }

        });

    }

    /**
     * disable insert button
     */
    public void disableButton(){
        btnCardInsert.setDisable(true);
    }


    /**
     * stop component when Error
     */
    public void stopComponent() {
        btnCardInsert.setDisable(true);
        card_number_TextField.setEditable(false);
    }

    public void restart() {
        card_number_TextField.setText("");
        btnCardInsert.setDisable(false);
    }
}
