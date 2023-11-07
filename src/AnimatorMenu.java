import RayTracing.Camera;
import RayTracing.HittableList;
import RayTracing.Vector3;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

public class AnimatorMenu {
    private JPanel mainPanel;//主面板
    private JScrollPane framesPanel;//动画帧面板

    private int FPS;//帧数
    private int currentId;//当前帧

    public ArrayList<AnimeFrame> animeFrameList;//动画帧缓存

    private HittableList world;
    private Camera camera;


    private volatile boolean play = false;//是否播放
    private volatile boolean loop = false;//是否循环播放
    private boolean isRenderMode = false;//是否渲染模式
    //private boolean isCameraRecord = false;//是否摄像机视角

    private JButton playButton;//播放按钮
    private JButton loopButton;//循环按钮
    private JTextField frameIdValue;//当前帧id

    private AnimeThread animeThread;//动画线程

    private  JSlider animatorSlider;//动画滑动条
    public AnimatorMenu(DrawPanel previewPanel, HittableList world, Camera camera){
        this.mainPanel=new JPanel(new GridLayout(2,1));
        this.framesPanel=new JScrollPane();
        animeFrameList =new ArrayList<AnimeFrame>();

        this.world=world;
        this.camera=camera;

        //动画系统面板
        animatorSlider = new JSlider();
        animatorSlider.setMajorTickSpacing(10);
        animatorSlider.setMinorTickSpacing(1);
        animatorSlider.setPaintLabels(true);
        animatorSlider.setPaintTicks(true);


        animatorSlider.setMinimum(0);
        animatorSlider.setValue(0);



        JPanel playPanel = new JPanel();

        JButton replayButton = new JButton("重新播放");
        replayButton.setBackground(Color.LIGHT_GRAY);
        this.playButton = new JButton("播放");
        playButton.setBackground(Color.LIGHT_GRAY);
        this.loopButton = new JButton("循环播放On");
        loopButton.setBackground(Color.LIGHT_GRAY);
        playPanel.add(replayButton);
        playPanel.add(playButton);
        playPanel.add(loopButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!play){
                    continuePlay();
                    playButton.setText("暂停");
                    playButton.setBackground(Color.WHITE);

                    animeThread=new AnimeThread();
                    animeThread.start();
                }
                else{
                    stopPlay();
                    if(animeThread !=null){
                        animeThread.interrupt();
                    }
                    playButton.setText("播放");
                    playButton.setBackground(Color.LIGHT_GRAY);

                }
            }
        });
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animatorSlider.setValue(0);
                continuePlay();
                playButton.setText("暂停");
                playButton.setBackground(Color.WHITE);

                animeThread=new AnimeThread();
                animeThread.start();
            }
        });
        loopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!loop){
                    loop=true;
                    loopButton.setText("循环播放Close");
                    loopButton.setBackground(Color.WHITE);
                }
                else{
                    loop=false;
                    loopButton.setText("循环播放On");
                    loopButton.setBackground(Color.LIGHT_GRAY);
                }


            }
        });

        JPanel valuePanel = new JPanel();
        JLabel fpsLabel=new JLabel("帧数:");
        JTextField fpsValue=new JTextField(10);
        fpsValue.setText("24");

        FPS =Integer.valueOf(fpsValue.getText());

        JLabel idLabel=new JLabel("当前帧:");
        frameIdValue = new JTextField(10);
        frameIdValue.setText("0");

        valuePanel.add(fpsLabel);
        valuePanel.add(fpsValue);
        valuePanel.add(idLabel);
        valuePanel.add(frameIdValue);

        JPanel cludPanel = new JPanel();
        JButton addButton = new JButton("添加关键帧");
        addButton.setBackground(Color.LIGHT_GRAY);
//        JButton cameraRecordButton = new JButton("摄像机录制ON");
//        cameraRecordButton.setBackground(Color.LIGHT_GRAY);
        JButton renderModeButton = new JButton("渲染模式ON");
        renderModeButton.setBackground(Color.LIGHT_GRAY);
        JButton renderButton = new JButton("渲染");
        renderButton.setBackground(Color.LIGHT_GRAY);

        //动画帧滑条数值事件
        animatorSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!isRenderMode){
                    if(!animeFrameList.isEmpty()){
                        currentId = animatorSlider.getValue();
//                        Vector3 cameraPosition=null;
//                        Vector3 cameraLookAt=null;
//                        Vector3 cameraUp=null;
//                        double cameraVfov = 0.0;
//                        //摄像机录制
//                        if(isCameraRecord){
//                            animeFrameList.get(currentId).setCameraPosition(camera.getLookfrom());
//                            animeFrameList.get(currentId).setCameraLookAt(camera.getLookat());
//                            animeFrameList.get(currentId).setCameraUp(camera.getVup());
//                            animeFrameList.get(currentId).setCameraVfov(camera.getVfov());
//                        }
//                        cameraPosition = animeFrameList.get(currentId).getCameraPosition();
//                        cameraLookAt = animeFrameList.get(currentId).getCameraLookAt();
//                        cameraUp = animeFrameList.get(currentId).getCameraUp();
//                        cameraVfov = animeFrameList.get(currentId).getCameraVfov()
                        ArrayList<Vector3>scenePos= animeFrameList.get(currentId).getSceneObjectsPosition();
                        for(int i=0;i<world.length();i++){
                            world.getIndexObj(i).setPosition(scenePos.get(i));
                        }
//                        if(!isCameraRecord){
//                            camera.setFrameCamera(cameraPosition,cameraLookAt,cameraUp,cameraVfov);
//                            cameraMenu.setCameraPanelValue();
//                        }
//                        //摄像机录制结束
//                        if(currentId==animatorSlider.getMaximum()-1 && isCameraRecord){
//                            isCameraRecord=false;
//                            cameraRecordButton.setText("摄像机视角ON");
//                            cameraRecordButton.setBackground(Color.LIGHT_GRAY);
//                        }
                        if(currentId==animatorSlider.getMaximum()&& play){
                            stopPlay();
                            animeThread=null;
                            playButton.setText("播放");
                        }
                        previewPanel.getParent().repaint();
                    }
                    else{
                        animatorSlider.setValue(0);
                    }
                }
                else{
                    previewPanel.setFrameId(animatorSlider.getValue());
                    previewPanel.getParent().repaint();
                }
                frameIdValue.setText(String.valueOf(Integer.valueOf(animatorSlider.getValue())));
            }
        });
        //帧数事件监听
        fpsValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int speed=1;
                if(e.getWheelRotation()==-1){
                    fpsValue.setText(String.valueOf(Integer.valueOf(fpsValue.getText())+speed));
                    FPS =Integer.valueOf(fpsValue.getText());
                }
                if(e.getWheelRotation()==1){
                    if(Integer.valueOf(fpsValue.getText())>0){
                        fpsValue.setText(String.valueOf(Integer.valueOf(fpsValue.getText())-speed));
                        FPS =Integer.valueOf(fpsValue.getText());
                    }

                }
            }
        });


        cludPanel.add(addButton);
        //cludPanel.add(cameraRecordButton);
        cludPanel.add(renderModeButton);
        cludPanel.add(renderButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFrame();
            }
        });
