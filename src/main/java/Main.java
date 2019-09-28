import Config.GetConfig;
import ShtrafyOnline.ShtrafyOnlineService;
import gibdd.rf.GIBDDService;


public class Main {

    public static void main(String[] args) {

        Long interval = new GetConfig().getPollingInterval() ;
        while (true) {
            try {
                new GIBDDService().gibddService();
                new ShtrafyOnlineService().service();
                Thread.sleep(interval);
            } catch (Exception e){
            }
        }

    }
}
