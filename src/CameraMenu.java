import RayTracing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class CameraMenu {
    private DrawPanel previewPanel;
    private Camera camera;
    private boolean run;

    private JTextField widthValue;
    private JComboBox aspectCB;

    private JTextField xPosValue;
    private JTextField yPosValue;
    private JTextField zPosValue;

    private JTextField xAtValue;
    private JTextField yAtValue;
    private JTextField zAtValue;

    private JTextField xUpValue;
    private JTextField yUpValue;
    private JTextField zUpValue;

    private JTextField vfValue;
    private JTextField apertureValue;
    private  JTextField dtfValue;
    private JTextField samplesValue;
    private JTextField depthValue;

    private JTextField mouseSpeedValue;
    private JTextField keySpeedValue;



    public CameraMenu(DrawPanel previewPanel, Camera camera){
        this.previewPanel=previewPanel;
        this.camera=camera;
    }
    public JPanel setCameraPanel(){
        previewPanel.setMode(0);
        JPanel cameraPanel= new JPanel(new GridLayout(10,1));

        JPanel pixelSizePanel=new JPanel();
        JLabel pixelLabel =new JLabel("分辨率设置：");
        JLabel widthLabel = new JLabel("宽");
        widthValue = new JTextField(5);
        widthValue.setText("600");

        widthValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int speed = 1;
                if(e.getWheelRotation()==-1){
                    widthValue.setText(String.valueOf(Integer.valueOf(widthValue.getText())+speed));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    if(Integer.valueOf(widthValue.getText())>0){
                        widthValue.setText(String.valueOf(Integer.valueOf(widthValue.getText())-speed));
                        getCameraPanelValue();
                    }

                }
            }
        });
        widthValue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCameraPanelValue();
            }
        });

        JLabel aspectLabel = new JLabel("宽高比");
        aspectCB = new JComboBox();
        aspectCB.addItem("16:9");
        aspectCB.addItem("3:2");
        aspectCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCameraPanelValue();
            }
        });

        pixelSizePanel.add(pixelLabel);
        pixelSizePanel.add(widthLabel);
        pixelSizePanel.add(widthValue);
        pixelSizePanel.add(aspectLabel);
        pixelSizePanel.add(aspectCB);

        JPanel cameraPosPanel = new JPanel();
        JLabel cameraPosLabel = new JLabel("相机位置：");
        JLabel xPosLabel=new JLabel("X");
        JLabel yPosLabel=new JLabel("Y");
        JLabel zPosLabel=new JLabel("Z");
        xPosValue=new JTextField(5);
        yPosValue=new JTextField(5);
        zPosValue=new JTextField(5);
        xPosValue.setText("0");
        yPosValue.setText("60");
        zPosValue.setText("-300");

        xPosValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    xPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xPosValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    xPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xPosValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });
        yPosValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    yPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yPosValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    yPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yPosValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });
        zPosValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    zPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zPosValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    zPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zPosValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });

        cameraPosPanel.add(cameraPosLabel);
        cameraPosPanel.add(xPosLabel);
        cameraPosPanel.add(xPosValue);
        cameraPosPanel.add(yPosLabel);
        cameraPosPanel.add(yPosValue);
        cameraPosPanel.add(zPosLabel);
        cameraPosPanel.add(zPosValue);

        JPanel lookAtPanel = new JPanel();
        JLabel lookAtLabel = new JLabel("指向方向：");
        JLabel xAtLabel=new JLabel("X");
        JLabel yAtLabel=new JLabel("Y");
        JLabel zAtLabel=new JLabel("Z");
        xAtValue=new JTextField(5);
        yAtValue=new JTextField(5);
        zAtValue=new JTextField(5);
        xAtValue.setText("0");
        yAtValue.setText("0");
        zAtValue.setText("0");

        xAtValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    xAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xAtValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    xAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xAtValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });
        yAtValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    yAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yAtValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    yAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yAtValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });
        zAtValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    zAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zAtValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    zAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zAtValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });

        lookAtPanel.add(lookAtLabel);
        lookAtPanel.add(xAtLabel);
        lookAtPanel.add(xAtValue);
        lookAtPanel.add(yAtLabel);
        lookAtPanel.add(yAtValue);
        lookAtPanel.add(zAtLabel);
        lookAtPanel.add(zAtValue);

        JPanel upPanel = new JPanel();
        JLabel upLabel = new JLabel("相机向上：");
        JLabel xUpLabel=new JLabel("X");
        JLabel yUpLabel=new JLabel("Y");
        JLabel zUpLabel=new JLabel("Z");
        xUpValue=new JTextField(5);
        yUpValue=new JTextField(5);
        zUpValue=new JTextField(5);
        xUpValue.setText("0");
        yUpValue.setText("1");
        zUpValue.setText("0");

        xUpValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    xUpValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xUpValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    xUpValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xUpValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });
        yUpValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    yUpValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yUpValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    yUpValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yUpValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });
        zUpValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    zUpValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zUpValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    zUpValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zUpValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
            }
        });

        upPanel.add(upLabel);
        upPanel.add(xUpLabel);
        upPanel.add(xUpValue);
        upPanel.add(yUpLabel);
        upPanel.add(yUpValue);
        upPanel.add(zUpLabel);
        upPanel.add(zUpValue);

        JPanel vfPanel = new JPanel();
        JLabel vfLabel = new JLabel("垂直视角");
        vfValue = new JTextField(5);
        vfValue.setText("90");

        vfPanel.add(vfLabel);
        vfPanel.add(vfValue);

        vfValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    vfValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(vfValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(vfValue.getText())>0){
                        vfValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(vfValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                        getCameraPanelValue();
                    }

                }
            }
        });

        JPanel aperturePanel = new JPanel();
        JLabel apertureLabel = new JLabel("光圈");
        apertureValue = new JTextField(5);
        apertureValue.setText("0.0");
        aperturePanel.add(apertureLabel);
        aperturePanel.add(apertureValue);

        apertureValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    apertureValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(apertureValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(apertureValue.getText())>0){
                        apertureValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(apertureValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                        getCameraPanelValue();
                    }

                }
            }
        });

        JPanel dtfPanel = new JPanel();
        JLabel dtfLabel = new JLabel("焦距");
        dtfValue = new JTextField(5);
        dtfValue.setText("10.0");
        dtfPanel.add(dtfLabel);
        dtfPanel.add(dtfValue);

        dtfValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = 1;
                if(e.getWheelRotation()==-1){
                    dtfValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(dtfValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(dtfValue.getText())>0){
                        dtfValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(dtfValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                        getCameraPanelValue();
                    }

                }
            }
        });

        JPanel valuePanel= new JPanel();
        valuePanel.add(vfPanel);
        valuePanel.add(aperturePanel);
        valuePanel.add(dtfPanel);

        JPanel samplesPanel = new JPanel();
        JLabel samplesLabel = new JLabel("采样数");
        samplesValue = new JTextField(5);
        samplesValue.setText("10");
        samplesPanel.add(samplesLabel);
        samplesPanel.add(samplesValue);

        JLabel depthLabel = new JLabel("最大深度");
        depthValue = new JTextField(5);
        depthValue.setText("50");
        samplesPanel.add(depthLabel);
        samplesPanel.add(depthValue);


        JPanel speedPanel = new JPanel();

        JLabel mouseLabel = new JLabel("鼠标速度");
        mouseSpeedValue=new JTextField(5);
        mouseSpeedValue.setText("0.5");
        JLabel keyLabel = new JLabel("键盘速度");
        keySpeedValue=new JTextField(5);
        keySpeedValue.setText("8");
        speedPanel.add(mouseLabel);
        speedPanel.add(mouseSpeedValue);
        speedPanel.add(keyLabel);
        speedPanel.add(keySpeedValue);



        JPanel buttonPanel = new JPanel();
        JButton button = new JButton("确认");
        buttonPanel.add(button);

        //相机初试化
        camera.setCamera(new Vector3(0.0,0.0,0.0),new Vector3(0.0,0.0,0.0),new Vector3(0.0,1.0,0.0),90.0,1.777,0.0,10.0,600,100,50,0.0,1.0);

        //相机属性设置
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCameraPanelValue();
            }
        });

        //鼠标滚轮，控制相机镜头距离（不是相机移动）
        previewPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(run){
                    if(e.getWheelRotation()==-1){
                        vfValue.setText(String.valueOf(Double.valueOf(vfValue.getText())+1.0));
                    }
                    if(e.getWheelRotation()==1){
                        vfValue.setText(String.valueOf(Double.valueOf(vfValue.getText())-1.0));
                    }
                    getCameraPanelValue();
                }

            }
        });

        Point clickedPos=new Point();
        //鼠标点击，获取鼠标点击的初始位置
        previewPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(run){
                    previewPanel.requestFocusInWindow();//设置键盘焦点在显示面板上
                    clickedPos.setLocation(e.getPoint());//获得当前鼠标点击的位置
                }

            }
        });
        //鼠标拖动
        previewPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e){
                if(run){
                    double speed=Double.valueOf(mouseSpeedValue.getText());
                    speed = Double.valueOf(new BigDecimal(speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());//相机转向速度的
                    double moveX = (clickedPos.getX()-e.getPoint().getX())*speed;
                    double moveY =(clickedPos.getY()-e.getPoint().getY())*speed;
                    xAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xAtValue.getText())+moveX).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    yAtValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yAtValue.getText())+moveY).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    getCameraPanelValue();

                }
            }
        });


        //键盘事件
        previewPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
            @Override
            public void keyPressed(KeyEvent e){
                double speed =Double.valueOf(keySpeedValue.getText());//移动速度
                if(run){
                    switch (e.getKeyChar()){
                        case 'a'://x轴上
                            xPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xPosValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'd'://x轴下
                            xPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xPosValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'w'://z轴上
                            zPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zPosValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 's'://z轴下
                            zPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zPosValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'q'://y轴上
                            yPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yPosValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'e'://y轴下
                            yPosValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yPosValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                    }
                    getCameraPanelValue();
                }

            }
        });
        cameraPanel.add(pixelSizePanel);
        cameraPanel.add(cameraPosPanel);
        cameraPanel.add(lookAtPanel);
        cameraPanel.add(upPanel);
        cameraPanel.add(valuePanel);
        cameraPanel.add(samplesPanel);
        cameraPanel.add(speedPanel);
        cameraPanel.add(buttonPanel);

