import java.util.Scanner;

/**
 * 一、写一个转换字符串的函数
 * <p>
 * 1.1 题目描述
 * <p>
 * 将输入字符串中下标为偶数的字符连成一个新的字符串输出，需要注意两点：
 * 1. 如果输入字符串的长度超过20，则转换失败，返回“ERROR！”字符串；
 * 2. 输入字符串只能由0-9数字，小写a-z和大写A-Z组成，如果包含其他字符，则转换失败，返回“ERROR！”字符串。
 */
public class StringConvert {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        if (string.length() > 20) {
            System.out.print("ERROR!");
            return;
        }
        char[] str = string.toCharArray();
        char[] output = new char[(str.length + 1) / 2];
        ConvertStr(str, output);
        for (int i = 0; i < output.length; i++) {
            System.out.print(output[i]);
        }
    }

    public static void ConvertStr(char[] str, char[] output) {
        for (int i = 0; i < str.length; i++) {
            if (!isValid(str[i])) {
                System.out.println("ERROR!");
            }
            if (i % 2 == 0) {
                output[i / 2] = str[i];
            }
        }
    }

    public static boolean isValid(char c) {
        Character character = c;
        return (character.isAlphabetic(c) || character.isDigit(c));
    }
}
