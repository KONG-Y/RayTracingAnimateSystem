package RayTracing;

public abstract class Material extends RayTracing {
    //发射光（光源）
    public Vector3 emitted(double u , double v, final Vector3 p ){
        return new Vector3(0,0,0);
    }
    public Vector3 emitted(final Ray rIn, final HitRecord rec, double u, double v, final Vector3 p){
        return new Vector3(0,0,0);
    }
    //散射光
    public abstract boolean scatter(final Ray rIn, final HitRecord rec,Vector3 attenuation, Ray scattered);
    public abstract boolean scatter(final Ray rIn, final HitRecord rec, ScatterRecord sre);

    //散射pdf
    public double scatteringPdf(final Ray rIn, final HitRecord rec, final Ray scattered){
        return 0;
    }

    public abstract Vector3 getColor();
    public abstract  void setColor(Vector3 color);

    public abstract String getType();
}

