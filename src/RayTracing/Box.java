package RayTracing;

public class Box extends Hittable{
    private String type="Box";
    private Vector3 position;
    private Vector3 maxPos;
    private Vector3 minPos;


    private double height;
    private double width;
    private double length;
    private Material material;
    private HittableList sides;
    public Box(){};
    public Box(Vector3 position,double height,double width,double length,Material material ){

        this.position=position;
        this.height=height;
        this.width=width;
        this.length=length;
        this.material=material;

        this.minPos=new Vector3(position.x()-(width/2.0),position.y()-(length/2.0),position.z()-(height/2.0));
        this.maxPos=new Vector3(position.x()+(width/2.0),position.y()+(length/2.0),position.z()+(height/2.0));

        this.sides=new HittableList();
        //xy轴
        this.sides.add(new XYRect(new Vector3(position.x(),position.y(),position.z()+(height/2.0)),width,length,material));
        this.sides.add(new XYRect(new Vector3(position.x(),position.y(),position.z()-(height/2.0)),width,length,material));

        //xz轴
        this.sides.add(new XZRect(new Vector3(position.x(),position.y()+(length/2.0),position.z()),height,width,material));
        this.sides.add(new XZRect(new Vector3(position.x(),position.y()-(length/2.0),position.z()),height,width,material));

        //yz轴
        this.sides.add(new YZRect(new Vector3(position.x()+(width/2.0),position.y(),position.z()),height,length,material));
        this.sides.add(new YZRect(new Vector3(position.x()-(width/2.0),position.y(),position.z()),height,length,material));

        this.minPos=new Vector3(position.x()-(width/2.0),position.y()-(length/2.0),position.z()-(height/2.0));
        this.maxPos=new Vector3(position.x()+(width/2.0),position.y()+(length/2.0),position.z()+(height/2.0));

    }


    @Override
    public boolean hit(final Ray r, double tMin, double tMax, HitRecord rec){
        return this.sides.hit(r, tMin, tMax, rec);
    }



        @Override
    public boolean boundingBox(double t0, double t1, AABB outputBox){
        outputBox.setMaximum(maxPos);
        outputBox.setMinimum(minPos);
        return true;
    }

    @Override
    public Material getMaterial() {
        return this.material;
    }
    @Override
    public void setMaterial(Material material){
        this.material=material;
        for(int i=0;i<this.sides.length();i++){
            this.sides.getIndexObj(i).setMaterial(this.material);
        }
    }
    @Override
    public String getShape() {
        return "Box";
    }

    @Override
    public void setPosition(Vector3 position){
        this.position=position;
        this.sides.getIndexObj(0).setPosition(new Vector3(position.x(),position.y(),position.z()+(height/2.0)));
        this.sides.getIndexObj(1).setPosition(new Vector3(position.x(),position.y(),position.z()-(height/2.0)));

        this.sides.getIndexObj(2).setPosition(new Vector3(position.x(),position.y()+(length/2.0),position.z()));
        this.sides.getIndexObj(3).setPosition(new Vector3(position.x(),position.y()-(length/2.0),position.z()));

        this.sides.getIndexObj(4).setPosition(new Vector3(position.x()+(width/2.0),position.y(),position.z()));
        this.sides.getIndexObj(5).setPosition(new Vector3(position.x()-(width/2.0),position.y(),position.z()));

        this.minPos=new Vector3(position.x()-(width/2.0),position.y()-(length/2.0),position.z()-(height/2.0));
        this.maxPos=new Vector3(position.x()+(width/2.0),position.y()+(length/2.0),position.z()+(height/2.0));

    }
    @Override
    public Vector3 getPosition(){
        return this.position;

    }


    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;

        //xy
        this.sides.getIndexObj(0).setPosition(new Vector3(position.x(),position.y(),position.z()+(height/2.0)));
        this.sides.getIndexObj(1).setPosition(new Vector3(position.x(),position.y(),position.z()-(height/2.0)));
        //xz
        this.sides.getIndexObj(2).setHeight(this.height);
        this.sides.getIndexObj(3).setHeight(this.height);
        //yz
        this.sides.getIndexObj(4).setHeight(this.height);
        this.sides.getIndexObj(5).setHeight(this.height);

        this.minPos=new Vector3(position.x()-(width/2.0),position.y()-(length/2.0),position.z()-(height/2.0));
        this.maxPos=new Vector3(position.x()+(width/2.0),position.y()+(length/2.0),position.z()+(height/2.0));


    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        //xy
        this.sides.getIndexObj(0).setWidth(this.width);
        this.sides.getIndexObj(1).setWidth(this.width);
        //xz
        this.sides.getIndexObj(2).setWidth(this.width);
        this.sides.getIndexObj(3).setWidth(this.width);
        //yz
        this.sides.getIndexObj(4).setPosition(new Vector3(position.x()+(this.width/2.0),position.y(),position.z()));
        this.sides.getIndexObj(5).setPosition(new Vector3(position.x()-(this.width/2.0),position.y(),position.z()));

        this.minPos=new Vector3(position.x()-(width/2.0),position.y()-(length/2.0),position.z()-(height/2.0));
        this.maxPos=new Vector3(position.x()+(width/2.0),position.y()+(length/2.0),position.z()+(height/2.0));

    }
    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
        //xy
        this.sides.getIndexObj(0).setHeight(this.length);
        this.sides.getIndexObj(1).setHeight(this.length);
        //xz
        this.sides.getIndexObj(2).setPosition(new Vector3(position.x(),position.y()+(this.length/2.0),position.z()));
        this.sides.getIndexObj(3).setPosition(new Vector3(position.x(),position.y()-(this.length/2.0),position.z()));
        //yz
        this.sides.getIndexObj(4).setWidth(this.length);
        this.sides.getIndexObj(5).setWidth(this.length);

        this.minPos=new Vector3(position.x()-(width/2.0),position.y()-(length/2.0),position.z()-(height/2.0));
        this.maxPos=new Vector3(position.x()+(width/2.0),position.y()+(length/2.0),position.z()+(height/2.0));

    }
}
