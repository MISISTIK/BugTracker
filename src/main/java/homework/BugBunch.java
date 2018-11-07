package homework;

import java.util.concurrent.TimeUnit;

public class BugBunch {

    private int maxBugsCount;
    private int bugsCount;
    private boolean isEmpty;

    BugBunch(int initBugsCount,int maxBugsCount) {
        this.maxBugsCount = maxBugsCount;
        this.bugsCount = initBugsCount;
        this.isEmpty = false;
        log("BugBunch created. Current bugs = " + bugsCount + " | Max = " + maxBugsCount);
    }

    public int getMaxBugsCount() {
        return maxBugsCount;
    }

    int getBugsCount() {
        return bugsCount;
    }

    boolean isEmpty() {
        return isEmpty;
    }

    int getBugs(int count) {
        if (!isEmpty) {
            if (bugsCount >= count) {
                bugsCount -= count;
                if (bugsCount == 0) {
                    isEmpty = true;
                    return (-1)*count;
                }
                return count;
            } else {
                int temp = bugsCount;
                bugsCount = 0;
                isEmpty = true;
                return temp;
            }
        } else {
            return 0;
        }
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
