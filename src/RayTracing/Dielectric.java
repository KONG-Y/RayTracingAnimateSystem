package RayTracing;

public class Dielectric extends Material {
    private String type = "Dielectric";
    public double ir;
    public String getType(){
        return "玻璃材质";
    }
    public  Dielectric(double indexOfRefraction){
        ir = indexOfRefraction;
    }
    private static double reflectance(double cosine, double refIdx){
        double r0= (1-refIdx)/(1+refIdx);
        r0=r0*r0;
        return r0+(1-r0)*Math.pow((1-cosine),5);
    }
    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec, Vector3 attenuation, Ray scattered){
        attenuation.setValue(new Vector3(1.0,1.0,1.0));//要是vec3(1,0,1.0,0.0)，会让B通道没有折射光
        double refractionRatio = rec.getFrontFace()?(1.0/ir):ir;

        Vector3 unitDirection = rIn.getDirection().unitVector();
        double cosTheta = Math.min(unitDirection.nan().dot(rec.getNormal()),1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);

        boolean cannotRefract = refractionRatio*sinTheta>1.0;
        Vector3 direction;

        if(cannotRefract||reflectance(cosTheta,refractionRatio)>randomDouble()){
            //发生反射
            direction = new Vector3().reflect(unitDirection,rec.getNormal());
        }
        else{
            //发生折射
            direction = new Vector3().refract(unitDirection,rec.getNormal(),refractionRatio);
        }

        scattered.setValue(new Ray(rec.getPoint(),direction,rIn.getTime()));
        return true;
    }
    @Override
    public final boolean scatter(final Ray rIn, final HitRecord rec, ScatterRecord srec){
        srec.setIsSpecular(true);
        srec.setPdf(null);
        srec.setAttenuation(new Vector3(1.0,1.0,1.0));//要是vec3(1,0,1.0,0.0)，会让B通道没有折射光

        double refractionRatio = rec.getFrontFace()?(1.0/ir):ir;

        Vector3 unitDirection = rIn.getDirection().unitVector();
        double cosTheta = Math.min(unitDirection.nan().dot(rec.getNormal()),1.0);
        double sinTheta = Math.sqrt(1.0 - cosTheta*cosTheta);

        boolean cannotRefract = refractionRatio*sinTheta>1.0;
        Vector3 direction;

        if(cannotRefract||reflectance(cosTheta,refractionRatio)>randomDouble()){
            //发生反射
            direction = new Vector3().reflect(unitDirection,rec.getNormal());
        }
        else{
            //发生折射
            direction = new Vector3().refract(unitDirection,rec.getNormal(),refractionRatio);
        }

        srec.setSpecularRay(new Ray(rec.getPoint(),direction,rIn.getTime()));
        return true;
    }
    @Override
    public final Vector3 getColor(){
        return new Vector3(1,1,1);
    }
    @Override
    public final void setColor(Vector3 color){

    }
}
