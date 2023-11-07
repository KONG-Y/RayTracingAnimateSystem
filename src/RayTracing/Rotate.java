package RayTracing;

public class Rotate extends Hittable {
    private Hittable object;
    private double sinTheta;
    private double cosTheta;

    private boolean hasBox;

    private AABB aabb;


    public Rotate(Hittable object, double angle) {
        double radians = degreesToRadians(angle);
        sinTheta = Math.sin(radians);
        cosTheta = Math.cos(radians);

        aabb = new AABB();
        this.object = object;
        hasBox = this.object.boundingBox(0, 1, aabb);

        Vector3 min = new Vector3(posInfinity, posInfinity, posInfinity);
        Vector3 max = new Vector3(negInfinity, negInfinity, negInfinity);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    double x = i * aabb.getMaximum().x() + (1 - i) * aabb.getMinimum().x();
                    double y = j * aabb.getMaximum().y() + (1 - j) * aabb.getMinimum().y();
                    double z = k * aabb.getMaximum().z() + (1 - k) * aabb.getMinimum().z();

                    //沿y轴旋转，y值不变
                    double newX = cosTheta * x + sinTheta * z;
                    double newZ = -sinTheta * x + cosTheta * z;

                    Vector3 tester = new Vector3(newX, y, newZ);

                    min.setX(fmin(min.x(), tester.x()));
                    min.setY(fmin(min.y(), tester.y()));
                    min.setZ(fmin(min.z(), tester.z()));

                    max.setX(fmax(max.x(), tester.x()));
                    max.setY(fmax(max.y(), tester.y()));
                    max.setZ(fmax(max.z(), tester.z()));
                }
            }
        }

        aabb = new AABB(min, max);
    }

    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec) {
        Vector3 origin = r.getOrigin();
        Vector3 direction = r.getDirection();

        origin.setX(cosTheta*r.getOrigin().x()-sinTheta*r.getOrigin().z());
        origin.setZ(sinTheta*r.getOrigin().x()+cosTheta*r.getOrigin().z());

        direction.setX(cosTheta*r.getDirection().x()-sinTheta*r.getDirection().z());
        direction.setZ(sinTheta*r.getDirection().x()+cosTheta*r.getDirection().z());

        Ray rotatedR=new Ray(origin,direction,r.getTime());

        if(!this.object.hit(rotatedR,tMin,tMax,rec)){
            return false;
        }

        Vector3 point = rec.getPoint();
        Vector3 normal = rec.getNormal();

        point.setX(cosTheta*rec.getPoint().x()+sinTheta*rec.getPoint().z());
        point.setZ(-sinTheta*rec.getPoint().x()+cosTheta*rec.getPoint().z());

        normal.setX(cosTheta*rec.getNormal().x()+sinTheta*rec.getNormal().z());
        normal.setZ(-sinTheta*rec.getNormal().x()+cosTheta*rec.getNormal().z());

        rec.setPoint(point);
        rec.setFaceNormal(rotatedR,normal);
        return true;
    }

    public boolean boundingBox(double t0, double t1, AABB outputBox){
        outputBox.setMinimum(aabb.getMinimum());
        outputBox.setMaximum(aabb.getMaximum());
        return hasBox;
    }

    public  Material getMaterial(){
        return null;
    }

    public   void setMaterial(Material material){

    }
    public   String getShape(){
        return "Rotate";
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