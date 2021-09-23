public class Test {

    public static void main(String[] args) {

        a();


    }

    public  static void a(){
        System.out.println((1.0/0)+"****");//抛异常
//        double d = 3.0-0.2;
//        System.out.println(d);//
        double d=3.0;
        double d2=2;
        double r = d / d2;
        System.out.println(r);

    }
}
