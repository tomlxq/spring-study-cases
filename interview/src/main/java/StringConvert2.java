import java.util.Scanner;

/*
实现2-62进制任意两种进制之间的转换

3.1 题目描述

将一个处于Integer类型聚会范围内的整数从指定源进制转换成指定目标进制；可指定的进制值范围为[2-62]；每个数字的可聚会范围为[0-9a-zA-Z];输出字符串的每一个都须为有效值，反例：”012”的百位字符为无效值，实现时无需考虑非法输入。

输入描述：源进制 目标进制 待转换的整数值
输入示例：8 16 12345670
输出示例：29cbb8
3.2 题目分析

        先将进制值与字符值、字符值与进制值之间的转换函数写好；
        两种进制之间的转换，一般是先将源进制转换成十进制，然后再转换成新进制；
        另外需要注意输出是否为有效值。
        3.3 代码实现

        由于本人能力有限，写出的代码只能通过65%，希望大家有全通过的代码，可以分享一下，大家共同进步。
*/
public class StringConvert2 {
    public static int toInt(char c) {//将字符值转换成进制值
        int result = 0;
        if (c >= '0' && c <= '9') {
            result = c - '0';
        } else if (c >= 'a' && c <= 'z') {
            result = c - 'a' + 10;
        } else if (c >= 'A' && c <= 'Z') {
            result = c - 'A' + 36;
        }
        return result;
    }

    public static char toChar(int i) {//将进制值转换成字符值
        int result = 0;
        if (i <= 9) {
            result = i + 48;
        } else if (i <= 35) {
            result = i + 87;
        } else if (i <= 61) {
            result = i + 29;
        }
        return (char) result;
    }

    public static String toTen(String s, int o) {//将原进制转换成十进制
        char[] array = s.toCharArray();
        Long sum = 0l;
        for (int i = 0; i < array.length; i++) {
            //System.out.print(toInt(array[array.length-1-i])+" ");
            sum = (long) (sum + (toInt(array[array.length - 1 - i])) * Math.pow(o, i));
        }
        String result = sum + "";
        return result;
    }

    public static String toNew(String s, int n) {//将十进制转换成新进制
        Long num = Long.parseLong(s);
        StringBuilder sBuilder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        while (num > 0) {
            int temp = (int) (num % n);
            sBuilder.append(toChar(temp));
            num = num / n;
        }
        byte flag = 0;
        for (int i = 0; i < sBuilder.length(); i++) {
            if (flag == 0 && sBuilder.charAt(sBuilder.length() - 1 - i) == '0') {
                break;
            } else {
                flag = 1;
                result.append(sBuilder.charAt(sBuilder.length() - 1 - i));
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int o = scanner.nextInt();
        int n = scanner.nextInt();
        String num = scanner.next();
        String string = toTen(num, o);
        //System.out.println(string);
        string = toNew(string, n);
        System.out.println(string);
    }

    //提示：大家应该注意到题目中的Integer类型，需要考虑负整数的情况，之前就是因为没有考虑负整数的，所以只通过65%。
}
