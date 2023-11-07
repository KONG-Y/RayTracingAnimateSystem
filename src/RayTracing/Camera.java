package RayTracing;

public class Camera extends RayTracing{
    //三维物体投影到视窗上，用视窗上的像素表示出
    //视窗用来发射相机的光线，设立渲染图raster image的像素尺寸
    //把raster image 贴在视窗前面（视窗也可叫图像平面image plane）
    private String type="Camera";
    private Vector3 origin;         //视窗起点
    private Vector3 lowerLeftCorner;//视窗左下角坐标
    private Vector3 horizontal;     //视窗向右向量
    private Vector3 vertical;       //视窗向上向量
    private Vector3 u,v,w;          //相机的坐标轴(用来确定视窗的方向)
    private double lensRadius;      //透镜半径

    private Vector3 lookfrom;
    private Vector3 lookat;
    private Vector3 vup;
    private double vfov;
    private double aspectRatio;
    private double aperture;    //光圈（值越大越模糊）
    private double focusDist;
    private int imgWidth;
    private int samplesPerPixel;
    private int maxDepth;
    private double time0=0;           //快门开始时间
    private double time1=0;           //快门结束时间



    public Camera(){
    }

//    public Camera(Vector3 lookfrom,     //相机位置
//                  Vector3 lookat,       //相机指向
//                  Vector3 vup,          //相机向上向量（表示相机倾斜程度）
//                  double vfov,	        //垂直视角
//                  double aspectRatio,   //宽高比
//                  double aperture,      //光圈（值越大越模糊）
//                  double focusDist,	    //焦距（物体到相机的距离不等于焦距，就会出现模糊）
//                  int imgWidth,
//                  int samplesPerPixel,
//                  int maxDepth,
//                  double _time0,
//                  double _time1){
//
//        this.aspectRatio=aspectRatio;
//        this.imgWidth=imgWidth;
//        this.samplesPerPixel=samplesPerPixel;
//        this.maxDepth=maxDepth;
//
//        //相机视锥
//        double theta = degreesToRadians(vfov);              //把vfov角度变为弧度
//        double h = Math.tan(theta/2);                       //半视角尺度（长度）
//        double viewportHeight=2.0*h;                        //视角高
//        double viewportWidth = aspectRatio*viewportHeight;  //视角宽
//
//        //相机坐标轴（确定相机的旋转向，视窗的方向
//        w=lookfrom.minus(lookat).unitVector();   //视线方向向量（相机看向的方向为-w,w的反方向）
//        u=vup.cross(w).unitVector();             //相机u轴方向向量（x）
//        v=w.cross(u);                           //相机v轴方向向量(y)
//
//        //视窗（发射相机的光线，只渲染射到的范围，三维物体投影到上面，用像素表示出来）
//        //视窗当成屏幕，在世界坐标下，我们只看到屏幕范围上的物体
//        origin = lookfrom;
//        horizontal = u.multiply(focusDist*viewportWidth);
//        vertical = v.multiply(focusDist*viewportHeight);
//
//        lowerLeftCorner=origin.minus(horizontal.divide(2)).minus(vertical.divide(2)).minus(w.multiply(focusDist));
//
//
//        lensRadius = aperture/2;
//        time0=_time0;
//        time1=_time1;
//    }



    public void setCamera(Vector3 lookfrom,     //相机位置
                          Vector3 lookat,       //相机指向
                          Vector3 vup,          //相机向上向量（表示相机倾斜程度）
                          double vfov,            //垂直视角
                          double aspectRatio,   //宽高比
                          double aperture,      //光圈（值越大越模糊）
                          double focusDist,        //焦距（物体到相机的距离不等于焦距，就会出现模糊）
                          int imgWidth,
                          int samplesPerPixel,
                          int maxDepth,
                          double time0,
                          double time1){
        this.lookfrom=lookfrom;
        this.lookat = lookat;
        this.vup=vup;
        this.vfov=vfov;
        this.aspectRatio=aspectRatio;
        this.aperture=aperture;
        this.focusDist=focusDist;
        this.imgWidth=imgWidth;
        this.samplesPerPixel=samplesPerPixel;
        this.maxDepth=maxDepth;
        this.time0=time0;
        this.time1=time1;

        //相机视锥
        double theta = degreesToRadians(vfov);              //把vfov角度变为弧度
        double h = Math.tan(theta/2);                       //半视角尺度（长度）
        double viewportHeight=2.0*h;                        //视角高
        double viewportWidth = aspectRatio*viewportHeight;  //视角宽

        //相机坐标轴（确定相机的旋转向，视窗的方向
        w=lookfrom.minus(lookat).unitVector();   //视线方向向量（相机看向的方向为-w,w的反方向）
        u=vup.cross(w).unitVector();             //相机u轴方向向量（x）
        v=w.cross(u);                           //相机v轴方向向量(y)

        //视窗（发射相机的光线，只渲染射到的范围，三维物体投影到上面，用像素表示出来）
        //视窗当成屏幕，在世界坐标下，我们只看到屏幕范围上的物体
        origin = lookfrom;
        horizontal = u.multiply(focusDist*viewportWidth);
        vertical = v.multiply(focusDist*viewportHeight);

        lowerLeftCorner=origin.minus(horizontal.divide(2)).minus(vertical.divide(2)).minus(w.multiply(focusDist));


        lensRadius = aperture/2;


    }
    public int getImgPixel(){
        int imgHeight = (int)(imgWidth/aspectRatio);
        return imgHeight*imgWidth;
    }

    public void setFrameCamera(Vector3 pos, Vector3 lookat, Vector3 up,double vfov){
        this.lookfrom=pos;
        this.lookat=lookat;
        this.vup=up;
        this.vfov=vfov;
    }

    public Vector3 getLookfrom(){return this.lookfrom;}
    public Vector3 getLookat(){return this.lookat;}
    public Vector3 getVup(){return this.vup;}
    public double getVfov(){return this.vfov;}
    public double getAspectRatio(){
        return this.aspectRatio;
    }
    public double getAperture(){return this.aperture;}
    public double getFocusDist(){return this.focusDist;}
    public int getImgWidth(){
        return this.imgWidth;
    }
    public int getSamplesPerPixel(){
        return this.samplesPerPixel;
    }
    public int getMaxDepth(){
        return this.maxDepth;
    }
    public final Ray getRay(double s, double t){
        Vector3 rd = new Vector3();
        rd = rd.randomInUnitDisk().multiply(lensRadius);//散焦点
        Vector3 offset = u.multiply(rd.x()).plus(v.multiply(rd.y()));//散焦偏差值

        return new Ray(
                origin.plus(offset),
                lowerLeftCorner.plus(horizontal.multiply(s)).plus(vertical.multiply(t)).minus(origin).minus(offset),
                randomDouble(time0,time1)
        );
    }
}
