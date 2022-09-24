/**
 * @program: 代码
 * @description:随机数
 * @author: max-qaq
 * @create: 2022-09-21 10:23
 **/

public class Code03_RandToRand {

    public static void main(String[] args) {
        int testTimes = 1000000;
        int count = 0;
        for(int i = 0; i < testTimes; i++){
            if ( xToXPower2() < 0.7) {
                count++;
            }
        }
        System.out.println((double) count /(double) testTimes);
    }
    //把x出现的概率调整为x的平方
    public static double xToXPower2(){
        return Math.max(Math.random() , Math.random());
    }
    public static int[] randomArray(int maxLen, int maxVal){
        int len = (int) (Math.random() * maxLen);
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++){
            arr[i] = (int) (Math.random() * maxVal);
        }
        return arr;
    }
}
