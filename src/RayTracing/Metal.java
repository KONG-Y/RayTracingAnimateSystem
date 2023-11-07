package RayTracing;

public class Metal extends  Material {
    private String type="Metal";
    public Vector3 albedo;//反射率
    public double fuzz;//模糊率

    public String getType()
    {
        return "金属材质" ;
    }

    public Metal(final Vector3 a, double f){
        albedo=a;
        fuzz=(f<1)?f:1;
    }

    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec, Vector3 attenuation, Ray scattered){
        Vector3 r = new Vector3();
        Vector3 reflected = r.reflect(rIn.getDirection().unitVector(),rec.getNormal());
        scattered.setValue(new Ray(rec.getPoint(),reflected.plus(r.randomInUnitSphere().multiply(fuzz)),rIn.getTime()));
        attenuation.setValue(albedo);
        return (scattered.getDirection().dot(rec.getNormal())>0);
    }
    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec, ScatterRecord sre){
        Vector3 reflected = new Vector3();
        reflected=reflected.reflect(rIn.getDirection().unitVector(),rec.getNormal());
        sre.setSpecularRay(new Ray(rec.getPoint(),reflected.plus(reflected.randomInUnitSphere().multiply(fuzz)),rIn.getTime()));
        sre.setAttenuation(albedo);
        sre.setIsSpecular(true);
        sre.setPdf(null);
        return true;
    }

    @Override
    public final Vector3 getColor(){
        return albedo;
    }
    @Override
    public final void setColor(Vector3 color){
        albedo=color;
    }
}

