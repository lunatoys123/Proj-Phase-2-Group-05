package AppStarter.BAMS;

import AppStarter.common.AppStarter;
import AppStarter.common.MBox;
import javafx.scene.control.TextArea;

import java.util.logging.Logger;

public class BAMSController {
    public TextArea ServerLog;
    private String id;
    private AppStarter appStarter;
    private BAMS_Emulator bams_emulator;
    private MBox BAMS_Box;
    private Logger logger;

    public void initialize(String id, AppStarter appStarter, Logger logger, BAMS_Emulator bams_emulator) {
        this.id = id;
        this.appStarter = appStarter;
        this.logger = logger;
        this.bams_emulator = bams_emulator;
        this.BAMS_Box = appStarter.getThread("BAMS").getMbox();
    }

    public void showStatus(String Line) {
        ServerLog.appendText(Line + "\n");
    }


}
