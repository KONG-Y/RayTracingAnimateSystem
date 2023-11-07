package RayTracing;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;

public class RTColor extends RayTracing{

    public  void  writeColor(int x, int y, BufferedImage image, Vector3 pixelColor, int samplePerPixel){
        double r = pixelColor.x();
        double g = pixelColor.y();
        double b = pixelColor.z();
        //System.out.println(r+" "+g+" "+b);
        //当RGB为NaN时用0代替（NaN会出现斑点）
        if(r!=r)r=0.0;
        if(g!=g)g=0.0;
        if(b!=b)b=0.0;

        //RGB通道各取所有样本数的平均值（反走样）
        double scale = 1.0/samplePerPixel;
        r=Math.sqrt(scale*r);
        g=Math.sqrt(scale*g);
        b=Math.sqrt(scale*b);
        image.setRGB(x,y,new java.awt.Color(
                (int)(256*clamp(r,0.0,0.999)),
                (int)(256*clamp(g,0.0,0.999)),
                (int)(256*clamp(b,0.0,0.999))).getRGB());

    }


    public void writeColor(FileWriter fw, Vector3 pixelColor, int samplePerPixel){
        double r = pixelColor.x();
        double g = pixelColor.y();
        double b = pixelColor.z();
        //System.out.println(r+" "+g+" "+b);
        //当RGB为NaN时用0代替（NaN会出现斑点）
        if(r!=r)r=0.0;
        if(g!=g)g=0.0;
        if(b!=b)b=0.0;

        //RGB通道各取所有样本数的平均值（反走样）
        double scale = 1.0/samplePerPixel;
        r=Math.sqrt(scale*r);
        g=Math.sqrt(scale*g);
        b=Math.sqrt(scale*b);

        try{
        fw.write((int)(256*clamp(r,0.0,0.999))+" "
                +(int)(256*clamp(g,0.0,0.999))+" "
                +(int)(256*clamp(b,0.0,0.999))+"\n");
        } catch (IOException e) {
            System.out.print("文件读写异常");
        }
    }
}
