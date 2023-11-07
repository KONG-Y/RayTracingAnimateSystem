package RayTracing;

public class HittablePdf extends Pdf {
    public Vector3 origin;
    public Hittable ptr;

    public HittablePdf(Hittable p, final Vector3 o){
        ptr = p;
        origin = o;
    }

    @Override
    public final double value(final Vector3 direction){
        return ptr.pdfValue(origin,direction);
    }

    @Override
    public final Vector3 generate(){
        return ptr.random();
    }
}
