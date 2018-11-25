/*二、给定一个正整数，给出消除重复数字以后最大的整数

        2.1 题目描述

        给定一个正整数，给出消除重复数字以后最大的整数，注意需要考虑长整数。

        输入示例：423234
        输出示例：432
        2.2 代码实现*/

import java.util.Scanner;

public class GetMaxNotRepeatNum {
    //第二题
    public static void solve2(String s) {
        char[] a = new char[10];
        StringBuilder sbBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i) - '0';
            System.out.println("index:" + index + ",s.charAt(i):" + s.charAt(i) + "　a[index]" + a[index]);
            if (a[index] == 0) {
                a[index] = 1;
                sbBuilder.append(s.charAt(i));
            } else if (a[index] == 1) {//如果这个数字已经存在，则取其中大的数保存
                StringBuilder string = new StringBuilder(sbBuilder.toString());
                long m = Long.parseLong(string.toString());
                int temp = string.indexOf(index + "");
                string = string.deleteCharAt(temp);
                string.append(s.charAt(i));
                long n = Long.parseLong(string.toString());
                System.out.println("m:" + m + ",n:" + n);
                if (m < n) {
                    sbBuilder = string;
                }
            }
        }
        System.out.print(sbBuilder.toString());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        solve2(string);
    }
}