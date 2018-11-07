package homework;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class UserLoader implements Runnable {
    private int initLoaderSpeed;
    private JiraTelega telega;
    private BugBunch bugBunch;
    private Exchanger<JiraTelega> ex;
    private boolean stop;

    UserLoader(int initLoaderSpeed, JiraTelega telega, BugBunch bugBunch, Exchanger<JiraTelega> ex) {
        this.telega = telega;
        this.bugBunch = bugBunch;
        this.stop = false;
        this.initLoaderSpeed = initLoaderSpeed;
        this.ex = ex;
        log("UserLoader created. BugTracking speed = " + initLoaderSpeed + " bugs/sec");
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            log("UserLoader start working...");
            while (!stop) {
                while (!telega.isLoaded()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    log("UserLoader starting to retreiving bugs from BugBunch (" + bugBunch.getBugsCount() + " / " + bugBunch.getMaxBugsCount() + " left)");
                    int bugsRetrieved = bugBunch.getBugs(initLoaderSpeed);
                    if (bugsRetrieved > 0) {
                        log("UserLoader get " + bugsRetrieved + " bugs from BugBunch. (" + bugBunch.getBugsCount() + " / " + bugBunch.getMaxBugsCount() + " left)");
                        telega.putBugs(bugsRetrieved);
                        log("UserLoader put " + bugsRetrieved + " bugs to Jira. (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + ")");
                        if (bugBunch.isEmpty()) {
                            log("BugBunch is empty. No bugs! (unreal o_O)");
                            telega.setLastTelega();
                        }
                    } else if (bugsRetrieved == 0) {
                        log("BugBunch is empty. No bugs! (unreal o_O)");
                        telega.setLoaded();
                        telega.setLastTelega();
                        break;
                    } else {
                        bugsRetrieved = Math.abs(bugsRetrieved);
                        log("UserLoader get " + bugsRetrieved + " bugs from BugBunch. (" + bugBunch.getBugsCount() + " / " + bugBunch.getMaxBugsCount() + ") left");
                        telega.putBugs(bugsRetrieved);
                        telega.setLoaded();
                        telega.setLastTelega();
                        log("UserLoader put " + bugsRetrieved + " bugs to Jira. (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + ")");
                        log("BugBunch is empty. No bugs! (unreal o_O)");
                        break;
                    }
                }
                log("Jira is loaded (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + ")");
                log("Waiting for exchange with SupportTransfer...");
                ex.exchange(telega);
                log("Starting exchange with SupportTransfer...");
                log("Exchange with SupportTransfer complete. Jira transfered to SupportTransfer");

                log("Waiting to retreive the Jira from SupportTransfer...");
                telega = ex.exchange(null);
                log("Starting exchange with SupportTransfer...");
                log("Exchange with SupportTransfer complete. Jira retrieved from SupportTransfer");

                if (telega.isLastTelega()) {
                    //breaks the while loop
                    log("No bugs left in BugBunch. Users go home, Devs go to the bar.");
                    stop = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log("UserLoader stopped working");
    }

    private void log(String s) {
        System.out.println("[" + getClass().getSimpleName() + "] " + s);
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
