package RayTracing;

public class CosinePdf extends Pdf {
    public Onb uvw=new Onb();
    public CosinePdf(final Vector3 w){
        uvw.buildFromW(w);
    }
    public Vector3 randomCosineDirection(){
        double r1 = randomDouble();
        double r2 = randomDouble();
        double z = Math.sqrt(1-r2);

        double phi=2*pi*r1;
        double x = Math.cos(phi)*Math.sqrt(r2);
        double y =Math.sin(phi)*Math.sqrt(r2);

        return new Vector3(x,y,z);
    }
    @Override
    public final double value(final Vector3 direction){
        double cosine = uvw.w().dot(direction.unitVector());
        return (cosine<=0)?0:cosine/pi;
    }

    @Override
    public final Vector3 generate(){
        return uvw.local(randomCosineDirection());
    }
}
