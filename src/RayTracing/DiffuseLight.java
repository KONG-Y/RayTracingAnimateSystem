package RayTracing;

public class DiffuseLight extends Material{
    private String type="DiffuseLight";
    public Texture emit;
    private Vector3 colorValue;

    public String getType(){
        return"发光材质";
    }
    public DiffuseLight(Texture a){
        emit=a;
        colorValue=emit.value(0,0,null);
    }
    public DiffuseLight(Vector3 c){
        emit= new SolidColor(c);
        colorValue=emit.value(0,0,null);
    }

    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec,Vector3 attenuation, Ray scattered){
        return false;
    }
    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec, ScatterRecord sre){
       return false;
    }

    @Override
    public final Vector3 emitted(double u, double v, final Vector3 p){
        return emit.value(u,v,p);
    }
    @Override
    public final Vector3 emitted(final Ray rIn, final HitRecord rec, double u, double v, Vector3 p){
        //外表面才发射光的颜色
        if(rec.getFrontFace()){
            return emit.value(u,v,p);
        }
        else{
            return new Vector3(0,0,0);
        }

    }

    @Override
    public final Vector3 getColor(){
        return colorValue;
    }
    @Override
    public final void setColor(Vector3 color){
        colorValue=color;
    }
}