//        JPanel topPanel=new JPanel();
//        JButton addButton = new JButton("添加");
//        JButton multiButton = new JButton("多视角模式");
//        topPanel.add(addButton);
//        topPanel.add(multiButton);
//        //垂直分隔面板
//        JSplitPane addPanel = new JSplitPane(
//                JSplitPane.VERTICAL_SPLIT,
//                true,
//                topPanel,
//                this.cameraListPane
//        );
//        addPanel.setDividerLocation(40);
//        //水平分隔面板
//        JSplitPane hSplitPane = new JSplitPane(
//                JSplitPane.HORIZONTAL_SPLIT,
//                true,
//                addPanel,
//                cameraPanel
//
//        );
//        hSplitPane.setDividerLocation(201);
//        hSplitPane.setDividerSize(10);

        JPanel menuPanel = new JPanel(new GridLayout(1,1));
        menuPanel.add(cameraPanel);
        return menuPanel;
    }
    //从相机面板上获取属性，赋值给相机对象
    public void getCameraPanelValue(){
        Vector3 pos = new Vector3(Double.valueOf(xPosValue.getText()),Double.valueOf(yPosValue.getText()),Double.valueOf(zPosValue.getText()));
        Vector3 lookat = new Vector3(Double.valueOf(xAtValue.getText()),Double.valueOf(yAtValue.getText()),Double.valueOf(zAtValue.getText()));
        Vector3 vup = new Vector3(Double.valueOf(xUpValue.getText()),Double.valueOf(yUpValue.getText()),Double.valueOf(zUpValue.getText()));
        double vfov =Double.valueOf(vfValue.getText());
        double aspectRatio=1.777;
        switch (aspectCB.getSelectedItem().toString()){
            case "16:9":
                aspectRatio=1.777;
                break;
            case "3:2":
                aspectRatio=1.5;
                break;
        }
        double aperture = Double.valueOf(apertureValue.getText());
        double distToFocus = Double.valueOf(dtfValue.getText());
        int imgWidth=Integer.valueOf(widthValue.getText());
        int sampelsPerPixel = Integer.valueOf(samplesValue.getText());
        int maxDepth=Integer.valueOf(depthValue.getText());
        camera.setCamera(pos,lookat,vup,vfov,aspectRatio,aperture,distToFocus,imgWidth,sampelsPerPixel,maxDepth,0.0,1.0);
        previewPanel.getParent().repaint();
    }

    //从相机对象获取属性，赋值给相机面板
    public void setCameraPanelValue(){
        widthValue.setText(String.valueOf(camera.getImgWidth()));
        double aspectRatio=camera.getAspectRatio();
        if(aspectRatio==1.777){
            aspectCB.setSelectedItem("16:9");
        }
        else if(aspectRatio==1.5){
            aspectCB.setSelectedItem("3:2");
        }

        Vector3 pos=camera.getLookfrom();
        xPosValue.setText(String.valueOf(pos.x()));
        yPosValue.setText(String.valueOf(pos.y()));
        zPosValue.setText(String.valueOf(pos.z()));

        Vector3 at = camera.getLookat();
        xAtValue.setText(String.valueOf(at.x()));
        yAtValue.setText(String.valueOf(at.y()));
        zAtValue.setText(String.valueOf(at.z()));

        Vector3 up = camera.getVup();
        xUpValue.setText(String.valueOf(up.x()));
        yUpValue.setText(String.valueOf(up.y()));
        zUpValue.setText(String.valueOf(up.z()));

        vfValue.setText(String.valueOf(camera.getVfov()));
        apertureValue.setText(String.valueOf(camera.getAperture()));
        dtfValue.setText(String.valueOf(camera.getFocusDist()));
        samplesValue.setText(String.valueOf(camera.getSamplesPerPixel()));
        depthValue.setText(String.valueOf(camera.getMaxDepth()));


    }

    public void setRun(boolean run){
        this.run=run;
    }
}
