package homework;

class JiraTelega {
    private int maxLoad;
    private volatile boolean lastTelega;
    private volatile boolean isLoaded;
    private int loaded;

    public void setLoaded() {
        isLoaded = true;
    }

    int getCurrentLoad() {
        return loaded;
    }

    int getMaxLoad() {
        return maxLoad;
    }

    boolean isLoaded() {
        return isLoaded;
    }

    JiraTelega(int maxLoad) {
        this.maxLoad = maxLoad;
        isLoaded = false;
        lastTelega = false;
    }

    void putBugs(int bugs) {
        loaded += bugs;
        if (loaded >= maxLoad) {
            loaded = maxLoad;
            isLoaded = true;
        }
    }

    int getBugs(int bugs) {
        if (isLoaded) {
            if (loaded >= bugs) {
                loaded -= bugs;
                if (loaded == 0) {
                    isLoaded = false;
                }
                return bugs;
            } else {
                int temp = loaded;
                loaded = 0;
                isLoaded = false;
                return temp;
            }

        } else {
            return 0;
        }
    }

    void setLastTelega() {
        lastTelega = true;
    }

    boolean isLastTelega() {
        return lastTelega;
    }

}
