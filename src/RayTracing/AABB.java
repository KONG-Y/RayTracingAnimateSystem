package RayTracing;

public class AABB {

    public Vector3 minimum;

    public Vector3 getMinimum() {
        return minimum;
    }

    public void setMinimum(Vector3 minimum) {
        this.minimum = minimum;
    }

    public Vector3 getMaximum() {
        return maximum;
    }

    public void setMaximum(Vector3 maximum) {
        this.maximum = maximum;
    }

    public Vector3 maximum;
    public AABB(){
        minimum=new Vector3(0,0,0);
        maximum=new Vector3(0,0,0);
    }
    public AABB(Vector3 a, Vector3 b){
        minimum=a;
        maximum=b;
    }


    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec){
        double invD = 1.0f/r.getDirection().x();
        double t0 = (minimum.x()-r.getOrigin().x())*invD;
        double t1 = (maximum.x()-r.getOrigin().x())*invD;
        double temp;
        if(invD<0.0f){
            temp=t0;
            t0=t1;
            t1=temp;
        }
        tMin = Math.max(t0, tMin);
        tMax = Math.min(t1, tMax);
        if(tMax<=tMin){
            return false;
        }

        invD = 1.0f/r.getDirection().y();
        t0 = (minimum.y()-r.getOrigin().y())*invD;
        t1 = (maximum.y()-r.getOrigin().y())*invD;
        if(invD<0.0f){
            temp=t0;
            t0=t1;
            t1=temp;
        }
        tMin = Math.max(t0, tMin);
        tMax = Math.min(t1, tMax);
        if(tMax<=tMin){
            return false;
        }

         invD = 1.0f/r.getDirection().z();
         t0 = (minimum.z()-r.getOrigin().z())*invD;
         t1 = (maximum.z()-r.getOrigin().z())*invD;
        if(invD<0.0f){
            temp=t0;
            t0=t1;
            t1=temp;
        }
        tMin = Math.max(t0, tMin);
        tMax = Math.min(t1, tMax);
        if(tMax<=tMin){
            return false;
        }

        return true;
    }


}
