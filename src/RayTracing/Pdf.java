package RayTracing;

public abstract class Pdf extends RayTracing{
    public abstract  double value(final Vector3 direction);
    public abstract  Vector3 generate();

    public Vector3 randomToSphere(double radius, double distanceSquared){
        double r1 = randomDouble();
        double r2 = randomDouble();
        double z = 1+r2*(Math.sqrt(1-radius*radius/distanceSquared)-1);

        double phi = 2*pi*r1;
        double x =Math.cos(phi)*Math.sqrt(1-z*z);
        double y = Math.sin(phi)*Math.sqrt(1-z*z);
        return new Vector3(x,y,z);
    }
}
