package RayTracing;

public class Vector3 extends RayTracing{
    private double []e;
    public Vector3(){
        e=new double[]{0,0,0};
    }

    public Vector3(double e0, double e1, double e2){
        e=new double[]{e0,e1,e2};
    }

    public final double x() {
        return e[0];
    }
    public final double y(){
        return e[1];
    }
    public final double z(){
        return e[2];
    }

    public void setX(double x){
        this.e[0]=x;
    }
    public void setY(double y){
        this.e[1]=y;
    }
    public void setZ(double z){
        this.e[2]=z;
    }
    public void setValue(Vector3 v){
        e[0]=v.x();
        e[1]=v.y();
        e[2]=v.z();
    }
    //向量的负数形式
    public Vector3 nan(){
        return new Vector3(-e[0],-e[1],-e[2]);
    }

    public Vector3 plus(Vector3 v){
        return new Vector3(
                e[0]+v.e[0],
                e[1]+v.e[1],
                e[2]+v.e[2]);
    }
    public Vector3 minus(Vector3 v){
        return new Vector3(
                e[0]-v.e[0],
                e[1]-v.e[1],
                e[2]-v.e[2]);
    }
    public Vector3 multiply(Vector3 v){
        return new Vector3(
                e[0]*v.e[0],
                e[1]*v.e[1],
                e[2]*v.e[2]);
    }
    public Vector3 divide(Vector3 v){
        return new Vector3(
                e[0]/v.e[0],
                e[1]/v.e[1],
                e[2]/v.e[2]);
    }

    public Vector3 multiply(double t){
        return new Vector3(e[0]*t,e[1]*t,e[2]*t);
    }
    public Vector3 divide(double t){
        return new Vector3(
                e[0]*(1/t),
                e[1]*(1/t),
                e[2]*(1/t));
    }

    public final double lengthSquared(){
        return e[0]*e[0]+e[1]*e[1]+e[2]*e[2];
    }

    public boolean nearZero(){
        final double s =1e-8;
        return (Math.abs(e[0])<s)&&(Math.abs(e[1])<s)&&(Math.abs(e[2])<s);
    }

    //向量的模
    public final double length(){
        return Math.sqrt(lengthSquared());
    }

    //点乘
    public double dot(final Vector3 v){
        return e[0]*v.e[0]+
                e[1]*v.e[1]+
                e[2]*v.e[2];
    }

    //叉乘
    public Vector3 cross(final Vector3 v){
        return new Vector3(
                e[1] * v.e[2] - e[2] * v.e[1],
                e[2] * v.e[0] - e[0] * v.e[2],
                e[0] * v.e[1] - e[1] * v.e[0]);
    }
    public Vector3 random(double min, double max){
        return new Vector3(randomDouble(min,max), randomDouble(min,max),randomDouble(min,max));
    }

    //归一化向量（单位向量）
    public Vector3 unitVector(){
        double len=length();
        return divide(len);
    }

    //归一化随机点（单位球上取随机点）
    public Vector3 randomUnitVector(){
        //光线均匀分布，否则光线会在更大范围内扩散
        //光线会高概率靠近法线，低概率向偏角散射，需要归一化让光线高概率靠近法线同时均匀分布
        return randomInUnitSphere().unitVector();
    }

    //球体随机点
    public Vector3 randomInUnitSphere(){
        while(true){
            Vector3 p = random(-1,1);
            if(p.lengthSquared()>=1)continue;//随机点局限在单位球体内（随机点在球体外则重新生成，直到在球体内）
            return p;
        }
    }

    public Vector3 randomInHemisphere(final Vector3 normal){
        Vector3 inUnitSphere = randomInUnitSphere();
        //随机点和法线在同一半球上
        if(inUnitSphere.dot(normal)>0.0)return inUnitSphere;
        return inUnitSphere.nan();
    }

    //反射方向向量
    public Vector3 reflect(final Vector3 v, final Vector3 n){
        //反射光线为v+2b*n, b为v到表面的高度, b=dot(v,n)
        //因为入射光线v指向表面内，b为负数，所以用减号，即v-2b*n
        return v.minus(n.multiply(2*v.dot(n)));
    }

    //折射方向向量
    public Vector3 refract(final Vector3 uv, final Vector3 n, double etaiOverEtat){
        double cosTheta=Math.min(n.dot(uv.nan()),1.0);
        Vector3 rOutPerp=uv.plus(n.multiply(cosTheta)).multiply(etaiOverEtat);
        Vector3 rOutParallel = n.multiply(-Math.sqrt(Math.abs(1.0-rOutPerp.lengthSquared())));
        return rOutPerp.plus(rOutParallel);
    }
    //散焦随机点
    public Vector3 randomInUnitDisk(){
        while(true){
            Vector3 p = new Vector3(randomDouble(-1,1),randomDouble(-1,1),0);
            if(p.lengthSquared()>=1)continue;
            return p;
        }
    }
}
