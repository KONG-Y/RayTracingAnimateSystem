package RayTracing;

public class FlipFace extends Hittable{
    public Hittable object;

    public FlipFace(Hittable p){
        this.object=p;
    }

    @Override
    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec) {
        if(!this.object.hit(r,tMin,tMax,rec)){
            return false;
        }
        rec.setFrontFace(!rec.getFrontFace());
        return true;
    }
    @Override
    public   boolean boundingBox(double t0, double t1, AABB outputBox)
    {
        return this.object.boundingBox(t0,t1,outputBox);
    }
    public   Material getMaterial(){
        return null;
    }

    public   void setMaterial(Material material){

    }
    public   String getShape(){
        return "FlipFace";
    }

    public  void setPosition(Vector3 position){

    }
    public  Vector3 getPosition(){
        return null;
    }

    public  void setHeight(double height){

    }
    public  void setWidth(double width){

    }
}
