package com.foo.condition;

/**
 *
 * @ClassName: TestCondition
 * @Description:
 * @Author: tomluo
 * @Date: 2022/12/17 19:55
 **/
public class TestCondition {
    /**
     * 【强制】在一个switch块内，每个case要么通过break/return等来终止，要么注释说明程序将继续执行到哪一个case为止；
     * 在一个switch块内，都必须包含一个default语句并且放在最后，即使空代码。
     */
    public static final int testSwitchCount() {
        switch (1) {
            case 1:
                break;
            case 2:
                return 1;
            default:
                break;
        }
        return 1;
    }

    /**
     * 【强制】在if/else/for/while/do语句中必须使用大括号。即使只有一行代码，避免采用单行的编码方式：
     * if (condition) statements;
     * 【强制】在高并发场景中，避免使用"等于"判断作为中断或退出的条件。
     * 说明：如果并发控制没有处理好，容易产生等值判断被"击穿"的情况，使用大于或小于的区间判断条件来代替。
     *
     * 反例：判断剩余奖品数量等于0时，终止发放奖品，但因为并发处理错误导致奖品数量瞬间变成了负数，这样的话，活动无法终止。
     */
    public void testIf() {

    }

    /**
     * 【推荐】表达异常的分支时，少用if-else方式，这种方式可以改写成：
     *     if (condition) {
     *       ...
     *       return obj;
     *     }
     *     // 接着写else的业务逻辑代码;
     *
     * 说明：如果非得使用if()...else if()...else...方式表达逻辑，【强制】避免后续代码维护困难，请勿超过3层。
     * 正例：超过3层的 if-else 的逻辑判断代码可以使用卫语句、策略模式、状态模式等来实现，其中卫语句示例如下：
     */
    public void testException() {

    }

    public void today() {
        if (isBusy()) {
            System.out.println("change time.");
            return;
        }
        if (isFree()) {
            System.out.println("go to travel.");
            return;
        }
        System.out.println("stay at home to learn Alibaba Java Coding Guidelines.");
        return;
    }

    private boolean isBusy() {
        return false;
    }

    private boolean isFree() {
        return false;
    }
    /**
     * 【推荐】除常用方法（如getXxx/isXxx）等外，不要在条件判断中执行其它复杂的语句，将复杂逻辑判断的结果赋值给一个有意义的布尔变量名，以提高可读性。
     * 说明：很多if语句内的逻辑相当复杂，阅读者需要分析条件表达式的最终结果，才能明确什么样的条件执行什么样的语句，那么，如果阅读者分析逻辑表达式错误呢？
     * 正例：
     * // 伪代码如下 final boolean existed = (file.open(fileName, "w") != null) && (...) || (...);
     * if (existed) {
     *    ...
     * }
     */

    /**
     * 【推荐】循环体中的语句要考量性能，以下操作尽量移至循环体外处理，如定义对象、变量、获取数据库连接，进行不必要的try-catch操作（这个try-catch是否可以移至循环体外）
     */

    /**
     * 【推荐】避免采用取反逻辑运算符。
     * 说明：取反逻辑不利于快速理解，并且取反逻辑写法必然存在对应的正向逻辑写法。
     * 正例：使用if (x < 628) 来表达 x 小于628。
     * 反例：使用if (!(x >= 628)) 来表达 x 小于628。
     */

    /**
     * 【参考】下列情形，需要进行参数校验：
     * 1） 调用频次低的方法。
     * 2） 执行时间开销很大的方法。此情形中，参数校验时间几乎可以忽略不计，但如果因为参数错误导致中间执行回退，或者错误，那得不偿失。
     * 3） 需要极高稳定性和可用性的方法。
     * 4） 对外提供的开放接口，不管是RPC/API/HTTP接口。
     * 5） 敏感权限入口。
     * 【参考】下列情形，不需要进行参数校验：
     * 1） 极有可能被循环调用的方法。但在方法说明里必须注明外部参数检查要求。
     * 2） 底层调用频度比较高的方法。毕竟是像纯净水过滤的最后一道，参数错误不太可能到底层才会暴露问题。一般DAO层与Service层都在同一个应用中，部署在同一台服务器中，所以DAO的参数校验，可以省略。
     * 3） 被声明成private只会被自己代码所调用的方法，如果能够确定调用方法的代码传入参数已经做过检查或者肯定不会有问题，此时可以不校验参数。
     */
    /**
     * 【参考】特殊注释标记，请注明标记人与标记时间。注意及时处理这些标记，通过标记扫描，经常清理此类标记。线上故障有时候就是来源于这些标记处的代码。
     * 1） 待办事宜（TODO）:（ 标记人，标记时间，[预计处理时间]） 表示需要实现，但目前还未实现的功能。这实际上是一个Javadoc的标签，目前的Javadoc还没有实现，但已经被广泛使用。只能应用于类，接口和方法（因为它是一个Javadoc标签）。
     * 2） 错误，不能工作（FIXME）:（标记人，标记时间，[预计处理时间]） 在注释中用FIXME标记某代码是错误的，而且不能工作，需要及时纠正的情况。
     */
}