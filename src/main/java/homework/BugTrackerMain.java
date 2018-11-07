package homework;

import java.util.concurrent.Exchanger;

public class BugTrackerMain {
    static int initBugCount = 20;
    static int maxBugCount = 20;
    static int initJiraMaxLoad = 6;
    static int initLoaderSpeed = 2;
    static int initUnloaderSpeed = 3;
    static int initTransferTime = 3;

    public static void main(String[] args) {
        Exchanger<JiraTelega> exSupportUser = new Exchanger<>();
        Exchanger<JiraTelega> exSupportDev = new Exchanger<>();
        JiraTelega jira = new JiraTelega(initJiraMaxLoad);
        BugBunch bugBunch = new BugBunch(initBugCount,maxBugCount);
        new SupportTransfer(initTransferTime,exSupportUser,exSupportDev);
        new UserLoader(initLoaderSpeed, jira, bugBunch, exSupportUser);
        new DevUnloader(initUnloaderSpeed, exSupportDev);


    }
}
