package AppStarter.common;

public class CommonUtility {

    private int numberOfOneHundred;
    private int numberOfFiveHundred;
    private int numberOfOneThousand;

    public CommonUtility(AppStarter appStarter) {
        this.numberOfOneHundred = Integer.parseInt(appStarter.getProperties().getProperty("numberOfOneHundred"));
        this.numberOfFiveHundred = Integer.parseInt(appStarter.getProperties().getProperty("numberOfFiveHundred"));
        this.numberOfOneThousand = Integer.parseInt(appStarter.getProperties().getProperty("numberOfOneThousand"));
    }

    public int getNumberOfFiveHundred() {
        return numberOfFiveHundred;
    }

    public void setNumberOfFiveHundred(int numberOfFiveHundred) {
        this.numberOfFiveHundred = numberOfFiveHundred;
    }

    public int getNumberOfOneHundred() {
        return numberOfOneHundred;
    }

    public void setNumberOfOneHundred(int numberOfOneHundred) {
        this.numberOfOneHundred = numberOfOneHundred;
    }

    public int getNumberOfOneThousand() {
        return numberOfOneThousand;
    }

    public void setNumberOfOneThousand(int numberOfOneThousand) {
        this.numberOfOneThousand = numberOfOneThousand;
    }

    public int Total(){
        return 100* numberOfOneHundred+500*numberOfFiveHundred+1000*numberOfOneThousand;
    }
}
