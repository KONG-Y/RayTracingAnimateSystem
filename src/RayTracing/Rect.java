package RayTracing;

public abstract class Rect extends  Hittable{
    public abstract String getType();

    public abstract void setPosition(Vector3 pos);
    public abstract Vector3 getPosition();
    public abstract double getHeight();
    public abstract  double getWidth();
    @Override
    public String getShape(){return "Rect";}


}
