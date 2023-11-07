package RayTracing;

public abstract class Texture {
    public abstract Vector3 value(double u, double v, final Vector3 p);
}
