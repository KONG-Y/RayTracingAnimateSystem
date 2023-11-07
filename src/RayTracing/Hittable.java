package RayTracing;

public abstract class Hittable extends RayTracing{
    public abstract boolean hit(final Ray r, double tMin, double tMax, HitRecord rec);
    public double pdfValue(final Vector3 o, final Vector3 v){
        return 0.0;
    }
    public Vector3 random(){
        return new Vector3(1,0,0);
    }

    public abstract  boolean boundingBox(double t0, double t1, AABB box);
    public abstract  Material getMaterial();

    public abstract  void setMaterial(Material material);
    public abstract  String getShape();

    public abstract void setPosition(Vector3 position);
    public abstract Vector3 getPosition();

    public abstract void setHeight(double height);
    public abstract void setWidth(double width);
    public double degreesToRadians(double angle){
        return angle*Math.PI/180.0;
    }
}
