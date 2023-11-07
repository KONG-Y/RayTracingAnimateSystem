package RayTracing;

public class RayTracing {
    public static final double posInfinity = Double.POSITIVE_INFINITY;//正无穷大
    public static final double negInfinity = Double.NEGATIVE_INFINITY;//负无穷大
    public static final double pi =  3.1415926535897932385;
    //限制在[min,max]区间上
    public double clamp(double x, double min, double max){
        if(x<min)return min;
        if(x>max)return max;
        return x;
    }
    //角度变换为弧度
    public double degreesToRadians(double degrees){
        return degrees*pi/180.0;
    }

    //返回[0,1]间的double随机数
    public double randomDouble(){
        return Math.random();
    }
    //返回[min,max]间的double随机数
    public double randomDouble(double min, double max){
        return (Math.random()*(max-min))+min;
    }
    //返回[min,max]间的int随机数
    public int randomInt(int min, int max){
        return (int)(Math.random()*(max-min))+min;
    }

    public double fmin(double a,double b){
        if(a<b)return a;
        return b;
    }
    public double fmax(double a, double b){
        if(a>b)return a;
        return b;
    }

}
