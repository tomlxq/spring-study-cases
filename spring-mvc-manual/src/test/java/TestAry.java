
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.Arrays;

public class TestAry {
    @Test
    public void testAry() {
        // List<String> value=Arrays.asList(new String[]{"nilei","hamei","roze"})
        String s = Arrays.toString(new String[]{"nilei", "hamei", "roze"});
        System.out.println(s);
        String v = s.replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");

        System.out.println(v);
        System.out.println(StringUtils.join(Arrays.asList("nilei", "hamei", "roze"), ","));
    }
}
