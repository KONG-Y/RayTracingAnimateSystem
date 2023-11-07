package RayTracing;

public class XZRect extends Rect {
    private String type="XZRect";



    private Vector3 position;
    private double height;
    private double width;
    private double x0,x1,z0,z1,k;
    private Material material;
    public XZRect(){}
    public XZRect(Vector3 position,double height, double width, Material m){
        this.position=position;
        this.height=height;
        this.width=width;
        this.x0=this.position.x()-(this.width/2.0);
        this.x1=this.position.x()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.y();
        this.material=m;
    }

    @Override
    public boolean boundingBox(double t0, double t1, AABB outputBox){
        outputBox.setMinimum(new Vector3(x0,k-0.0001,z0));
        outputBox.setMaximum(new Vector3(x1,k+0.0001,z1));
        return true;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }
    @Override
    public void setPosition(Vector3 position) {
        this.position = position;
        this.x0=this.position.x()-(this.width/2.0);
        this.x1=this.position.x()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.y();
    }

    public double getWidth() {
        return width;
    }
    @Override
    public void setWidth(double width) {
        this.width = width;
        this.x0=this.position.x()-(this.width/2.0);
        this.x1=this.position.x()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.y();
    }


    public double getHeight() {
        return height;
    }
    @Override
    public void setHeight(double height) {
        this.height = height;
        this.x0=this.position.x()-(this.width/2.0);
        this.x1=this.position.x()+(this.width/2.0);
        this.z0=this.position.z()-(this.height/2.0);
        this.z1 =this.position.z()+(this.height/2.0);
        this.k=this.position.y();
    }
    public double getX0(){
        return x0;
    }
    public double getX1(){
        return x1;
    }
    public double getZ0(){
        return z0;
    }
    public double getZ1(){
        return z1;
    }
    public double getY(){
        return k;
    }
    public String getType(){
        return"XZ";
    }

    @Override
    public void setMaterial(Material m){
        material=m;
    }

    @Override
    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec){
        //光线碰撞到的平面为xz轴组成的平面，y轴值为k
        //光线碰撞的平面为x=k的平面，由光线公式P(t)=A+tb,得P(t)=k, 即k=A+tb（y轴的A和b）
        //t=(k-A)/b（y轴的A和b）

        double t = (k-r.getOrigin().y())/r.getDirection().y();

        if(t<tMin||t>tMax){
            return false;
        }

        double x = r.getOrigin().x()+t*r.getDirection().x();
        double z = r.getOrigin().z()+t*r.getDirection().z();
        if(x<x0||x>x1||z<z0||z> z1){
            return false;
        }

        rec.setU((x-x0)/(x1-x0));
        rec.setV((z-z0)/(z1 -z0));
        rec.setTime(t);
        Vector3 outwardNormal = new Vector3(0,1,0);
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
