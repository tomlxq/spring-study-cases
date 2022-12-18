import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsIterableContaining.hasItems;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * 抽象类命名使用 Abstract 或 Base 开头
 * 异常类命名使用 Exception 结尾；
 * 测试类命名以它要测试的类的名称开始，以 Test 结尾。
 */
public class GenericDemoTest {

    public final static String UNIT_PREFIX = "ENTERPRISE_TOM_UNIT_TEST_";

    public void test() {
        List list = new LinkedList();
        list.add(new Integer(1));
        Integer i = (Integer)list.iterator().next();

    }

    public void test2() {
        List<Integer> list = new LinkedList();
        list.add(new Integer(1));
        Integer i = list.iterator().next();
    }

    @Test
    public void givenArrayOfIntegers_thanListOfStringReturnedOK() {
        Integer[] intArray = {1, 2, 3, 4, 5};
        List<String> stringList = Generics.fromArrayToList(intArray, Object::toString);

        assertThat(stringList, hasItems("1", "2", "3", "4", "5"));
    }

    public static void main(String[] args) {
        // 缩进4个空格
        String say = "hello";
        // 运算符的左右必须有一个空格
        int flag = 0;
        // 关键词if与括号之间必须有一个空格，括号内的f与左括号，0与右括号不需要空格
        if (flag == 0) {
            System.out.println(say);
        }
        // 左大括号前加空格且不换行；左大括号后换行
        if (flag == 1) {
            System.out.println("world");
            // 右大括号前换行，右大括号后有else，不用换行
        } else {
            System.out.println("ok");
            // 在右大括号后直接结束，则必须换行
        }
    }
}
