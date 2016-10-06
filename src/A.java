import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by hyx on 2016/9/25.
 */
public class A {

    public static void main(String[] args) {
        int a = 0;
        System.out.print("请输入一个数a:");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            a = Integer.parseInt(br.readLine());
        } catch (IOException ex) {
        }
        int b = 0;
        System.out.print("请输入另一个数b:");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            b = Integer.parseInt(br.readLine());
        } catch (IOException ex) {
        }
        if (a <= 0 || b <= 0)
            System.out.println("你输入的数不合法,将会出现错误!");
        int ab = a * b;
        int r = 0;
        if (a < b) {
            r = a;
            b = a;
            a = r;
        }
        int i = 1;
        while (i != 0) {
            i = a % b;
            a = b;
            b = i;
        }
        System.out.println("你输入的两数的最大公约数为:" + a);
        System.out.println("你输入的两数的最小公倍数为:" + ab / a);
    }

}
