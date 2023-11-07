package RayTracing;

public class SolidColor extends Texture {
    private Vector3 colorValue;
    public SolidColor(){}
    public SolidColor(Vector3 c){
        colorValue =c;
    }
    public SolidColor(double red, double green, double blue){
        colorValue=new Vector3(red, green, blue);
    }

    public final Vector3 value(double u, double v, final Vector3 p){
        return colorValue;
    }
}
