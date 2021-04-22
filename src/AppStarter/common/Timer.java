package AppStarter.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Timer extends AppThread {
    private static final String setUpTimerCase = "Set_up_Timer";
    private static int simulationSpeed;
    private static MBox TimerBox;
    private static long SystemCurrentTime;
    private final int tick;
    private final Ticker ticker;
    private final List<ActiveTimer> timerList;


    public Timer(String id, AppStarter appStarter) {
        super(id, appStarter);
        TimerBox = getMbox();
        ticker = new Ticker(TimerBox);
        timerList = new ArrayList<>();
        tick = Integer.parseInt(appStarter.getProperties().getProperty("SecondPerTicks"));
        simulationSpeed = Integer.parseInt(appStarter.getProperties().getProperty("SimulationSpeed"));
    }

    public static int setTimer(String id, MBox mbox, long sleepTime) {
        int timerID = new Random().nextInt(900000) + 100000;
        return setTimer(id, mbox, sleepTime, timerID);
    }

    public static int setTimer(String id, MBox mbox, long sleepingTime, int timerID) {
        TimerBox.send(new Msg.Builder(id, Msg.Type.setupTimer, mbox).setDetails(new TimerMsg(Msg.Type.setupTimer, timerID, sleepingTime).toString()).build());
        return timerID;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(id);
        log.info(id + " Starting");
        SystemCurrentTime = System.currentTimeMillis();
        new Thread(ticker).start();

        boolean quit = false;

        while (!quit) {
            Msg msg = mbox.Receive();

            switch (msg.getType()) {
                case setupTimer:
                    set(msg);
                    //log.info("Set up Timer: running: "+SystemCurrentTime);
                    break;
                case Tick:
                    checkTimeOut();
                    break;
                case Terminate:
                    quit = true;
                    ticker.setQuit(true);
                    break;
            }
        }

        appStarter.UnRegisterThread(this);
        log.info("UnRegister thread: " + id);
    }

    private void checkTimeOut() {
        long currentTime = new Date().getTime();
        //log.info("Checking Time");
        List<ActiveTimer> timerOutTimers = new ArrayList<>();

        for (ActiveTimer timer : timerList) {
            if (timer.timeout(currentTime)) {
                timerOutTimers.add(timer);
            }
        }

        for (ActiveTimer timer : timerOutTimers) {
            int timerId = timer.getTimerID();
            MBox mbox = timer.getMbox();
            //System.out.println(mbox.getId());
            mbox.send(new Msg.Builder("Timer", Msg.Type.TimesUp, null).setDetails(new TimerMsg(Msg.Type.TimesUp, timerId, 0).toString()).build());
            timerList.remove(timer);
        }

    }

    public void set(Msg msg) {

        TimerMsg timerMsg = new TimerMsg(msg.getDetails());

        int timerID = timerMsg.getTimerID();
        long sleepTime = timerMsg.getSleepTime();

        long wakeUpTime = new Date().getTime() + sleepTime;

        String master = msg.getId();
        MBox mbox = msg.getSender();

        timerList.add(new ActiveTimer(timerID, wakeUpTime, master, mbox));
        log.info("set up message: [" + timerID + "]" + " [" + master + "]" + " :" + wakeUpTime);
    }

    public static class ActiveTimer {
        private final int timerID;
        private final long wakeupTime;
        private final String master;
        private final MBox mbox;

        public ActiveTimer(int timerID, long wakeupTime, String master, MBox mbox) {
            this.timerID = timerID;
            this.wakeupTime = wakeupTime;
            this.master = master;
            this.mbox = mbox;
        }

        public int getTimerID() {
            return timerID;
        }

        public long getWakeupTime() {
            return wakeupTime;
        }

        public String getMaster() {
            return master;
        }

        public MBox getMbox() {
            return mbox;
        }

        public boolean timeout(long currentTime) {
            return currentTime > wakeupTime;
        }


    }

    public static class TimerMsg {

        private final String messageHandler;
        private Msg.Type type;
        private final int timerID;
        private final long sleepTime;

        public TimerMsg(Msg.Type type, int timerID, long sleepTime) {
            this.type = type;
            this.timerID = timerID;
            this.sleepTime = sleepTime;

            switch (type) {
                case setupTimer:
                    this.messageHandler = "Set_up_Timer";
                    break;
                case CancelTimer:
                    this.messageHandler = "Cancel Timer";
                    break;
                case TimesUp:
                    this.messageHandler = "Times Up";
                    break;
                default:
                    this.type = Msg.Type.error;
                    throw new RuntimeException("Invaild message: " + this.type);
            }
        }


        public TimerMsg(String details) {
            String[] detailsArray = details.split(" ");

            if (detailsArray.length < 3) {
                throw new RuntimeException("details don't have enough length (3) :" + detailsArray.length);
            }

            this.messageHandler = detailsArray[0];
            switch (messageHandler) {
                case setUpTimerCase:
                    this.type = Msg.Type.setupTimer;
                    break;
                default:
                    this.type = Msg.Type.error;
                    throw new RuntimeException("Invaild message: " + detailsArray[0]);
            }

            this.timerID = Integer.parseInt(detailsArray[1]);
            System.out.println("timerID: " + timerID);

            this.sleepTime = Long.parseLong(detailsArray[2]);
            System.out.println("sleeping Time: " + sleepTime);

        }

        public int getTimerID() {
            return timerID;
        }

        public long getSleepTime() {
            return sleepTime;
        }

        public String toString() {
            return String.format("%s %d %d", this.messageHandler, this.timerID, this.sleepTime);
        }
    }

    public class Ticker implements Runnable {
        private final MBox timerBox;
        private boolean quit = false;

        public Ticker(MBox mbox) {
            this.timerBox = mbox;
        }

        @Override
        public void run() {
            Thread.currentThread().setName("Ticker");
            log.info("Ticker starting");
            while (!quit) {
                try {
                    Thread.sleep(tick);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mbox.send(new Msg.Builder("Ticker", Msg.Type.Tick, null).setDetails("tick").build());
            }
        }

        public void setQuit(boolean quit) {
            this.quit = quit;
        }
    }
}
