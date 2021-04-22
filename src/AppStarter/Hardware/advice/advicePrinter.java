package AppStarter.Hardware.advice;

import AppStarter.Hardware.HardwareHandler;
import AppStarter.common.AppStarter;
import AppStarter.common.Msg;

public class advicePrinter extends HardwareHandler {

    protected int numOfPapers;
    public advicePrinter(String id, AppStarter appStarter) {
        super(id, appStarter);
        this.numOfPapers=Integer.parseInt(appStarter.getProperties().getProperty("numberOfPapers"));
    }

    public void processMsg(Msg msg){
        switch (msg.getType()){
            case print_advice:
                if(msg.getAction().equalsIgnoreCase("Account Inquiry")) {
                    printadvice(numOfPapers, msg.getCardnum(), msg.getAccounts(), msg.getAccount_Amount());
                }else if(msg.getAction().equalsIgnoreCase("withdraw")){
                    printadviceForTransition(numOfPapers,msg.getAccounts(),msg.getAccount_Amount(),msg.getOutAmount(),msg.getAction());
                }else if(msg.getAction().equalsIgnoreCase("deposit")){
                    printadviceForTransition(numOfPapers,msg.getAccounts(),msg.getAccount_Amount(),msg.getInAmount(),msg.getAction());
                }else if(msg.getAction().equalsIgnoreCase("afterTransfer")){
                    printadviceForTransfer(numOfPapers,msg.getCardnum(),msg.getFromAccount(),msg.getOutAccount(),msg.getTransferAmount(),msg.getAction());
                }
                numOfPapers--;
                break;
        }
    }

    protected void printadvice(int numOfPapers,String cardnum,String account,double amount){}
    protected void printadviceForTransition(int numOfPapers,String account,int account_amount,int outAmount,String action){}
    protected void printadviceForTransfer(int numOfPapers,String cardID,String FromAccount,String outAccount,int TransferAmount,String action){}




}
