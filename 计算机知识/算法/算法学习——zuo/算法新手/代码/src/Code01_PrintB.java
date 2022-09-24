/**
 * @program: 代码
 * @description:
 * @author: max-qaq
 * @create: 2022-09-19 16:19
 **/

public class Code01_PrintB {
    /**
     * 打印一个数字的32位
     * @param num
     */
    public static void print(int num){
        for (int i = 31; i >= 0; i--){
            System.out.println((num & (1 << i)) == 0 ? "0" : "1");
        }
    }
}
