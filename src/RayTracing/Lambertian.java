package RayTracing;

//漫反射材质（理想的完全漫散射）
public class Lambertian extends Material {
    private String type="Lambertian";
    public Texture albedo;
    private Vector3 colorValue;
    public String getType()
    {
        return "漫反射材质" ;
    }

    public Lambertian(final Vector3 a){
        albedo=new SolidColor(a);
        colorValue=albedo.value(0,0,null);
    }
    public Lambertian(Texture a){
        albedo=a;
        colorValue=albedo.value(0,0,null);
    }
    public Lambertian(double red, double green, double blue){
        albedo = new SolidColor(red,green,blue);
        colorValue=albedo.value(0,0,null);
    }

    //散射
    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec,Vector3 attenuation, Ray scattered){
        Vector3 r=new Vector3();
        Vector3 scatterDirection = rec.getNormal().plus(r.randomUnitVector());

        if(scatterDirection.nearZero()){
            scatterDirection=rec.getNormal();
        }

        Ray s = new Ray(rec.getPoint(),scatterDirection,rIn.getTime());
        scattered.setValue(s);
        attenuation.setValue(albedo.value(rec.getU(),rec.getV(),rec.getPoint()));
        return true;
    }

    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec, ScatterRecord srec){
        srec.setIsSpecular(false);
        srec.setAttenuation(albedo.value(rec.getU(),rec.getV(),rec.getPoint()));
        srec.setPdf(new CosinePdf(rec.getNormal()));
        return true;
    }

    //散射PDF
    @Override
    public final double scatteringPdf(final Ray rIn, final HitRecord rec, final Ray scattered){
        double cosine = rec.getNormal().dot(scattered.getDirection().unitVector());
        return cosine<0?0:cosine/pi;
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
