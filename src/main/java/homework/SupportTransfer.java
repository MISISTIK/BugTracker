package homework;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

public class SupportTransfer implements Runnable {
    private Exchanger<JiraTelega> exSupportUser;
    private Exchanger<JiraTelega> exSupportDev;
    private JiraTelega telega;
    private int initTransferTime;
    private boolean stop;

    public SupportTransfer(int initTransferTime, Exchanger<JiraTelega> exUserSupport, Exchanger<JiraTelega> exSupportDev) {
        this.telega = null;
        this.exSupportUser = exUserSupport;
        this.exSupportDev = exSupportDev;
        this.initTransferTime = initTransferTime;
        stop = false;
        log("SupportTransfer created. Transfer time = " + initTransferTime + " sec");
        new Thread(this).start();
    }

    @Override
    public void run() {
        log("SupportTransfer start working...");
        try {
            while (!stop) {

                log("Waiting for exchange with UserLoader...");
                telega = exSupportUser.exchange(null);
                log("Starting exchange with UserLoader...");
                log("Exchange complete. Jira inst received (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + ")");
                log("Starting to transfer User -> Dev ...");

                for (int i = 0; i < initTransferTime; i++) {
                    log("Transfering ... " + (i + 1) + " sec");
                    TimeUnit.SECONDS.sleep(1);
                }

                log("Waiting for exchange with DevUnloader...");
                exSupportDev.exchange(telega);
                log("Starting exchange with DevUnloader...");
                log("Exchange complete. Jira inst transferred to Dev (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + ")");

                //Transfer middle. Waiting from Dev to receive the Jira

                log("Waiting for exchange with DevUnloader...");
                telega = exSupportDev.exchange(null);
                log("Starting exchange with DevUnloader...");
                log("Exchange complete. Jira inst received (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + ")");
                log("Starting to transfer Dev -> User ...");

                for (int i = 0; i < initTransferTime; i++) {
                    TimeUnit.SECONDS.sleep(1);
                    log("Transfering ... " + (i + 1) + " sec");
                }

                log("Waiting for exchange with UserLoader...");
                exSupportUser.exchange(telega);
                log("Starting exchange with UserLoader...");
                log("Exchange complete. Jira inst transferred to User (" + telega.getCurrentLoad() + " / " + telega.getMaxLoad() + ")");


                if (telega.isLastTelega()) {
                    log("Last transfer from Dev to User of empty Jira instance");
                    stop = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log("SupportTransfer stopped working");
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
