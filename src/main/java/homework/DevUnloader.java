package homework;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class DevUnloader implements Runnable{

    private int initUnloaderSpeed;
    private BugBunch bugBunch;
    private JiraTelega telega;
    private Exchanger<JiraTelega> ex;
    private boolean stop;


    DevUnloader(int initUnloaderSpeed, Exchanger<JiraTelega> ex) {
        this.initUnloaderSpeed = initUnloaderSpeed;
        this.bugBunch = bugBunch;
        this.ex = ex;
        this.telega = null;
        stop = false;
        log("DevUnloader created. Bug solving speed = " + initUnloaderSpeed + " bugs/sec");
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            while (!stop) {
                log("Waiting for exchange with SupportTransfer...");
                telega = ex.exchange(null);
                log("Starting exchange with SupportTransfer...");
                log("Exchange complete. Jira inst received. Starting to solve tickets... (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + " left)");

                while (telega.isLoaded()) {
                    TimeUnit.SECONDS.sleep(1);
                    int solvedBugs = telega.getBugs(initUnloaderSpeed);
                    if (solvedBugs != 0) {
                        log("DevUnloader solves " + solvedBugs + " bugs (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + " left)");
                    } else {
                        log("Nothing to solve. Break");
                        break;
                    }
                }
                log("DevUnloader finishes to fix the bugs. Jira status: "+ telega.getCurrentLoad() + " / " + telega.getMaxLoad() + " tickets left");
                log("Waiting for exchange with SupportTransfer");
                ex.exchange(telega);
                log("Starting exchange with SupportTransfer ... ");
                log("Exchange complete. Jira instance transfered to SupportTransfer.");

                if (telega.isLastTelega()) {

                    //breaks the while loop
                    stop = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log("DevLoader stopped working");
    }

    private void log(String s) {
        System.out.println("[" + getClass().getSimpleName()+ "] " + s);
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
