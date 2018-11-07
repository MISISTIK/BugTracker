package homework;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JiraTelegaTest {

    private JiraTelega telega;

    @Before
    public void initTelega() {
        telega = new JiraTelega(6);
        telega.putBugs(6);
    }

    @Test
    public void testIfMoreThanMax() {
        int requiredToGet = telega.getMaxLoad() + 1;
        int res = telega.getBugs(requiredToGet);
        assertEquals(telega.getMaxLoad(),res);
    }

    @Test
    public void testIfMore() {
        int requiredToGet = telega.getMaxLoad();
        int minus = 2;
        telega.getBugs(minus);
        int res = telega.getBugs(requiredToGet);
        assertEquals(requiredToGet-minus,res);
    }

    @Test
    public void testIfEquals() {
        int requiredToGet = telega.getMaxLoad();
        int res = telega.getBugs(requiredToGet);
        assertEquals(requiredToGet,res);
    }

    @Test
    public void testIfLess() {
        int requiredToGet = 3;
        int res = telega.getBugs(requiredToGet);
        assertEquals(telega.getMaxLoad()-requiredToGet,res);
    }

}