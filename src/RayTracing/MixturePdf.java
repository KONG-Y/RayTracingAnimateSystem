package RayTracing;

public class MixturePdf extends  Pdf {
    public Pdf []p=new Pdf[2];
    public MixturePdf(Pdf p0, Pdf p1){
        p[0]=p0;
        p[1]=p1;
    }

    @Override
    public final double value(final Vector3 direction){
        return 0.5*p[0].value(direction) + 0.5*p[1].value(direction);
    }

    @Override
    public final Vector3 generate(){
        if(randomDouble()<0.5){
            return p[0].generate();
        }
        else{
            return p[1].generate();
        }
    }
}
