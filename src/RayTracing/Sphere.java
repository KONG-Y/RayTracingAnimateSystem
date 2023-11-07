package RayTracing;

public class Sphere extends Hittable {
    private String type="Sphere";
    private Vector3 position;
    private double radius;
    private Material material;
    private String materialType;

    public Sphere(){}
    public Sphere(Vector3 position, double r, Material m){
        this.position=position;
        radius=r;
        material=m;
    }

    @Override
    public boolean boundingBox(double t0, double t1, AABB outputBox){
        outputBox.setMinimum(position.minus(new Vector3(radius,radius,radius)));
        outputBox.setMaximum(position.minus(new Vector3(radius,radius,radius)));
        return true;
    }


    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector3 position){
        this.position=position;
    }
    public double getRadius(){
        return radius;
    }
    public void setRadius(double r){
        radius=r;
    }
    @Override
    public void setMaterial(Material m){
        material=m;
    }

    @Override
    public String getShape(){
        return "Sphere";
    }
    @Override
    public Material getMaterial(){
        return material;
    }
    public final Vector3 random(final Vector3 o){
        Vector3 direction = this.position.minus(o);
        double distanceSquared = direction.lengthSquared();
        Onb uvw = new Onb();
        uvw.buildFromW(direction);


        double r1 = randomDouble();
        double r2 = randomDouble();
        double z = 1+r2*(Math.sqrt(1-radius*radius/distanceSquared)-1);

        double phi = 2*pi*r1;
        double x =Math.cos(phi)*Math.sqrt(1-z*z);
        double y = Math.sin(phi)*Math.sqrt(1-z*z);
        return uvw.local(new Vector3(x,y,z));
    }

    //获取UV坐标
    private void getSphereUV(final Vector3 p, HitRecord rec){
        double theta = Math.acos(-p.y());
        double phi = Math.atan2(-p.z(),p.x())+pi;
        rec.setU(phi/(2*pi));
        rec.setV(theta/pi);
    }

    @Override
    public final double pdfValue(final Vector3 o, final Vector3 v){
        HitRecord rec =new HitRecord() ;
        if(!this.hit(new Ray(o,v,0),0.001,posInfinity,rec)){
            return 0;
        }

        double cosThetaMax = Math.sqrt(1-radius*radius/(this.position.minus(o).lengthSquared()));
        double solidAngle = 2*pi*(1-cosThetaMax);
        return 1/solidAngle;
    }

    //光线碰撞
    @Override
    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec){
        //A为起点r.getOrigin()，B为方向向量r.getDirection()，C为球中心点center
        //球数学公式：(球表面点-球中心)^2 - R^2=0
        //dot((A+t*B-C),(A+t*B-C))=R*R
        //上式化简为：t*t*dot(B,B)+2*t*dot(B,A-C)+dot(A-C,A-C)-R*R=0
        //求出是否有解t，有解则为相交点
        Vector3 oc = r.getOrigin().minus(this.position);//A-C
        double a = r.getDirection().lengthSquared();//dot(B,B)
        double halfB=r.getDirection().dot(oc);//dot(B,A-C)，化简去掉*2
        double c = oc.lengthSquared()-radius*radius;	//dot(A-C,A-C)-R*R
        double discriminant = halfB*halfB-a*c;//t的解，根据二元一次方程判断是否有解（两个解），化简去掉*4

        //光线没有与球体相交
        if(discriminant<0)return false;
        //t的解，根据二元一次方程判断是否有解（两个解）
        //判断最小解
        double sqrtd = Math.sqrt(discriminant);
        double root = (-halfB-sqrtd)/a;
        if(root<tMin||tMax<root){
            root = (-halfB + sqrtd)/a;
            if(root<tMin||tMax<root)return false;
        }

        rec.setTime(root);
        rec.setPoint(r.at(rec.getTime()));
        rec.setMaterial(material);
        Vector3 outwardNormal = rec.getPoint().minus(this.position).divide(radius);//法向量（局部空间坐标）
        getSphereUV(outwardNormal,rec);
        rec.setFaceNormal(r,outwardNormal);
        return true;
    }

    @Override
    public void setHeight(double height){

    }
    @Override
    public void setWidth(double width){
    }

}
