package RayTracing;

import java.awt.image.BufferedImage;

public class Scene extends RayTracing {
    private HittableList world;
    private Camera camera;
    private int mode;

    public Vector3 rayColorFull(final Ray r, final Hittable world, int depth){
        HitRecord rec =new HitRecord();
        //递归层次小于0时，终止递归
        if(depth<=0){
            return new Vector3(0,0,0);
        }

        if(world.hit(r,0.001,posInfinity,rec)){
            Ray scattered = new Ray();
            Vector3 attenuation = new Vector3();
            if(rec.getMaterial().scatter(r,rec,attenuation,scattered)){
                return rayColorFull(scattered,world,depth-1).multiply(attenuation);
            }
            return new Vector3(0,0,0);
        }
        Vector3 unitDirection = r.getDirection().unitVector();
        double t= 0.5*(unitDirection.y()+1.0);
        Vector3 color=new Vector3(1.0,1.0,1.0);
        Vector3 color2=new Vector3(0.5,0.7,1.0);
        return color.multiply(1.0-t).plus(color2.multiply(t));

    }

    public Vector3 rayColorPdf(final Ray r, final Vector3 background, final Hittable world, final Hittable lights, int depth){
        HitRecord rec =new HitRecord();
        //递归层次小于0时，终止递归
        if(depth<=0){
            return new Vector3(0,0,0);
        }

        //光线没有碰撞到物体
        //漫反射光线击中物体时并不会是t=0,所以t_min=0.001）
        if(!world.hit(r,0.001,posInfinity,rec)){
            return background;
        }

        ScatterRecord srec =new ScatterRecord();//散射信息
        Vector3 emitted = rec.getMaterial().emitted(r,rec,rec.getU(),rec.getV(),rec.getPoint());//发射光（光源才发光颜色，否则发射黑色光）

        //物体表面不散射光
        if(!rec.getMaterial().scatter(r,rec,srec)){
            return emitted;//材质是光源材质就发射光颜色，不是光源材质就发射黑色光（既不发光）
        }

        //镜面反射
        if(srec.getIsSpecular()){
            //递归,这次镜面反射光作为下次递归的入射光
            return srec.getAttenuation().multiply(rayColorPdf(srec.getSpecularRay(),background,world,lights,depth-1));
        }
        //概率密度函数
        HittablePdf light=new HittablePdf(lights,rec.getPoint());//光源概率密度函数
        MixturePdf p=new MixturePdf(light,srec.getPdf());//光源概率密度和镜面反射光源概率密度的混合概率密度函数
        //散射光
        Ray scattered = new Ray(rec.getPoint(),p.generate(),r.getTime());
        double pdfVal=p.value(scattered.getDirection());//概率密度函数值

        //递归，这次散射光作为下次递归的入射光
        return emitted.plus(
                srec.getAttenuation().multiply(
                        rec.getMaterial().scatteringPdf(r,rec,scattered)).multiply(
                        rayColorPdf(scattered,background,world,lights,depth-1)).divide(pdfVal));

    }

    public Vector3 rayColor(final Ray r, final Vector3 background, final Hittable world, int depth){
        HitRecord rec =new HitRecord();
        //递归层次小于0时，终止递归
        if(depth<=0){
            return new Vector3(0,0,0);
        }
        //光线没有碰撞到物体
        //漫反射光线击中物体时并不会是t=0,所以t_min=0.001）
        if(!world.hit(r,0.001,posInfinity,rec)){
            return background;
        }
        Ray scattered = new Ray();
        Vector3 attenuation = new Vector3();
        Vector3 emitted = rec.getMaterial().emitted(rec.getU(),rec.getV(),rec.getPoint());
        //物体表面不散射光
        if(!rec.getMaterial().scatter(r,rec,attenuation,scattered)){
            return emitted;
        }
        return emitted.plus(rayColor(scattered,background,world,depth-1).multiply(attenuation));
    }

    public Vector3 rayColorPreview(final Ray r, final Hittable world){
        HitRecord rec =new HitRecord();
        if(world.hit(r,0.001,posInfinity,rec)){
            return rec.getMaterial().getColor();
        }
        Vector3 unitDirection = r.getDirection().unitVector();
        double t= 0.5*(unitDirection.y()+1.0);
        Vector3 color=new Vector3(1.0,1.0,1.0);
        Vector3 color2=new Vector3(0.5,0.7,1.0);
        return color.multiply(1.0-t).plus(color2.multiply(t));
    }

    public Scene(HittableList world, Camera cam, int mode){
        this.world=world;
        this.camera =cam;
        this.mode=mode;
    }

