package test;

public class Test {
    public static void main(String[] args) {
        int[] arr=new int[10];
        for (int i = 0; i < arr.length; i++) {
            arr[i]= m1(i);
        }
        for (int a:arr) {
            System.out.println(a);
        }
        //System.out.println(m1(6));
    }
    public static int m1(int n){
        int a=0,b=1;
        int sum=0;
        while (n>=0){
            if(n==1){
                sum=b;
                break;
            }else if(n==0){
                sum=a;
                break;
            }else {
            sum=a+b;
            a=b;
            b=sum;
            n--;
            }
        }
        return sum;
    }
}
