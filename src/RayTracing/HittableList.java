package RayTracing;

import java.util.ArrayList;
public class HittableList extends  Hittable{
    public ArrayList<Hittable> objects=new ArrayList<Hittable>();
    public HittableList(){}
    public HittableList(Hittable object){
        objects.add(object);
    }
    public void add(Hittable object){
        objects.add(object);
    }
    public void clear(){
        objects.clear();
    }
    public void delete(int index){objects.remove(index);}
    public final double pdfValue(final Vector3 o, final Vector3 v){
        double weight =1.0/objects.size();
        double sum=0.0;

        for(final Hittable object:objects){
            sum+=weight*object.pdfValue(o,v);
        }

        return sum;
    }

    @Override
    public boolean boundingBox(double t0, double t1,AABB outputBox){
        if(objects.isEmpty()){
            return false;
        }
        AABB tempBox=new AABB();
        boolean firstBox = true;

        for(final Hittable object:objects){
            //是否与包围盒相交
            if(!object.boundingBox(t0,t1,tempBox)){
                return false;
            }
            //第一次包围盒不用比较，否则要计算新的包围盒并比较前一个包围盒，找出两个包围盒中最小点和最大点
            outputBox=firstBox?tempBox:surroundingBox(outputBox,tempBox);
            firstBox = false;
        }
        return true;
    }
    public AABB surroundingBox(AABB box0, AABB box1){
        Vector3 box0Min = box0.getMinimum();
        Vector3 box1Min = box1.getMinimum();
        Vector3 box0Max = box0.getMaximum();
        Vector3 box1Max = box1.getMaximum();
        Vector3 small = new Vector3(
                fmin(box0Min.x(),box1Min.x()),
                fmin(box0Min.y(),box1Min.y()),
                fmin(box0Min.z(),box1Min.z())
        );

        Vector3 big=new Vector3(
                fmax(box0Max.x(),box1Max.x()),
                fmax(box0Max.y(),box1Max.y()),
                fmax(box0Max.z(),box1Max.z())

        );
        return new AABB(small,big);
    }

    @Override
    public Material  getMaterial(){
        return null;
    }
    @Override
    public void  setMaterial(Material material){

    }
    public int length(){
        return objects.size();
    }
    public Hittable getIndexObj(int index){
        return objects.get(index);
    }
    public void setIndexObj(int index, Hittable obj){
        objects.set(index,obj);
    }
    public Vector3 getMaterialColor(){
        return new Vector3(0,0,0);
    }
    @Override
    public String getShape(){
        return "";
    }
    @Override
    public final boolean hit(final Ray r, double tMin, double tMax, HitRecord rec){
        HitRecord tempRec=new HitRecord();
        boolean hitAnything=false;
        double closestSoFar = tMax;

        //光线碰撞最后一个物体后的时间，为全部物体都能与光线碰撞的时间，以此时间来计算场景所有光线渲染
        for(int i=0; i<objects.size();++i){
            //若物体与光线碰撞，则返回光线碰撞信息
            if(objects.get(i).hit(r,tMin,closestSoFar,tempRec)){
                hitAnything =true;
                closestSoFar =tempRec.getTime();//到达此碰撞体交点时的时间，作为下一个碰撞体的时间上限（一条光线会依次碰撞物体，所以按顺序来计算）
                rec.setValue(tempRec);//光线碰撞记录
            }
        }
        return hitAnything;
    }

    public void setFrameObjects(ArrayList<Vector3>scenePos){
        for(int i=0;i<scenePos.size();i++){
            objects.get(i).setPosition(scenePos.get(i));
        }
    }
    @Override
    public void setPosition(Vector3 position){

    }

    @Override
    public Vector3 getPosition(){
        return null;
    }

    @Override
    public void setHeight(double height){

    }
    @Override
    public void setWidth(double width){
    }

}
