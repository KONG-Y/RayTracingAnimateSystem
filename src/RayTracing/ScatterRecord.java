package RayTracing;

public class ScatterRecord {
    private Ray specularRay;
    private boolean isSpecular;//是否为镜面反射
    private Vector3 attenuation;//衰减量
    private Pdf pdf;//概率函数

    public Pdf getPdf(){
        return pdf;
    }
    public void setPdf(Pdf p){
        pdf=p;
    }
    public Vector3 getAttenuation(){
        return attenuation;
    }
    public void setAttenuation(Vector3 a){
        attenuation=a;
    }
    public boolean getIsSpecular(){
        return isSpecular;
    }
    public void setIsSpecular(boolean is){
        isSpecular=is;
    }

    public Ray getSpecularRay(){
        return specularRay;
    }
    public void setSpecularRay(Ray s){
        specularRay=s;
    }



}
