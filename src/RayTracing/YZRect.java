package RayTracing;

public class YZRect extends Rect {
    private String type="YZRect";

    private Vector3 position;
    private double height;
    private double width;
    private double y0, y1,z0,z1,k;
    private Material material;
    public YZRect(){}
    public YZRect(Vector3 position,double height,double width, Material m){
        this.position=position;
        this.height=height;
        this.width=width;
        this.y0 =this.position.y()-(this.width/2.0);
        this.y1 =this.position.y()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.x();
        this.material=m;
    }

    @Override
    public boolean boundingBox(double t0, double t1, AABB outputBox){
        outputBox.setMinimum(new Vector3(k-0.0001,y0,z0));
        outputBox.setMaximum(new Vector3(k+0.0001,y1,z1));
        return true;
    }
    @Override
    public Vector3 getPosition() {
        return position;
    }
    @Override
    public void setPosition(Vector3 position) {
        this.position = position;
        this.y0 =this.position.y()-(this.width/2.0);
        this.y1 =this.position.y()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.x();
    }

    public double getHeight() {
        return height;
    }
    @Override
    public void setHeight(double height) {
        this.height = height;
        this.y0 =this.position.y()-(this.width/2.0);
        this.y1 =this.position.y()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.x();
    }

    public double getWidth() {
        return width;
    }
    @Override
    public void setWidth(double width) {
        this.width = width;
        this.y0 =this.position.y()-(this.width/2.0);
        this.y1 =this.position.y()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.x();
    }
    public double getY0(){
        return y0;
    }
    public double getY1(){
        return y1;
    }
    public double getZ0(){
        return z0;
    }
    public double getZ1(){
        return z1;
    }
    public double getX(){
        return k;
    }
    public String getType(){
        return "YZ";
    }

    @Override
    public void setMaterial(Material m){
        material=m;
    }

    @Override
    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec){
        //光线碰撞到的平面为yz轴组成的平面，x轴值为k
        //光线碰撞的平面为x=k的平面，由光线公式P(t)=A+tb,得P(t)=k, 即k=A+tb（x轴的A和b）
        //t=(k-A)/b（x轴的A和b）
        double t = (k-r.getOrigin().x())/r.getDirection().x();

        if(t<tMin||t>tMax){
            return false;
        }

        double z = r.getOrigin().z()+t*r.getDirection().z();
        double y = r.getOrigin().y()+t*r.getDirection().y();
        if(z< z0 ||z> z1 ||y<y0||y> y1){
            return false;
        }

        rec.setU((y- y0)/(y1 - y0));
        rec.setV((z-z0)/(z1 -z0));
        rec.setTime(t);
        Vector3 outwardNormal = new Vector3(1,0,0);
        rec.setFaceNormal(r,outwardNormal);
        rec.setMaterial(material);
        rec.setPoint(r.at(t));
        return true;
    }
    @Override
    public Material getMaterial(){
        return material;
    }
}
