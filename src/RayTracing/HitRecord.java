package RayTracing;

public class HitRecord {
    private Vector3 point;   //交点
    private Vector3 normal;  //法向量
    private Material material;//材质指针
    private double time;	  //相交时间
    private boolean frontFace;//是否为外表面
    private double u,v;

    public HitRecord(){
        point =new Vector3(0,0,0);
        normal=new Vector3(0,0,0);
    }

    public void setValue(HitRecord rec){
        point=rec.getPoint();
        time=rec.getTime();
        material=rec.getMaterial();
        u=rec.getU();
        v=rec.getV();
        frontFace=rec.getFrontFace();
        normal=rec.getNormal();
    }
    public void setTime(double t){
        time=t;
    }
    public double getTime(){
        return time;
    }

    public void setPoint(Vector3 p){
        point=p;
    }
    public Vector3 getPoint(){
        return point;
    }

    public void setMaterial(Material m){
        material=m;
    }
    public Material getMaterial(){
        return material;
    }

    public double getU(){
        return u;
    }
    public void setU(double U){
        u=U;
    }

    public void setV(double V){
        v=V;
    }
    public double getV(){
        return v;
    }

    public boolean getFrontFace(){
        return this.frontFace;
    }
    public void setFrontFace(boolean frontFace){
        this.frontFace=frontFace;
    }
    public Vector3 getNormal(){
        return normal;
    }

    //根据光线在球上是否朝外设置法线方向
    public void setFaceNormal(final Ray r, final Vector3 outwardNormal){
        frontFace = r.getDirection().dot(outwardNormal)<0;;//光线在球内，则>0。光线在球外，则<0
        normal = frontFace?outwardNormal:outwardNormal.nan();
    }
}
