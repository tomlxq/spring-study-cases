public class TestString_StringBuffer_StringBuilder {
    /**
     * String,StringBuffer, StringBuilder 的区别是什么？String为什么是不可变的？
     * 答： 1、String是字符串常量，StringBuffer和StringBuilder都是字符串变量。
     * 后两者的字符内容可变，而前者创建后内容不可变。
     * 2、String不可变是因为在JDK中String类被声明为一个final类。
     * 3、StringBuffer是线程安全的，而StringBuilder是非线程安全的。
     * ps：线程安全会带来额外的系统开销，所以StringBuilder的效率比StringBuffer高。
     * 如果对系统中的线程是否安全很掌握，可用StringBuffer，在线程不安全处加上关键字Synchronize。
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
