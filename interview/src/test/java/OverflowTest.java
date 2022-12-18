import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OverflowTest {
    @Test
    public void OverFlowDemo() {
        short s = Short.MAX_VALUE;
        short ss = (short)(s + 1);
        log.info("{}", ss);
        int i = Integer.MAX_VALUE;
        int j = i + 1;
        // j will roll over to -2_147_483_648
        log.info("{}", j);

        double d = Double.MAX_VALUE;
        double o = d + 1;
        // o will be Infinity
        log.info("{}", o);
    }
}