//        cameraRecordButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(!isCameraRecord){
//                    isCameraRecord=true;
//                    cameraRecordButton.setText("摄像机录制CLOSE");
//                    cameraRecordButton.setBackground(Color.WHITE);
//                }
//            }
//        });
        renderModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isRenderMode){
                    isRenderMode =true;
                    renderModeButton.setText("渲染模式ON");
                    renderModeButton.setBackground(Color.LIGHT_GRAY);
                    previewPanel.setIsAnime(true);
                    previewPanel.getParent().repaint();
                }
                else{
                    isRenderMode =false;
                    renderModeButton.setText("渲染模式CLOSE");
                    renderModeButton.setBackground(Color.WHITE);
                    previewPanel.setIsAnime(false);
                    previewPanel.getParent().repaint();

                }
            }
        });
        renderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                previewPanel.renderAnimation(animeFrameList);
            }
        });

        JPanel framePanel = new JPanel(new GridLayout(3,1));
        framePanel.add(playPanel);
        framePanel.add(valuePanel);
        framePanel.add(cludPanel);


        //动画面板（垂直分隔面板）
        JSplitPane animatorPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                framePanel,
                animatorSlider);
        animatorPane.setDividerLocation(100);


        this.mainPanel.add(animatorPane);

    }

    public boolean getIsRender(){
        return this.isRenderMode;
    }

    public void stopPlay(){
        synchronized (this){
            this.play=false;
            notifyAll();
        }
    }
    public void continuePlay(){
        synchronized (this){
            this.play=true;
            notifyAll();
        }
    }
    public JPanel getMainPanel(){
        return this.mainPanel;
    }
    //添加关键帧
    public void addFrame(){
        if(animeFrameList.isEmpty()){
            AnimeFrame frame = new AnimeFrame(camera,world,0);
            animeFrameList.add(frame);
        }
        else{
            int startNum = animeFrameList.size();
            int endNum= animeFrameList.size()+FPS-2;

            AnimeFrame start=new AnimeFrame(animeFrameList.get(startNum-1));
            AnimeFrame end = new AnimeFrame(camera,world,endNum);

            int k=1;
            //中间帧
            for(int i=startNum;i<endNum;i++){

                AnimeFrame f = new AnimeFrame();

                f.setId(i);

//                //摄像机位置
//                f.setCameraPosition(camera.getLookfrom());
//                //摄像机看向
//                f.setCameraLookAt(camera.getLookat());
//                //摄像机向上
//                f.setCameraUp(camera.getVup());

                //场景物体位置
                ArrayList<Vector3>startScenePos = start.getSceneObjectsPosition();
                ArrayList<Vector3>endScenePos = end.getSceneObjectsPosition();
                ArrayList<Vector3>scenePos=new ArrayList<Vector3>();
                for(int j=0;j<startScenePos.size();j++){
                    Vector3 pos=new Vector3(
                            ((endScenePos.get(j).x()-startScenePos.get(j).x())/(double)FPS) * k + startScenePos.get(j).x(),
                            ((endScenePos.get(j).y()-startScenePos.get(j).y())/(double)FPS) * k + startScenePos.get(j).y(),
                            ((endScenePos.get(j).z()-startScenePos.get(j).z())/(double)FPS) * k + startScenePos.get(j).z()
                    );
                    scenePos.add(pos);
                }
                f.setSceneObjectsPosition(scenePos);

                this.animeFrameList.add(f);
                k++;
            }

            animeFrameList.add(end);


            animatorSlider.setMaximum(endNum);
            animatorSlider.setValue(endNum);
        }
    }

    private class AnimeThread extends Thread{
        @Override
        public void run(){

            while(animatorSlider.getValue() < animatorSlider.getMaximum()){
                if(play){
                    try {
                        Thread.sleep(33);
                        animatorSlider.setValue(animatorSlider.getValue()+1);
                        if(animatorSlider.getValue()==animatorSlider.getMaximum()-1&&loop==true){
                            animatorSlider.setValue(0);
                        }
                    }
                    catch (Exception e){
                        //e.printStackTrace(); // 打印异常堆栈信息
                    }

                }
                else{
                    synchronized (this) {
                        try {
                            this.wait(); // 在此处等待，直到被其他线程通知
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            play=false;

        }
    }
}
