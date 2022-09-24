import java.util.StringJoiner;

/**
 * @program: 代码
 * @description:
 * @author: max-qaq
 * @create: 2022-09-19 16:55
 **/

public class Code02_SimpleSort {
    public static void main(String[] args) {
        int[] nums = new int[]{7,8,9,6,2,1,3,6,4,8,2,1};
        print(nums);
        insertSort(nums);
        print(nums);
    }
    public static void print(int[] nums){
        StringJoiner sj = new StringJoiner(",");
        for (int num : nums){
            sj.add(String.valueOf(num));
        }
        System.out.println(sj.toString());
    }
    public static void selectSort(int[] nums){
        //选择排序
        for (int i = 0; i < nums.length; i++){
            int minIndex = i;
            for (int j = i + 1; j < nums.length; j++){
                if (nums[j] < nums[minIndex]) minIndex = j;
            }
            swap(nums,i,minIndex);
        }
    }
    public static void bubbleSort(int[] nums){
        //冒泡排序
        boolean isSorted = true;//判断是否已经排序
        for (int end = nums.length - 1; end >= 0; end--){
            //一共N-1轮，每次都确定end值是最大的，end前移
            for (int start = 0; start < end; start++){
                //每一轮都是从0~end交换值
                if (nums[start] > nums[start + 1]){
                    swap(nums,start,start + 1);
                    isSorted = false;
                }
            }
            //如果没交换过，直接break
            if (isSorted) break;
        }
    }
    public static void insertSort(int[] nums){
        //插入排序
        for (int end = 1; end < nums.length; end++){
            int newEndIndex = end;
            //把小的一直向前移
            while (newEndIndex - 1 >= 0 && nums[newEndIndex] < nums[newEndIndex - 1]){
                swap(nums,newEndIndex,newEndIndex - 1);
                newEndIndex--;
            }
        }
    }
    public static void swap(int[] nums, int l, int r){
        int t = nums[l];
        nums[l] = nums[r];
        nums[r] = t;
    }

}
