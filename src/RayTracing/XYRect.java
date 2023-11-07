package RayTracing;

public class XYRect extends Rect {
    private String type="XYRect";
    private double x0,x1,y0,y1,k;
    private Material material;



    private Vector3 position;

    private double width;
    private double height;

    public XYRect(){}
    public XYRect(Vector3 position, double height, double width, Material m){
        this.position=position;
        this.height=height;
        this.width=width;
        this.x0=this.position.x()-(this.width/2.0);
        this.x1=this.position.x()+(this.width/2.0);
        this.y0=this.position.y()-(this.height/2.0);
        this.y1=this.position.y()+ (this.height/2.0);
        this.k=this.position.z();
        material=m;
    }

    @Override
    public boolean boundingBox(double t0, double t1, AABB outputBox){
        //outputBox.setMinimum(new Vector3(x0,y0,k-0.0001));
        //outputBox.setMaximum(new Vector3(x1,y1,k+0.0001));
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
        this.y0=this.position.y()-(this.height/2.0);
        this.y1=this.position.y()+ (this.height/2.0);
        this.k=this.position.z();
    }

    public double getWidth() {
        return width;
    }
    @Override
    public void setWidth(double width) {
        this.width = width;
        this.x0=this.position.x()-(this.width/2.0);
        this.x1=this.position.x()+(this.width/2.0);
        this.y0=this.position.y()-(this.height/2.0);
        this.y1=this.position.y()+ (this.height/2.0);
        this.k=this.position.z();
    }

    public double getHeight() {
        return height;
    }
    @Override
    public void setHeight(double height) {
        this.height = height;
        this.x0=this.position.x()-(this.width/2.0);
        this.x1=this.position.x()+(this.width/2.0);
        this.y0=this.position.y()-(this.height/2.0);
        this.y1=this.position.y()+ (this.height/2.0);
        this.k=this.position.z();
    }
    public double getX0(){
        return x0;
    }
    public double getX1(){
        return x1;
    }
    public double getY0(){
        return y0;
    }
    public double getY1(){
        return y1;
    }
    public double getZ(){
        return k;
    }


    public String getType(){
        return "XY";
    }

    @Override
    public void setMaterial(Material m){
        material=m;
    }

    @Override
    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec){
        //光线碰撞到的平面为xy轴组成的平面，z轴值为k
        //光线碰撞的平面为z=k的平面，由光线公式P(t)=A+tb,得P(t)=k, 即k=A+tb（z轴的A和b）
        //t=(k-A)/b（z轴的A和b）

        double t = (k-r.getOrigin().z())/r.getDirection().z();

        if(t<tMin||t>tMax){
            return false;
        }

        double x = r.getOrigin().x()+t*r.getDirection().x();
        double y = r.getOrigin().y()+t*r.getDirection().y();
        if(x<x0||x>x1||y<y0||y>y1){
            return false;
        }

        rec.setU((x-x0)/(x1-x0));
        rec.setV((y-y0)/(y1-y0));
        rec.setTime(t);
        Vector3 outwardNormal = new Vector3(0,0,1);
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
