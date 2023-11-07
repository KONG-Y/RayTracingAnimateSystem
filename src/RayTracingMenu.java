import RayTracing.Camera;
import RayTracing.HittableList;
import RayTracing.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RayTracingMenu extends JFrame{
    private  HittableList world;//场景物体
    private Camera camera;//摄像机
    private DrawPanel previewPanel;//显示窗口

    private JProgressBar drawProgressBar;//进度条

    private JPanel cameraPanel;//摄像机属性面板
    private JPanel scenePanel;//场景属性面板
    private JFrame menuFrame;//菜单
    private boolean run=false;//是否渲染
    private Scene scene;//场景对象


    RayTracingMenu(){
        menuFrame = new JFrame("光线追踪");
        //menuFrame.setBounds(400,300,1200,800);
        menuFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //menuFrame.setUndecorated(true);//真正全屏（边框都没有）
        menuFrame.setVisible(true);
        menuFrame.setResizable(true);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        camera=new Camera();

        world=new HittableList();


        drawProgressBar = new JProgressBar();
        drawProgressBar.setStringPainted(true);

        scene=new Scene(world,camera,0);
        previewPanel=new DrawPanel(scene);

    }
    public void Menu(){
        //相机
        CameraMenu cameraMenu=new CameraMenu(previewPanel,camera);
        cameraPanel=cameraMenu.setCameraPanel();//相机面板初试化

        //进度条
        drawProgressBar.setMinimum(0);
        drawProgressBar.setMaximum(camera.getImgPixel());
        drawProgressBar.setValue(0);


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        JFrame sceneFrame=new JFrame("场景设置");
//        sceneFrame.setBounds(400,300,800,500);
//        sceneFrame.setBackground(new Color(175,114,114));
//        sceneFrame.setVisible(false);
//        sceneFrame.setResizable(true);
//        sceneFrame.setLocationRelativeTo(null);
//        sceneFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        SceneMenu sceneMenu = new SceneMenu(previewPanel,this.world,this.camera,cameraMenu);//监视器
        scenePanel = sceneMenu.getSceneMenu();

        JPanel buttonPanel = new JPanel(new GridLayout(4,1));

        JButton button2 = new JButton("启动");
        JButton button3 = new JButton("渲染");
        JButton button4 = new JButton("导出");
        JButton button5 = new JButton("退出");

        Font f = new Font("微软黑体",Font.BOLD,50);
        button2.setFont(f);
        button3.setFont(f);
        button4.setFont(f);
        button5.setFont(f);

        button2.setBackground(Color.LIGHT_GRAY);
        button3.setBackground(Color.LIGHT_GRAY);
        button4.setBackground(Color.LIGHT_GRAY);
        button5.setBackground(Color.LIGHT_GRAY);

        //buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);


        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!run){
                    run=true;
                    drawProgressBar.setValue(0);
                    previewPanel.setMode(0);
                    previewPanel.getParent().repaint();
                    button2.setText("暂停");
                    button2.setBackground(Color.WHITE);
                }
                else{
                    run=false;
                    button2.setText("启动");
                    button2.setBackground(Color.LIGHT_GRAY);
                }
                cameraMenu.setRun(run);
                button2.getParent().repaint();
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawProgressBar.setValue(0);
                previewPanel.setMode(3);
                previewPanel.getParent().repaint();
            }
        });
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("./");
                fc.setMultiSelectionEnabled(false);
                fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                fc.setFileHidingEnabled(true);
                fc.setAcceptAllFileFilterUsed(false);
                int returnValue  = fc.showOpenDialog(null);
                String filePath="";
                if(returnValue==JFileChooser.APPROVE_OPTION){
                    filePath=fc.getSelectedFile().toString();
                }
                previewPanel.saveImage(filePath);
            }
        });
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
        });

        //动画系统面板
        AnimatorMenu animatorMenu = new AnimatorMenu(previewPanel,world,camera);
        JPanel animatorPane = animatorMenu.getMainPanel();



        //相机面板(水平分隔面板)
        JSplitPane hSplitPane1 = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                true,
                cameraPanel,
                buttonPanel

        );
        hSplitPane1.setDividerLocation(545);


        //垂直分隔面板2
        JSplitPane vSplitPane2 = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                scenePanel,
                hSplitPane1
                );
        vSplitPane2.setDividerLocation(480);

        //垂直分隔面板3
        JSplitPane vSplitPanel3 = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                previewPanel,
                animatorPane);
        vSplitPanel3.setDividerLocation(500);



        //水平分隔面板
        JSplitPane hSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                true,
                vSplitPane2,
                vSplitPanel3

                );
        hSplitPane.setDividerLocation(700);

        menuFrame.setContentPane(hSplitPane);
        menuFrame.revalidate();

    }


    public static void main(String[] args){
        RayTracingMenu menu=new RayTracingMenu();
        menu.Menu();
    }
}
