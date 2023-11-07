package RayTracing;

public class Ray  {
    private Vector3 origin;//起点向量
    private Vector3 direction;//方向向量
    private double time =0;//时间点
    public Ray(){}
    public Ray(final Vector3 origin, final Vector3 direction,double time){
        this.origin =origin;
        this.direction =direction;
        if (time!=0){
            this.time =time;
        }
    }
    public void setValue(Ray r){
        origin =r.getOrigin();
        direction =r.getDirection();
        time =r.getTime();
    }
    public final Vector3 getOrigin(){
        return origin;
    }

    public final Vector3 getDirection(){
        return direction;
    }

    public final double getTime(){
        return time;
    }

    //光线方向
    public final Vector3 at(double t){
        return origin.plus(direction.multiply(t));//光线公式
    }
}
