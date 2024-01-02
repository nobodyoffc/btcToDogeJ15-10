package tx;

public class NumberTools {
    public static double roundDouble8(double raw){
        long i = 0;
        i = (long) (raw* 100000000);
        return (double) i/ 100000000;
    }

    public static double roundDouble16(double raw){
        long i = 0;
        i = (long) (raw* 100000000 * 100000000);
        return (double) i/(100000000 * 100000000);
    }

    public static double roundDouble4(double raw){
        long i = 0;
        i = (long) (raw*10000);
        double j = (double) i / 10000;
//        System.out.println(raw + " is rounded to "+ j);
        return j;
    }

    public static double roundDouble2(double raw){
        long i = 0;
        i = (long) (raw*100);
        double j = (double) i / 100;
//        System.out.println(raw + " is rounded to "+ j);
        return j;
    }
}
