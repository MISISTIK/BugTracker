package homework;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestBugBunch {
    private static BugBunch bugBunch;


    @Before
    public void initBugBunch() {
        bugBunch = new BugBunch(20,20);

    }

    @Test
    public void testIfLess() {
        System.out.println("--------IF LESS-------");
        int requestToGet = 18;
        System.out.println("requested = " + requestToGet);
        int res = bugBunch.getBugs(requestToGet);
        System.out.println("bugs loaded = " + res);
        System.out.println("bugs left = " + bugBunch.getBugsCount());
        System.out.println("isEmpty? = " + bugBunch.isEmpty());
        assertEquals(18, res);
        System.out.println("----------------------");
        System.out.println();
    }

    @Test
    public void testIfEquals() {
        System.out.println("--------IF EQUALS-------");
        int requestToGet = 20;
        int res = bugBunch.getBugs(requestToGet);
        showInfo(res,requestToGet);
        assertEquals(-20, res);
        System.out.println("----------------------");
        System.out.println();
    }

    @Test
    public void testIfGreater() {
        System.out.println("--------IF GREATER-------");
        int requestToGet = 23;
        int res = bugBunch.getBugs(requestToGet);
        showInfo(res,requestToGet);
        res = bugBunch.getBugs(requestToGet);
        assertEquals(0, res);
        System.out.println("----------------------");
        System.out.println();
    }

    @Test
    public void testIfEmpty() {
        System.out.println("--------IF GREATER-------");
        int requestToGet = 24;
        int res = bugBunch.getBugs(requestToGet);
        res = bugBunch.getBugs(requestToGet);
        showInfo(res,requestToGet);
        assertEquals(0, res);
        System.out.println("----------------------");
        System.out.println();
    }

    private void showInfo(int res,int requestToGet) {
        System.out.println("requested = " + requestToGet);
        System.out.println("bugs loaded = " + res);
        System.out.println("bugs left = " + bugBunch.getBugsCount());
        System.out.println("isEmpty? = " + bugBunch.isEmpty());
    }

}