    public int getWorldLength(){
        return world.length();
    }
    public  void setMode(int mode){
        this.mode=mode;
    }
    public int getMode(){
        return this.mode;
    }
    public BufferedImage getPreviewScene(){
        final double aspectRatio= camera.getAspectRatio();
        final int imageWidth= camera.getImgWidth();
        final int imageHeight = (int)(imageWidth/aspectRatio);

        RTColor color=new RTColor();
        BufferedImage result = new BufferedImage(imageWidth,imageHeight, BufferedImage.TYPE_INT_RGB);

        for(int y = imageHeight-1; y>=0;--y){
            for(int x = 0; x<imageWidth;++x){

                Vector3 pixelColor=new Vector3(0,0,0);
                double u ;
                double v ;
                Ray r;
                u = (double)(x)/(imageWidth-1);
                v = (double)((imageHeight-1)-y)/(imageHeight-1);
                r = camera.getRay(u,v);
                pixelColor = pixelColor.plus(rayColorPreview(r,world));
                color.writeColor(x,y,result,pixelColor,1);
            }
        }
        return result;
    }

    public BufferedImage getScene(){
         double aspectRatio= camera.getAspectRatio();
         int imageWidth= camera.getImgWidth();
         int imageHeight = (int)(imageWidth/aspectRatio);
         int samplesPerPixel= camera.getSamplesPerPixel();
         int maxDepth= camera.getMaxDepth();
        //场景
        Vector3 background=new Vector3(0,0,0);
        //光源
        HittableList lights = new HittableList();
        RTColor color=new RTColor();
        BufferedImage result = new BufferedImage(imageWidth,imageHeight, BufferedImage.TYPE_INT_RGB);
        for(int y = imageHeight-1; y>=0;--y){
            for(int x = 0; x<imageWidth;++x){
                Vector3 pixelColor=new Vector3(0,0,0);
                double u ;
                double v ;
                Ray r;
                switch (mode){
                    case 1:
                        for(int s = 0; s<samplesPerPixel;++s){
                            u = (double)(x+randomDouble())/(imageWidth-1);
                            v = (double)((imageHeight-1)-y+randomDouble())/(imageHeight-1);
                            r = camera.getRay(u,v);
                            pixelColor = pixelColor.plus(rayColorPdf(r,background,world,lights,maxDepth));
                        }
                        break;
                    case 2:
                        for(int s = 0; s<samplesPerPixel;++s){
                            u = (double)(x+randomDouble())/(imageWidth-1);
                            v = (double)((imageHeight-1)-y+randomDouble())/(imageHeight-1);
                            r = camera.getRay(u,v);
                            pixelColor = pixelColor.plus(rayColorFull(r,world,maxDepth));
                        }
                        break;
                    case 3:
                        for(int s = 0; s<samplesPerPixel;++s){
                            u = (double)(x+randomDouble())/(imageWidth-1);
                            v = (double)((imageHeight-1)-y+randomDouble())/(imageHeight-1);
                            r = camera.getRay(u,v);
                            pixelColor = pixelColor.plus(rayColor(r,background,world,maxDepth));
                        }
                        break;
                    default:
                        System.out.println("无效输入");
                        break;
                }
                color.writeColor(x,y,result,pixelColor,samplesPerPixel);
            }
        }

        return result;
    }

    public Camera getCamera(){
        return this.camera;
    }
    public HittableList getWorld(){
        return this.world;
    }


    public BufferedImage getSceneMulti(){
        final double aspectRatio= camera.getAspectRatio();
        final int imageWidth= camera.getImgWidth();
        final int imageHeight = (int)(imageWidth/aspectRatio);
        int samplesPerPixel= camera.getSamplesPerPixel();
        final int maxDepth= camera.getMaxDepth();
        BufferedImage result = new BufferedImage(imageWidth,imageHeight, BufferedImage.TYPE_INT_RGB);

        //场景
        Vector3 background=new Vector3(0,0,0);
        //光源
        HittableList lights = new HittableList();
        for(int y = imageHeight-1; y>=0;--y) {
            for (int x = 0; x < imageWidth; ++x) {

                Vector3 pixelColor = new Vector3(0, 0, 0);
                double u;
                double v;
                Ray r;
                switch (mode) {
                    case 1:
                        for (int s = 0; s < samplesPerPixel; ++s) {
                            u = (double) (x + randomDouble()) / (imageWidth - 1);
                            v = (double) ((imageHeight - 1) - y + randomDouble()) / (imageHeight - 1);
                            r = camera.getRay(u, v);
                            pixelColor = pixelColor.plus(rayColorPdf(r, background, world, lights, maxDepth));
                        }
                        break;
                    case 2:
                        for (int s = 0; s < samplesPerPixel; ++s) {
                            u = (double) (x + randomDouble()) / (imageWidth - 1);
                            v = (double) ((imageHeight - 1) - y + randomDouble()) / (imageHeight - 1);
                            r = camera.getRay(u, v);
                            pixelColor = pixelColor.plus(rayColorFull(r, world, maxDepth));
                        }
                        break;
                    case 3:
                        for (int s = 0; s < samplesPerPixel; ++s) {
                            u = (double) (x + randomDouble()) / (imageWidth - 1);
                            v = (double) ((imageHeight - 1) - y + randomDouble()) / (imageHeight - 1);
                            r = camera.getRay(u, v);
                            pixelColor = pixelColor.plus(rayColor(r, background, world, maxDepth));
                        }
                        break;
                    default:
                        System.out.println("无效输入");
                        break;
                }

            }
        }

        return result;
    }


}

