import RayTracing.Scene;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DrawPanel extends JPanel {

    private Scene scene;
    private  BufferedImage image;
    private ArrayList<BufferedImage> animeImages;

    private boolean isAnime=false;
    private int frameId=0;
    private int mode;

    DrawPanel(Scene scene){
        animeImages=new ArrayList<BufferedImage>();
        this.scene=scene;
    }
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        if(!isAnime){
            if(this.scene.getWorldLength()!=0){
                if(this.scene.getMode()==0){
                    image=scene.getPreviewScene();
                    g.drawImage(image,getWidth()/2-(image.getWidth()/2),getHeight()/2-(image.getHeight()/2),this);

                }
                else if(this.scene.getMode()==3) {
                    image = scene.getScene();
                    g.drawImage(image,getWidth()/2-(image.getWidth()/2),getHeight()/2-(image.getHeight()/2),this);
                    setMode(-1);
                }
                else{
                    g.drawImage(image,getWidth()/2-(image.getWidth()/2),getHeight()/2-(image.getHeight()/2),this);
                }

            }
        }
        else{
            if(!animeImages.isEmpty()){
                System.out.println(frameId);
                g.drawImage(animeImages.get(frameId),getWidth()/2-(image.getWidth()/2),getHeight()/2-(image.getHeight()/2),this);
            }
        }
    }
    public void setMode(int mode){
        scene.setMode(mode);
    }
    public void setIsAnime(boolean isAnime){
        this.isAnime=isAnime;
    }
    public void setFrameId(int id){
        this.frameId = id;
    }
    public void saveImage(String filePath){
        File file = new File(filePath);
        try{
            ImageIO.write(image,"jpg",file);
        }catch (IOException e){
            System.out.print("文件读写异常");
        }
    }

    public void renderAnimation(ArrayList<AnimeFrame> frames){
        animeImages.clear();
        scene.setMode(3);
        for(int i=0;i<frames.size();i++){
//            scene.getCamera().setFrameCamera(
//                    frames.get(i).getCameraPosition(),
//                    frames.get(i).getCameraLookAt(),
//                    frames.get(i).getCameraUp(),
//                    frames.get(i).getCameraVfov()
//            );
            scene.getWorld().setFrameObjects(frames.get(i).getSceneObjectsPosition());
            BufferedImage img =scene.getScene();
            animeImages.add(img);
        }
        scene.setMode(0);
    }
}
