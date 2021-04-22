package AppStarter.Hardware.alarm;

import AppMain.ATM_SS;
import AppMain.ATM_SS_Starter;
import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.Msg;
import AppStarter.common.Timer;
//import jdk.jfr.internal.tool.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class alarm extends HardwareHandler {
    private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private int waitSeconds;
    protected int alarmGap;
    protected String state;
    public alarm(String id, ATM_SS_Starter atm_ss_starter) {
        super(id, atm_ss_starter);
        this.alarmGap=Integer.parseInt(appStarter.getProperties().getProperty("alarmTimeGap"));
        this.waitSeconds=Integer.parseInt(appStarter.getProperties().getProperty("alarmwaitSeconds"));
    }

    public void processMsg(Msg msg){
        switch (msg.getType()){
            case alarm:
                //log.info("alarming");
                this.state=msg.getAction();
                appendAlarm(msg.getDetails());
                Timer.setTimer(id,mbox,alarmGap);
                break;
            case TimesUp:
                log.info("Times up for alarm");
                //System.out.println("114514");
                playSound("src/AppStarter/Hardware/alarm/beep.wav");
                waitSeconds--;

                if(waitSeconds==0){
                    waitSeconds=Integer.parseInt(appStarter.getProperties().getProperty("alarmwaitSeconds"));

                }else{
                    Timer.setTimer(id,mbox,alarmGap);
                    appendAlarm("Alarm remain: "+waitSeconds+" seconds");
                }

                break;
        }
    }

    void playSound(String filename){

        String strFilename = filename;

        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }

    protected void appendAlarm(String text){}
}
