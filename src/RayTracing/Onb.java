package RayTracing;

public class Onb extends Vector3{
    public Vector3 []axis=new Vector3[3];
    public Onb(){}
    public final Vector3 operator(int i){
        return axis[i];
    }

    public final Vector3 u(){
        return axis[0];
    }

    public final Vector3 v(){
        return axis[1];
    }

    public final Vector3 w(){
        return axis[2];
    }

    public final Vector3 local(double a, double b, double c){
        return u().multiply(a).plus(v().multiply(b)).plus(w().multiply(c));
    }

    public final Vector3 local(final Vector3 a){
        return u().multiply(a.x()).plus(v().multiply(a.y())).plus(w().multiply(a.z()));
    }

    public void buildFromW(final Vector3 n){
        axis[2]=n.unitVector();
        Vector3 a = (Math.abs(w().x())>0.9)?new Vector3(0,1,0):new Vector3(1,0,0);
        axis[1]=w().cross(a).unitVector();
        axis[0]=w().cross(v());
    }

}
