import RayTracing.*;
import RayTracing.Box;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;



public class SceneMenu {
    private JPanel mainPanel;
    private DrawPanel previewPanel;
    private JPanel shapeValuePanel;
    private JPanel shapeCurrentPanel;
    private HittableList world;

    private JScrollPane hittableListPane;

    private boolean controlRun;

    SceneMenu(DrawPanel previewPanel, HittableList world, Camera camera, CameraMenu cameraAction ){
        this.mainPanel =new JPanel(new GridLayout(1,1));
        this.previewPanel =previewPanel;
        this.shapeCurrentPanel=addSpherePanel();
        this.shapeValuePanel=new JPanel();
        this.shapeValuePanel.add(this.shapeCurrentPanel);
        this.world=world;
        this.hittableListPane=new JScrollPane();

        updateHittableList();//更新物体集列表

        ///////////////////////////////////////////////////////////////////////////

        JButton addButton=new JButton("添加");
        JComboBox shapeCB = new JComboBox<String>();
        shapeCB.addItem("球体");
        shapeCB.addItem("矩形");
        shapeCB.addItem("立方体");

        //添加按钮事件触发
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapeValuePanel.remove(shapeCurrentPanel);

                String shape=shapeCB.getSelectedItem().toString().replace(" ","");
                if(shape.equals("球体")){
                    shapeCurrentPanel=addSpherePanel();
                }
                else if(shape.equals("矩形")){
                    shapeCurrentPanel=addRectPanel();
                }
                else if(shape.equals("立方体")){
                    shapeCurrentPanel=addBoxPanel();
                }
                shapeValuePanel.add(shapeCurrentPanel);
                shapeValuePanel.revalidate();
            }
        });
        //选项框数值变化事件触发
        shapeCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapeValuePanel.removeAll();
                shapeValuePanel.repaint();

                String shape=shapeCB.getSelectedItem().toString().replace(" ","");
                if(shape.equals("球体")){
                    shapeCurrentPanel=addSpherePanel();
                }
                else if(shape.equals("矩形")){
                    shapeCurrentPanel=addRectPanel();
                }
                else if(shape.equals("立方体")){
                    shapeCurrentPanel=addBoxPanel();
                }

                shapeValuePanel.add(shapeCurrentPanel);
                shapeValuePanel.revalidate();
            }
        });

        shapeValuePanel.add(shapeCurrentPanel);
        JPanel addPanel = new JPanel();
        addPanel.add(addButton);
        addPanel.add(shapeCB);

        JPanel ScenePanel = new JPanel();
        JButton openScene=new JButton("打开场景");
        JButton saveScene = new JButton("保存场景");
        ScenePanel.add(openScene);
        ScenePanel.add(saveScene);

        //打开场景事件
        openScene.addActionListener(new ActionListener() {
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

                FileOperator fileOperator=new FileOperator(world,camera);
                fileOperator.read(filePath);
                cameraAction.setCameraPanelValue();//获取相机面板数值，赋值到相机对象
                updateHittableList();
            }
        });
        //保存场景事件
        saveScene.addActionListener(new ActionListener() {
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

                cameraAction.getCameraPanelValue();//获取相机面板数值，赋值到相机对象
                FileOperator fileOperator=new FileOperator(world,camera);
                fileOperator.write(filePath);
            }
        });


        JPanel topPanel=new JPanel(new GridLayout(2,1));
        topPanel.add(ScenePanel);
        topPanel.add(addPanel);

        //垂直分隔面板
        JSplitPane shapePane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                topPanel,
                this.hittableListPane
        );
        shapePane.setDividerLocation(80);


        //水平分隔面板
        JSplitPane scenePane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                true,
                shapePane,
                shapeValuePanel
        );
        scenePane.setDividerLocation(200);

        mainPanel.add(scenePane);

    }

    public JPanel getSceneMenu(){
        return this.mainPanel;
    }
    //添加球体界面
    public JPanel addSpherePanel(){
        //-----------------------------------材质----------------------------------------------
        JLabel materialLabel= new JLabel("材质：");

        JComboBox materialCB = new JComboBox();
        materialCB.addItem("漫反射材质");
        materialCB.addItem("金属材质");
        materialCB.addItem("发光材质");
        materialCB.addItem("玻璃材质");
        //材质面板
        JPanel materialPanel = new JPanel();
        materialPanel.add(materialLabel);
        materialPanel.add(materialCB);

        //调色板面板
        JPanel showColorPanel = new JPanel();
        JPanel rPanel = new JPanel();
        JPanel gPanel = new JPanel();
        JPanel bPanel = new JPanel();

        JLabel rLabel = new JLabel("R");
        JSlider rSlider = new JSlider();
        rSlider.setMaximum(255);
        rSlider.setMinimum(0);
        rSlider.setValue(0);
        rPanel.add(rLabel);
        rPanel.add(rSlider);

        JLabel gLabel = new JLabel("G");
        JSlider gSlider = new JSlider();
        gSlider.setMaximum(255);
        gSlider.setMinimum(0);
        gSlider.setValue(0);
        gPanel.add(gLabel);
        gPanel.add(gSlider);

        JLabel bLabel = new JLabel("B");
        JSlider bSlider = new JSlider();
        bSlider.setMaximum(255);
        bSlider.setMinimum(0);
        bSlider.setValue(0);
        bPanel.add(bLabel);
        bPanel.add(bSlider);

        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        rSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });
        gSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });
        bSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });

        JPanel colorPanel = new JPanel(new GridLayout(3,1));
        colorPanel.add(rPanel);
        colorPanel.add(gPanel);
        colorPanel.add(bPanel);
        JPanel colorPanel2 = new JPanel(new GridLayout(1,2));
        colorPanel2.add(colorPanel);
        colorPanel2.add(showColorPanel);

        JPanel colorPanel3 = new JPanel(new GridLayout(2,1));
        colorPanel3.add(materialPanel);
        colorPanel3.add(colorPanel2);
        //-----------------------------------半径----------------------------------------------
        JLabel radiusLabel=new JLabel("球半径：");
        JTextField radiusValue=new JTextField(10);
        radiusValue.setText("5");
        JPanel radiusPanel = new JPanel();
        radiusPanel.add(radiusLabel);
        radiusPanel.add(radiusValue);

        //-----------------------------------球中心----------------------------------------------
        JLabel posLabel=new JLabel("球中心位置：");

        JLabel xLabel=new JLabel("X");
        JLabel yLabel=new JLabel("Y");
        JLabel zLabel=new JLabel("Z");
        JTextField xValue=new JTextField(10);
        JTextField yValue=new JTextField(10);
        JTextField zValue=new JTextField(10);
        xValue.setText("0");
        yValue.setText("0");
        zValue.setText("0");
        JPanel posPanel = new JPanel();
        posPanel.add(posLabel);
        posPanel.add(xLabel);
        posPanel.add(xValue);
        posPanel.add(yLabel);
        posPanel.add(yValue);
        posPanel.add(zLabel);
        posPanel.add(zValue);

        //球半径与中心坐标面板
        JPanel valuePanel=new JPanel(new GridLayout(3,1));
        valuePanel.add(radiusPanel);
        valuePanel.add(posPanel);


        //添加按钮面板
        JPanel addPanel = new JPanel();
        JButton addButton = new JButton("添加");
        addPanel.add(addButton);

        //物体添加按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());

                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        world.add(new Sphere(
                                new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())),
                                Double.valueOf(radiusValue.getText()),
                                new Lambertian(red,green,blue)
                                ));

                        updateHittableList();

                        break;
                    case"金属材质":
                        world.add(new Sphere(
                                new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())),
                                Double.valueOf(radiusValue.getText()),
                                new Metal(new Vector3(red,green,blue),0.0)
                                ));

                        updateHittableList();
                        break;
                    case"发光材质":
                        world.add(new Sphere(
                                new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())),
                                Double.valueOf(radiusValue.getText()),
                                new DiffuseLight(new Vector3(red,green,blue))
                                ));

                        updateHittableList();
                        break;
                    case"玻璃材质":
                        world.add(new Sphere(
                                new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())),
                                Double.valueOf(radiusValue.getText()),
                                new Dielectric(1.5)
                        ));

                        updateHittableList();
                        break;
                }
            }
        });

        //球体属性面板
        JPanel spherePanel = new JPanel(new GridLayout(3,1));
        spherePanel.add(colorPanel3);
        spherePanel.add(valuePanel);
        spherePanel.add(addPanel);

        return spherePanel;
    }

    //添加矩形界面
    public JPanel addRectPanel(){
        //-----------------------------------材质----------------------------------------------
        JLabel materialLabel= new JLabel("材质：");

        JComboBox materialCB = new JComboBox();
        materialCB.addItem("漫反射材质");
        materialCB.addItem("金属材质");
        materialCB.addItem("发光材质");
        materialCB.addItem("玻璃材质");

        JPanel materialPanel =new JPanel();
        materialPanel.add(materialLabel);
        materialPanel.add(materialCB);

        //调色板
        JPanel showColorPanel = new JPanel();
        JPanel rPanel = new JPanel();
        JPanel gPanel = new JPanel();
        JPanel bPanel = new JPanel();

        JLabel rLabel = new JLabel("R");
        JSlider rSlider = new JSlider();
        rSlider.setMaximum(255);
        rSlider.setMinimum(0);
        rSlider.setValue(0);
        rPanel.add(rLabel);
        rPanel.add(rSlider);

        JLabel gLabel = new JLabel("G");
        JSlider gSlider = new JSlider();
        gSlider.setMaximum(255);
        gSlider.setMinimum(0);
        gSlider.setValue(0);
        gPanel.add(gLabel);
        gPanel.add(gSlider);

        JLabel bLabel = new JLabel("B");
        JSlider bSlider = new JSlider();
        bSlider.setMaximum(255);
        bSlider.setMinimum(0);
        bSlider.setValue(0);
        bPanel.add(bLabel);
        bPanel.add(bSlider);

        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        rSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });
        gSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });
        bSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });


        //RGB滑条面板
        JPanel colorPanel = new JPanel(new GridLayout(3,1));
        colorPanel.add(rPanel);
        colorPanel.add(gPanel);
        colorPanel.add(bPanel);
        //调色板面板
        JPanel colorPanel2 = new JPanel(new GridLayout(1,2));
        colorPanel2.add(colorPanel);
        colorPanel2.add(showColorPanel);

        JPanel colorPanel3 = new JPanel(new GridLayout(2,1));
        colorPanel3.add(materialPanel);
        colorPanel3.add(colorPanel2);
        //-----------------------------------矩形----------------------------------------------
        JLabel cbLabel = new JLabel("矩形所在的坐标轴");
        JComboBox xyzCB = new JComboBox();
        xyzCB.addItem("XY轴");
        xyzCB.addItem("XZ轴");
        xyzCB.addItem("YZ轴");

        //坐标轴选项面板
        JPanel cbPanel = new JPanel();
        cbPanel.add(cbLabel);
        cbPanel.add(xyzCB);
        //添加按钮面板
        JPanel addPanel = new JPanel();
        JButton addButton = new JButton("添加");
        addPanel.add(addButton);

        //矩形长宽
        JPanel sizePanel = new JPanel();
        JLabel heightLabel = new JLabel("高:");
        JTextField heightValue = new JTextField(5);
        heightValue.setText("500");
        JLabel widthLabel = new JLabel("宽:");
        JTextField widthValue = new JTextField(5);
        widthValue.setText("500");
        sizePanel.add(heightLabel);
        sizePanel.add(heightValue);
        sizePanel.add(widthLabel);
        sizePanel.add(widthValue);

        //矩形坐标面板
        JPanel posPanel = new JPanel();
        JLabel xLabel=new JLabel("X");
        JTextField xValue=new JTextField(5);
        xValue.setText("0");
        JLabel yLabel=new JLabel("Y");
        JTextField yValue=new JTextField(5);
        yValue.setText("0");
        JLabel zLabel=new JLabel("Z");
        JTextField zValue=new JTextField(5);
        zValue.setText("0");
        posPanel.add(xLabel);
        posPanel.add(xValue);
        posPanel.add(yLabel);
        posPanel.add(yValue);
        posPanel.add(zLabel);
        posPanel.add(zValue);

        //添加按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Vector3 pos = new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText()));
                double height = Double.valueOf(heightValue.getText());
                double width = Double.valueOf(widthValue.getText());
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        if(xyzCB.getSelectedItem().toString().equals("XY轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XYRect(pos,height,width,new Lambertian(red,green,blue)));
                        }
                        else  if(xyzCB.getSelectedItem().toString().equals("XZ轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XZRect(pos,height,width,new Lambertian(red,green,blue)));
                        }
                        else if(xyzCB.getSelectedItem().toString().equals("YZ轴")){
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new YZRect(pos,height,width,new Lambertian(red,green,blue)));
                        }

                        updateHittableList();
                        break;
                    case"金属材质":
                        if(xyzCB.getSelectedItem().toString().equals("XY轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XYRect(pos,height,width,new Metal(new Vector3(red,green,blue),0.0)));
                        }
                        else  if(xyzCB.getSelectedItem().toString().equals("XZ轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XZRect(pos,height,width,new Metal(new Vector3(red,green,blue),0.0)));
                        }
                        else if(xyzCB.getSelectedItem().toString().equals("YZ轴")){
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new YZRect(pos,height,width,new Metal(new Vector3(red,green,blue),0.0)));
                        }
                        updateHittableList();
                        break;
                    case"发光材质":
                        if(xyzCB.getSelectedItem().toString().equals("XY轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XYRect(pos,height,width,new DiffuseLight(new Vector3(red,green,blue))));
                        }
                        else  if(xyzCB.getSelectedItem().toString().equals("XZ轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XZRect(pos,height,width,new DiffuseLight(new Vector3(red,green,blue))));
                        }
                        else if(xyzCB.getSelectedItem().toString().equals("YZ轴")){
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new YZRect(pos,height,width,new DiffuseLight(new Vector3(red,green,blue))));
                        }
                        updateHittableList();
                        break;
                    case"玻璃材质":
                        if(xyzCB.getSelectedItem().toString().equals("XY轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XYRect(pos,height,width,new Dielectric(1.5)));
                        }
                        else  if(xyzCB.getSelectedItem().toString().equals("XZ轴")){
                            double x0=Double.valueOf(xValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double x1=Double.valueOf(xValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new XZRect(pos,height,width,new Dielectric(1.5)));
                        }
                        else if(xyzCB.getSelectedItem().toString().equals("YZ轴")){
                            double y0=Double.valueOf(yValue.getText())-(Double.valueOf(widthValue.getText())/2.0);
                            double y1=Double.valueOf(yValue.getText())+(Double.valueOf(widthValue.getText())/2.0);
                            double z0=Double.valueOf(zValue.getText())-(Double.valueOf(heightValue.getText())/2.0);
                            double z1=Double.valueOf(zValue.getText())+(Double.valueOf(heightValue.getText())/2.0);
                            world.add(new YZRect(pos,height,width,new Dielectric(1.5)));
                        }
                        updateHittableList();
                        break;
                }


            }
        });


        //选项与坐标面板
        JPanel valuePanel=new JPanel(new GridLayout(3,1));
        valuePanel.add(cbPanel);
        valuePanel.add(sizePanel);
        valuePanel.add(posPanel);

        //矩形属性面板
        JPanel rectPanel = new JPanel(new GridLayout(3,1));
        rectPanel.add(colorPanel3);
        rectPanel.add(valuePanel);
        rectPanel.add(addPanel);

        return rectPanel;
    }

    //添加立方体界面
    public JPanel addBoxPanel(){
        //-----------------------------------材质----------------------------------------------
        JLabel materialLabel= new JLabel("材质：");

        JComboBox materialCB = new JComboBox();
        materialCB.addItem("漫反射材质");
        materialCB.addItem("金属材质");
        materialCB.addItem("发光材质");
        materialCB.addItem("玻璃材质");
        //材质面板
        JPanel materialPanel = new JPanel();
        materialPanel.add(materialLabel);
        materialPanel.add(materialCB);

        //调色板面板
        JPanel showColorPanel = new JPanel();
        JPanel rPanel = new JPanel();
        JPanel gPanel = new JPanel();
        JPanel bPanel = new JPanel();

        JLabel rLabel = new JLabel("R");
        JSlider rSlider = new JSlider();
        rSlider.setMaximum(255);
        rSlider.setMinimum(0);
        rSlider.setValue(0);
        rPanel.add(rLabel);
        rPanel.add(rSlider);

        JLabel gLabel = new JLabel("G");
        JSlider gSlider = new JSlider();
        gSlider.setMaximum(255);
        gSlider.setMinimum(0);
        gSlider.setValue(0);
        gPanel.add(gLabel);
        gPanel.add(gSlider);

        JLabel bLabel = new JLabel("B");
        JSlider bSlider = new JSlider();
        bSlider.setMaximum(255);
        bSlider.setMinimum(0);
        bSlider.setValue(0);
        bPanel.add(bLabel);
        bPanel.add(bSlider);

        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        rSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });
        gSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });
        bSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
            }
        });



        JPanel colorPanel = new JPanel(new GridLayout(3,1));
        colorPanel.add(rPanel);
        colorPanel.add(gPanel);
        colorPanel.add(bPanel);
        JPanel colorPanel2 = new JPanel(new GridLayout(1,2));
        colorPanel2.add(colorPanel);
        colorPanel2.add(showColorPanel);

        JPanel colorPanel3 = new JPanel(new GridLayout(2,1));
        colorPanel3.add(materialPanel);
        colorPanel3.add(colorPanel2);
        //-----------------------------------尺寸----------------------------------------------
        JLabel sizeLabel=new JLabel("立方体尺寸:");

        JLabel lengthLabel=new JLabel("长:");
        JLabel widthLabel=new JLabel("宽:");
        JLabel heightLabel=new JLabel("高:");

        JTextField lengthValue=new JTextField(10);
        JTextField widthValue=new JTextField(10);
        JTextField heightValue=new JTextField(10);

        lengthValue.setText("50");
        widthValue.setText("50");
        heightValue.setText("50");

        JPanel sizePanel = new JPanel();
        sizePanel.add(sizeLabel);
        sizePanel.add(lengthLabel);
        sizePanel.add(lengthValue);
        sizePanel.add(widthLabel);
        sizePanel.add(widthValue);
        sizePanel.add(heightLabel);
        sizePanel.add(heightValue);


        //-----------------------------------位置----------------------------------------------
        JLabel posLabel=new JLabel("位置:");

        JLabel xLabel=new JLabel("X");
        JLabel yLabel=new JLabel("Y");
        JLabel zLabel=new JLabel("Z");
        JTextField xValue=new JTextField(10);
        JTextField yValue=new JTextField(10);
        JTextField zValue=new JTextField(10);
        xValue.setText("0");
        yValue.setText("0");
        zValue.setText("0");
        JPanel posPanel = new JPanel();
        posPanel.add(posLabel);
        posPanel.add(xLabel);
        posPanel.add(xValue);
        posPanel.add(yLabel);
        posPanel.add(yValue);
        posPanel.add(zLabel);
        posPanel.add(zValue);

        JPanel valuePanel=new JPanel(new GridLayout(2,1));
        valuePanel.add(sizePanel);
        valuePanel.add(posPanel);

        //添加按钮面板
        JPanel addPanel = new JPanel();
        JButton addButton = new JButton("添加");
        addPanel.add(addButton);

        //物体添加按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double height =Double.valueOf(heightValue.getText());
                double width =Double.valueOf(widthValue.getText());
                double length =Double.valueOf(lengthValue.getText());
                Vector3 position= new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText()));
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        world.add(new Box(position,height,width,length,new Lambertian(red,green,blue)));
                        updateHittableList();
                        break;
                    case"金属材质":
                        world.add(new Box(position,height,width,length,new Metal(new Vector3(red,green,blue),0.0)));
                        updateHittableList();
                        break;
                    case"发光材质":
                        world.add(new Box(position,height,width,length,new DiffuseLight(new Vector3(red,green,blue))));
                        updateHittableList();
                        break;
                    case"玻璃材质":
                        world.add(new Box(position,height,width,length,new Dielectric(1.5)));
                        updateHittableList();
                        break;
                }
            }
        });


        JPanel boxPanel = new JPanel(new GridLayout(4,1));
        boxPanel.add(colorPanel3);
        boxPanel.add(valuePanel);
        boxPanel.add(addPanel);
        return boxPanel;
    }

    //修改球体界面
    public JPanel getSpherePanel(int index){
        controlRun=false;

        //-----------------------------------材质----------------------------------------------
        JLabel materialLabel= new JLabel("材质：");

        JComboBox materialCB = new JComboBox();
        materialCB.addItem("漫反射材质");
        materialCB.addItem("金属材质");
        materialCB.addItem("发光材质");
        materialCB.addItem("玻璃材质");

        switch (world.getIndexObj(index).getMaterial().getType()){
            case"漫反射材质":
                materialCB.setSelectedItem("漫反射材质");
                break;
            case "金属材质":
                materialCB.setSelectedItem("金属材质");
                break;
            case "发光材质":
                materialCB.setSelectedItem("发光材质");
                break;
            case "玻璃材质":
                materialCB.setSelectedItem("玻璃材质");
                break;
        }


        Vector3 rgb=world.getIndexObj(index).getMaterial().getColor();
        Color color = new Color(new Double(rgb.x()).floatValue(), new Double(rgb.y()).floatValue(), new Double(rgb.z()).floatValue());

        //调色板
        JPanel showColorPanel = new JPanel();
        JPanel rPanel = new JPanel();
        JPanel gPanel = new JPanel();
        JPanel bPanel = new JPanel();

        JLabel rLabel = new JLabel("R");
        JSlider rSlider = new JSlider();
        rSlider.setMaximum(255);
        rSlider.setMinimum(0);
        rSlider.setValue(color.getRed());
        rPanel.add(rLabel);
        rPanel.add(rSlider);

        JLabel gLabel = new JLabel("G");
        JSlider gSlider = new JSlider();
        gSlider.setMaximum(255);
        gSlider.setMinimum(0);
        gSlider.setValue(color.getGreen());
        gPanel.add(gLabel);
        gPanel.add(gSlider);

        JLabel bLabel = new JLabel("B");
        JSlider bSlider = new JSlider();
        bSlider.setMaximum(255);
        bSlider.setMinimum(0);
        bSlider.setValue(color.getBlue());
        bPanel.add(bLabel);
        bPanel.add(bSlider);
        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        rSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Sphere sphere=(Sphere)world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        sphere.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        sphere.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        sphere.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        sphere.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        gSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Sphere sphere=(Sphere)world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        sphere.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        sphere.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        sphere.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        sphere.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        bSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Sphere sphere=(Sphere)world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        sphere.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        sphere.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        sphere.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        sphere.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });

        JPanel materialPanel = new JPanel();
        materialPanel.add(materialLabel);
        materialPanel.add(materialCB);

        JPanel colorPanel = new JPanel(new GridLayout(3,1));
        colorPanel.add(rPanel);
        colorPanel.add(gPanel);
        colorPanel.add(bPanel);
        JPanel colorPanel2 = new JPanel(new GridLayout(1,2));
        colorPanel2.add(colorPanel);
        colorPanel2.add(showColorPanel);

        JPanel colorPanel3 = new JPanel(new GridLayout(2,1));
        colorPanel3.add(materialPanel);
        colorPanel3.add(colorPanel2);

        JLabel speedSizeLabel = new JLabel("调整速度:");
        JTextField speedSizeValue = new JTextField(5);
        speedSizeValue.setText("10");


        //材质数值变化事件触发
        materialCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Sphere sphere=(Sphere)world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        sphere.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        sphere.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        sphere.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        sphere.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });


        //-----------------------------------半径----------------------------------------------
        JLabel radiusLabel=new JLabel("球半径：");
        JTextField radiusValue=new JTextField(10);
        radiusValue.setText(Double.toString(((Sphere)world.getIndexObj(index)).getRadius()));
        JPanel radiusPanel = new JPanel();
        radiusPanel.add(radiusLabel);
        radiusPanel.add(radiusValue);

        radiusValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    radiusValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(radiusValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Sphere sphere=(Sphere) world.getIndexObj(index);
                    sphere.setRadius(Double.valueOf(radiusValue.getText()));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(radiusValue.getText())>0){
                        radiusValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(radiusValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                        Sphere sphere=(Sphere) world.getIndexObj(index);
                        sphere.setRadius(Double.valueOf(radiusValue.getText()));
                        updateHittableList();
                    }

                }
            }
        });

        //-----------------------------------球中心----------------------------------------------
        JLabel posLabel=new JLabel("球中心位置：");

        JLabel xLabel=new JLabel("X");
        JLabel yLabel=new JLabel("Y");
        JLabel zLabel=new JLabel("Z");
        JTextField xValue=new JTextField(10);
        JTextField yValue=new JTextField(10);
        JTextField zValue=new JTextField(10);
        xValue.setText(Double.toString(((Sphere) world.getIndexObj(index)).getPosition().x()));
        yValue.setText(Double.toString(((Sphere) world.getIndexObj(index)).getPosition().y()));
        zValue.setText(Double.toString(((Sphere) world.getIndexObj(index)).getPosition().z()));
        JPanel posPanel = new JPanel();
        posPanel.add(posLabel);
        posPanel.add(xLabel);
        posPanel.add(xValue);
        posPanel.add(yLabel);
        posPanel.add(yValue);
        posPanel.add(zLabel);
        posPanel.add(zValue);

        JLabel controlLabel = new JLabel("键盘控制物体移动:");
        JButton controlButton = new JButton("启动");
        controlButton.setBackground(Color.white);
        JLabel speedMoveLabel = new JLabel("移动速度:");
        JTextField speedMoveValue = new JTextField(5);
        speedMoveValue.setText("8");


        JPanel controlPanel = new JPanel();
        controlPanel.add(controlLabel);
        controlPanel.add(controlButton);
        controlPanel.add(speedMoveLabel);
        controlPanel.add(speedMoveValue);
        controlPanel.add(speedSizeLabel);
        controlPanel.add(speedSizeValue);

        xValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Sphere sphere=(Sphere) world.getIndexObj(index);
                    sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Sphere sphere=(Sphere) world.getIndexObj(index);
                    sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
            }
        });
        yValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Sphere sphere=(Sphere) world.getIndexObj(index);
                    sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Sphere sphere=(Sphere) world.getIndexObj(index);
                    sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });
        zValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Sphere sphere=(Sphere) world.getIndexObj(index);
                    sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Sphere sphere=(Sphere) world.getIndexObj(index);
                    sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });


        JPanel valuePanel=new JPanel(new GridLayout(3,1));
        valuePanel.add(radiusPanel);
        valuePanel.add(posPanel);
        valuePanel.add(controlPanel);

        JButton modifyButton = new JButton("确认");
        modifyButton.setBackground(Color.white);
        JPanel modifyPanel = new JPanel();
        modifyPanel.add(modifyButton);

        //物体修改按钮事件
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Sphere sphere=(Sphere)world.getIndexObj(index);
                sphere.setRadius(  Double.valueOf(radiusValue.getText()));
                sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        sphere.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        sphere.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        sphere.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        sphere.setMaterial(new Dielectric(1.5));
                        break;
                }
                controlRun=false;
                controlButton.setText("启动");
                controlButton.setBackground(Color.white);

                updateHittableList();
            }
        });

        //控制物体按钮事件
        controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controlRun){
                    controlRun=true;
                    controlButton.setText("暂停");
                    controlButton.setBackground(Color.LIGHT_GRAY);
                }
                else{
                    controlRun=false;
                    controlButton.setText("启动");
                    controlButton.setBackground(Color.white);
                }
            }
        });

        //移动物体键盘事件
        controlButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
            @Override
            public void keyPressed(KeyEvent e){
                double speed =Double.valueOf(speedMoveValue.getText());//移动速度
                if(controlRun){
                    switch (e.getKeyChar()){
                        case 'a'://x轴上
                            xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'd'://x轴下
                            xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'w'://z轴上
                            zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 's'://z轴下
                            zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'q'://y轴上
                            yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'e'://y轴下
                            yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                    }
                    Sphere sphere=(Sphere)world.getIndexObj(index);
                    sphere.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }


            }
        });


        JPanel spherePanel = new JPanel(new GridLayout(4,1));
        spherePanel.add(colorPanel3);
        spherePanel.add(valuePanel);
        spherePanel.add(modifyPanel);
        return spherePanel;
    }

    //修改矩形界面
    public JPanel getRectPanel(int index){
        //-----------------------------------材质----------------------------------------------
        JLabel materialLabel= new JLabel("材质：");

        JComboBox materialCB = new JComboBox();
        materialCB.addItem("漫反射材质");
        materialCB.addItem("金属材质");
        materialCB.addItem("发光材质");
        materialCB.addItem("玻璃材质");

        switch (world.getIndexObj(index).getMaterial().getType()){
            case"漫反射材质":
                materialCB.setSelectedItem("漫反射材质");
                break;
            case "金属材质":
                materialCB.setSelectedItem("金属材质");
                break;
            case "发光材质":
                materialCB.setSelectedItem("发光材质");
                break;
            case "玻璃材质":
                materialCB.setSelectedItem("玻璃材质");
                break;
        }

        Vector3 rgb=world.getIndexObj(index).getMaterial().getColor();
        Color color = new Color(new Double(rgb.x()).floatValue(), new Double(rgb.y()).floatValue(), new Double(rgb.z()).floatValue());

        //调色板
        JPanel showColorPanel = new JPanel();
        JPanel rPanel = new JPanel();
        JPanel gPanel = new JPanel();
        JPanel bPanel = new JPanel();

        JLabel rLabel = new JLabel("R");
        JSlider rSlider = new JSlider();
        rSlider.setMaximum(255);
        rSlider.setMinimum(0);
        rSlider.setValue(color.getRed());
        rPanel.add(rLabel);
        rPanel.add(rSlider);

        JLabel gLabel = new JLabel("G");
        JSlider gSlider = new JSlider();
        gSlider.setMaximum(255);
        gSlider.setMinimum(0);
        gSlider.setValue(color.getGreen());
        gPanel.add(gLabel);
        gPanel.add(gSlider);

        JLabel bLabel = new JLabel("B");
        JSlider bSlider = new JSlider();
        bSlider.setMaximum(255);
        bSlider.setMinimum(0);
        bSlider.setValue(color.getBlue());
        bPanel.add(bLabel);
        bPanel.add(bSlider);

        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        rSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Rect rect=(Rect) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        rect.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        rect.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        rect.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        rect.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        gSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Rect rect=(Rect) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        rect.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        rect.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        rect.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        rect.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        bSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Rect rect=(Rect) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        rect.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        rect.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        rect.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        rect.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });


        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        JPanel materialPanel = new JPanel();
        materialPanel.add(materialLabel);
        materialPanel.add(materialCB);

        JPanel colorPanel = new JPanel(new GridLayout(3,1));
        colorPanel.add(rPanel);
        colorPanel.add(gPanel);
        colorPanel.add(bPanel);
        JPanel colorPanel2 = new JPanel(new GridLayout(1,2));
        colorPanel2.add(colorPanel);
        colorPanel2.add(showColorPanel);

        JPanel colorPanel3 = new JPanel(new GridLayout(2,1));
        colorPanel3.add(materialPanel);
        colorPanel3.add(colorPanel2);

        //材质数值变化事件触发
        materialCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Rect rect=(Rect) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        rect.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        rect.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        rect.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        rect.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        //-----------------------------------矩形----------------------------------------------
        JLabel cbLabel = new JLabel("矩形所在的坐标轴");
        JComboBox xyzCB = new JComboBox();
        xyzCB.addItem("XY轴");
        xyzCB.addItem("XZ轴");
        xyzCB.addItem("YZ轴");
        //设置选项数值
        Rect rect=(Rect)world.getIndexObj(index);//矩形对象
        switch (rect.getType()){
            case"XY":
                xyzCB.setSelectedItem("XY轴");
                break;
            case "XZ":
                xyzCB.setSelectedItem("XZ轴");
                break;
            case "YZ":
                xyzCB.setSelectedItem("YZ轴");
                break;
        }
        //坐标轴选项面板
        JPanel cbPanel = new JPanel();
        cbPanel.add(cbLabel);
        cbPanel.add(xyzCB);
        //确认按钮面板
        JPanel modifyPanel = new JPanel();
        JButton modifyButton = new JButton("确认");
        modifyPanel.add(modifyButton);

        //矩形长宽
        JPanel sizePanel = new JPanel();

        JLabel heightLabel = new JLabel("高:");
        JTextField heightValue = new JTextField(5);
        heightValue.setText(Double.toString(((Rect)world.getIndexObj(index)).getHeight()));

        JLabel widthLabel = new JLabel("宽:");
        JTextField widthValue = new JTextField(5);
        widthValue.setText(Double.toString(((Rect)world.getIndexObj(index)).getWidth()));

        JLabel speedSizeLabel = new JLabel("调整速度:");
        JTextField speedSizeValue = new JTextField(5);
        speedSizeValue.setText("10");

        sizePanel.add(heightLabel);
        sizePanel.add(heightValue);
        sizePanel.add(widthLabel);
        sizePanel.add(widthValue);


        heightValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed=Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    heightValue.setText(String.valueOf(Double.valueOf(heightValue.getText())+speed));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setHeight(Double.valueOf(heightValue.getText()));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(heightValue.getText())>0){
                        heightValue.setText(String.valueOf(Double.valueOf(heightValue.getText())-speed));
                        Rect rect=(Rect) world.getIndexObj(index);
                        rect.setHeight(Double.valueOf(heightValue.getText()));
                        updateHittableList();
                    }

                }

            }
        });
        widthValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed=Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    widthValue.setText(String.valueOf(Double.valueOf(widthValue.getText())+speed));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setWidth(Double.valueOf(widthValue.getText()));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(widthValue.getText())>0){
                        widthValue.setText(String.valueOf(Double.valueOf(widthValue.getText())-speed));
                        Rect rect=(Rect) world.getIndexObj(index);
                        rect.setWidth(Double.valueOf(widthValue.getText()));
                        updateHittableList();
                    }

                }

            }
        });



        //矩形坐标面板
        JPanel posPanel = new JPanel();
        JLabel xLabel=new JLabel("X");
        JTextField xValue=new JTextField(5);
        xValue.setText(Double.toString(((Rect)world.getIndexObj(index)).getPosition().x()));
        JLabel yLabel=new JLabel("Y");
        JTextField yValue=new JTextField(5);
        yValue.setText(Double.toString(((Rect)world.getIndexObj(index)).getPosition().y()));
        JLabel zLabel=new JLabel("Z");
        JTextField zValue=new JTextField(5);
        zValue.setText(Double.toString(((Rect)world.getIndexObj(index)).getPosition().z()));
        posPanel.add(xLabel);
        posPanel.add(xValue);
        posPanel.add(yLabel);
        posPanel.add(yValue);
        posPanel.add(zLabel);
        posPanel.add(zValue);

        xValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed=Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });
        yValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed=Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });
        zValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed=Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Rect rect=(Rect) world.getIndexObj(index);
                    rect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });


        //物体修改按钮事件
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double height = Double.valueOf(heightValue.getText());
                double width = Double.valueOf(widthValue.getText());
                if(xyzCB.getSelectedItem().toString().equals("XY轴")){
                    XYRect xyRect=(XYRect)rect;
                    xyRect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    xyRect.setHeight(height);
                    xyRect.setWidth(width);
                    switch (materialCB.getSelectedItem().toString()){
                        case"漫反射材质":
                            xyRect.setMaterial(new Lambertian(red,green,blue));
                            break;
                        case"金属材质":
                            xyRect.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                            break;
                        case"发光材质":
                            xyRect.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                            break;
                        case"玻璃材质":
                            xyRect.setMaterial(new Dielectric(1.5));
                            break;
                    }
                }
                else if (xyzCB.getSelectedItem().toString().equals("XZ轴")){
                    XZRect xzRect=(XZRect)rect;
                    xzRect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    xzRect.setHeight(height);
                    xzRect.setWidth(width);
                    switch (materialCB.getSelectedItem().toString()){
                        case"漫反射材质":
                            xzRect.setMaterial(new Lambertian(red,green,blue));
                            break;
                        case"金属材质":
                            xzRect.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                            break;
                        case"发光材质":
                            xzRect.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                            break;
                        case"玻璃材质":
                            xzRect.setMaterial(new Dielectric(1.5));
                            break;
                    }
                }
                else if (xyzCB.getSelectedItem().toString().equals("YZ轴")){
                    YZRect yzRect=(YZRect)rect;
                    yzRect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    yzRect.setHeight(height);
                    yzRect.setWidth(width);
                    switch (materialCB.getSelectedItem().toString()){
                        case"漫反射材质":
                            yzRect.setMaterial(new Lambertian(red,green,blue));
                            break;
                        case"金属材质":
                            yzRect.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                            break;
                        case"发光材质":
                            yzRect.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                            break;
                        case"玻璃材质":
                            yzRect.setMaterial(new Dielectric(1.5));
                            break;
                    }
                }
                updateHittableList();
            }
        });

        JLabel controlLabel = new JLabel("键盘控制物体移动:");
        JButton controlButton = new JButton("启动");
        controlButton.setBackground(Color.white);
        JLabel speedMoveLabel = new JLabel("移动速度:");
        JTextField speedMoveValue = new JTextField(5);
        speedMoveValue.setText("8");
        JPanel controlPanel = new JPanel();
        controlPanel.add(controlLabel);
        controlPanel.add(controlButton);
        controlPanel.add(speedMoveLabel);
        controlPanel.add(speedMoveValue);
        controlPanel.add(speedSizeLabel);
        controlPanel.add(speedSizeValue);

        //控制物体按钮事件
        controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controlRun){
                    controlRun=true;
                    controlButton.setText("暂停");
                    controlButton.setBackground(Color.LIGHT_GRAY);
                }
                else{
                    controlRun=false;
                    controlButton.setText("启动");
                    controlButton.setBackground(Color.white);
                }
            }
        });

        //移动物体键盘事件
        controlButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
            @Override
            public void keyPressed(KeyEvent e){
                double speed =Double.valueOf(speedMoveValue.getText());//移动速度
                if(controlRun){
                    switch (e.getKeyChar()){
                        case 'a'://x轴上
                            xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'd'://x轴下
                            xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'w'://z轴上
                            zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 's'://z轴下
                            zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'q'://y轴上
                            yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'e'://y轴下
                            yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                    }
                    Rect rect=(Rect)world.getIndexObj(index);
                    rect.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
            }
        });

        //选项与坐标面板
        JPanel valuePanel=new JPanel(new GridLayout(5,1));
        valuePanel.add(cbPanel);
        valuePanel.add(sizePanel);
        valuePanel.add(posPanel);
        valuePanel.add(controlPanel);

        //矩形属性面板
        JPanel rectPanel = new JPanel(new GridLayout(3,1));
        rectPanel.add(colorPanel3);
        rectPanel.add(valuePanel);
        rectPanel.add(modifyPanel);

        return rectPanel;
    }

    //修改立方体界面
    public JPanel getBoxPanel(int index){

        controlRun=false;

        //-----------------------------------材质----------------------------------------------
        JLabel materialLabel= new JLabel("材质：");

        JComboBox materialCB = new JComboBox();
        materialCB.addItem("漫反射材质");
        materialCB.addItem("金属材质");
        materialCB.addItem("发光材质");
        materialCB.addItem("玻璃材质");

        switch (world.getIndexObj(index).getMaterial().getType()){
            case"漫反射材质":
                materialCB.setSelectedItem("漫反射材质");
                break;
            case "金属材质":
                materialCB.setSelectedItem("金属材质");
                break;
            case "发光材质":
                materialCB.setSelectedItem("发光材质");
                break;
            case "玻璃材质":
                materialCB.setSelectedItem("玻璃材质");
                break;
        }

        Vector3 rgb=world.getIndexObj(index).getMaterial().getColor();
        Color color = new Color(new Double(rgb.x()).floatValue(), new Double(rgb.y()).floatValue(), new Double(rgb.z()).floatValue());

        //调色板
        JPanel showColorPanel = new JPanel();
        JPanel rPanel = new JPanel();
        JPanel gPanel = new JPanel();
        JPanel bPanel = new JPanel();

        JLabel rLabel = new JLabel("R");
        JSlider rSlider = new JSlider();
        rSlider.setMaximum(255);
        rSlider.setMinimum(0);
        rSlider.setValue(color.getRed());
        rPanel.add(rLabel);
        rPanel.add(rSlider);

        JLabel gLabel = new JLabel("G");
        JSlider gSlider = new JSlider();
        gSlider.setMaximum(255);
        gSlider.setMinimum(0);
        gSlider.setValue(color.getGreen());
        gPanel.add(gLabel);
        gPanel.add(gSlider);

        JLabel bLabel = new JLabel("B");
        JSlider bSlider = new JSlider();
        bSlider.setMaximum(255);
        bSlider.setMinimum(0);
        bSlider.setValue(color.getBlue());
        bPanel.add(bLabel);
        bPanel.add(bSlider);

        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        rSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Box box=(Box) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        box.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        box.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        box.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        box.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        gSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Box box=(Box) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        box.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        box.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        box.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        box.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        bSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Box box=(Box) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        box.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        box.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        box.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        box.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });

        showColorPanel.setBackground(new Color(rSlider.getValue(),gSlider.getValue(),bSlider.getValue()));

        JPanel materialPanel = new JPanel();
        materialPanel.add(materialLabel);
        materialPanel.add(materialCB);

        JPanel colorPanel = new JPanel(new GridLayout(3,1));
        colorPanel.add(rPanel);
        colorPanel.add(gPanel);
        colorPanel.add(bPanel);
        JPanel colorPanel2 = new JPanel(new GridLayout(1,2));
        colorPanel2.add(colorPanel);
        colorPanel2.add(showColorPanel);

        JPanel colorPanel3 = new JPanel(new GridLayout(2,1));
        colorPanel3.add(materialPanel);
        colorPanel3.add(colorPanel2);

        //材质数值变化事件触发
        materialCB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Box box=(Box) world.getIndexObj(index);
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        box.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        box.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        box.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        box.setMaterial(new Dielectric(1.5));
                        break;
                }
                updateHittableList();
            }
        });
        //-----------------------------------尺寸----------------------------------------------
        JLabel sizeLabel=new JLabel("立方体尺寸:");

        JLabel lengthLabel=new JLabel("长:");
        JLabel widthLabel=new JLabel("宽:");
        JLabel heightLabel=new JLabel("高:");

        JTextField lengthValue=new JTextField(10);
        JTextField widthValue=new JTextField(10);
        JTextField heightValue=new JTextField(10);

        lengthValue.setText(Double.toString(((Box) world.getIndexObj(index)).getLength()));
        widthValue.setText(Double.toString(((Box) world.getIndexObj(index)).getWidth()));
        heightValue.setText(Double.toString(((Box) world.getIndexObj(index)).getHeight()));

        JPanel sizePanel = new JPanel();
        sizePanel.add(sizeLabel);
        sizePanel.add(lengthLabel);
        sizePanel.add(lengthValue);
        sizePanel.add(widthLabel);
        sizePanel.add(widthValue);
        sizePanel.add(heightLabel);
        sizePanel.add(heightValue);

        JLabel speedSizeLabel = new JLabel("调整速度:");
        JTextField speedSizeValue = new JTextField(5);
        speedSizeValue.setText("10");

        lengthValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    lengthValue.setText(String.valueOf(Double.valueOf(lengthValue.getText())+speed));
                    Box box=(Box)world.getIndexObj(index);
                    box.setLength(Double.valueOf(lengthValue.getText()));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(lengthValue.getText())>0){
                        lengthValue.setText(String.valueOf(Double.valueOf(lengthValue.getText())-speed));
                        Box box=(Box)world.getIndexObj(index);
                        box.setLength(Double.valueOf(lengthValue.getText()));
                        updateHittableList();
                    }

                }

            }
        });
        widthValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    widthValue.setText(String.valueOf(Double.valueOf(widthValue.getText())+speed));
                    Box box=(Box)world.getIndexObj(index);
                    box.setWidth(Double.valueOf(widthValue.getText()));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(widthValue.getText())>0){
                        widthValue.setText(String.valueOf(Double.valueOf(widthValue.getText())-speed));
                        Box box=(Box)world.getIndexObj(index);
                        box.setWidth(Double.valueOf(widthValue.getText()));
                        updateHittableList();
                    }

                }

            }
        });
        heightValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    heightValue.setText(String.valueOf(Double.valueOf(heightValue.getText())+speed));
                    Box box=(Box)world.getIndexObj(index);
                    box.setHeight(Double.valueOf(heightValue.getText()));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    if(Double.valueOf(heightValue.getText())>0){
                        heightValue.setText(String.valueOf(Double.valueOf(heightValue.getText())-speed));
                        Box box=(Box)world.getIndexObj(index);
                        box.setHeight(Double.valueOf(heightValue.getText()));
                        updateHittableList();
                    }

                }

            }
        });

        //-----------------------------------位置----------------------------------------------
        JLabel posLabel=new JLabel("位置:");

        JLabel xLabel=new JLabel("X");
        JLabel yLabel=new JLabel("Y");
        JLabel zLabel=new JLabel("Z");
        JTextField xValue=new JTextField(10);
        JTextField yValue=new JTextField(10);
        JTextField zValue=new JTextField(10);
        xValue.setText(Double.toString(((Box) world.getIndexObj(index)).getPosition().x()));
        yValue.setText(Double.toString(((Box) world.getIndexObj(index)).getPosition().y()));
        zValue.setText(Double.toString(((Box) world.getIndexObj(index)).getPosition().z()));
        JPanel posPanel = new JPanel();
        posPanel.add(posLabel);
        posPanel.add(xLabel);
        posPanel.add(xValue);
        posPanel.add(yLabel);
        posPanel.add(yValue);
        posPanel.add(zLabel);
        posPanel.add(zValue);

        JLabel controlLabel = new JLabel("键盘控制物体移动:");
        JButton controlButton = new JButton("启动");
        controlButton.setBackground(Color.white);
        JLabel speedLabel = new JLabel("移动速度:");
        JTextField speedValue = new JTextField(5);
        speedValue.setText("8");
        JPanel controlPanel = new JPanel();
        controlPanel.add(controlLabel);
        controlPanel.add(controlButton);
        controlPanel.add(speedLabel);
        controlPanel.add(speedValue);
        controlPanel.add(speedSizeLabel);
        controlPanel.add(speedSizeValue);

        JPanel rotatePanel = new JPanel();
        JLabel rotateLabel=new JLabel("旋转角度:");
        JTextField rotateValue=new JTextField(10);
        rotateValue.setText("0");
        rotatePanel.add(rotateLabel);
        rotatePanel.add(rotateValue);

        rotateValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    rotateValue.setText(String.valueOf(Double.valueOf(rotateValue.getText())+speed));
                }
                if(e.getWheelRotation()==1){
                    rotateValue.setText(String.valueOf(Double.valueOf(rotateValue.getText())-speed));
                }

            }
        });

        JPanel valuePanel=new JPanel(new GridLayout(4,1));
        valuePanel.add(sizePanel);
        valuePanel.add(posPanel);
        valuePanel.add(rotatePanel);
        valuePanel.add(controlPanel);

        JButton modifyButton = new JButton("确认");
        modifyButton.setBackground(Color.white);
        JPanel modifyPanel = new JPanel();
        modifyPanel.add(modifyButton);

        xValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Box box=(Box)world.getIndexObj(index);
                    box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Box box=(Box)world.getIndexObj(index);
                    box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });

        yValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Box box=(Box)world.getIndexObj(index);
                    box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Box box=(Box)world.getIndexObj(index);
                    box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });

        zValue.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double speed = Double.valueOf(speedSizeValue.getText());
                if(e.getWheelRotation()==-1){
                    zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Box box=(Box)world.getIndexObj(index);
                    box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }
                if(e.getWheelRotation()==1){
                    zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                    Box box=(Box)world.getIndexObj(index);
                    box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }

            }
        });

        //物体修改按钮事件
        modifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double red =Double.valueOf(new BigDecimal(rSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double green =Double.valueOf(new BigDecimal(gSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                double blue =Double.valueOf(new BigDecimal(bSlider.getValue()/255.0).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString());
                Box box=(Box)world.getIndexObj(index);
                box.setHeight(Double.valueOf(heightValue.getText()));
                box.setWidth(Double.valueOf(widthValue.getText()));
                box.setLength(Double.valueOf(lengthValue.getText()));
                box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                switch (materialCB.getSelectedItem().toString()){
                    case"漫反射材质":
                        box.setMaterial(new Lambertian(red,green,blue));
                        break;
                    case"金属材质":
                        box.setMaterial(new Metal(new Vector3(red,green,blue),0.0));
                        break;
                    case"发光材质":
                        box.setMaterial(new DiffuseLight(new Vector3(red,green,blue)));
                        break;
                    case"玻璃材质":
                        box.setMaterial(new Dielectric(1.5));
                        break;
                }
                controlRun=false;
                controlButton.setText("启动");
                controlButton.setBackground(Color.white);

                updateHittableList();
            }
        });

        //控制物体按钮事件
        controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!controlRun){
                    controlRun=true;
                    controlButton.setText("暂停");
                    controlButton.setBackground(Color.LIGHT_GRAY);
                }
                else{
                    controlRun=false;
                    controlButton.setText("启动");
                    controlButton.setBackground(Color.white);
                }
            }
        });

        //移动物体键盘事件
        controlButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }
            @Override
            public void keyPressed(KeyEvent e){
                double speed =Double.valueOf(speedValue.getText());//移动速度
                if(controlRun){
                    switch (e.getKeyChar()){
                        case 'a'://x轴上
                            xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'd'://x轴下
                            xValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(xValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 'w'://z轴上
                            zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case 's'://z轴下
                            zValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(zValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'q'://y轴上
                            yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())+speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                        case'e'://y轴下
                            yValue.setText(String.valueOf(Double.valueOf(new BigDecimal(Double.valueOf(yValue.getText())-speed).setScale(3,BigDecimal.ROUND_HALF_DOWN).toString())));
                            break;
                    }
                    Box box=(Box)world.getIndexObj(index);
                    box.setPosition(new Vector3(Double.valueOf(xValue.getText()),Double.valueOf(yValue.getText()),Double.valueOf(zValue.getText())));
                    updateHittableList();
                }


            }
        });


        JPanel spherePanel = new JPanel(new GridLayout(4,1));
        spherePanel.add(colorPanel3);
        spherePanel.add(valuePanel);
        spherePanel.add(modifyPanel);
        return spherePanel;
    }


    //物体列表窗口
    public void updateHittableList(){
        if (world.length()!=0){
            JPanel listPanel=new JPanel(new GridLayout(world.length()+10,1));
            for(int i=0;i<world.length();++i){
                JPanel hitPanel=new JPanel();
                JPanel colorPanel = new JPanel();
                JButton hitButton=new JButton();
                if(world.getIndexObj(i).getShape().equals("Sphere")){
                    hitButton.setText("球体"+i);
                }
                else if(world.getIndexObj(i).getShape().equals("Rect")){
                    hitButton.setText("矩形"+i);
                }
                else if(world.getIndexObj(i).getShape().equals("Box")){
                    hitButton.setText("立方体"+i);
                }

                JButton deleteButton = new JButton("删除");

                //删除按钮
                deleteButton.setBackground(Color.PINK);
                Vector3 rgb=world.getIndexObj(i).getMaterial().getColor();
                Color color = new Color(new Double(rgb.x()).floatValue(), new Double(rgb.y()).floatValue(), new Double(rgb.z()).floatValue());
                colorPanel.setBackground(color);

                //点击物体，显示当前物体属性界面
                HitValueAction hitValueAction=new HitValueAction(i);
                hitButton.addActionListener(hitValueAction);

                //删除物体，更新物体列表
                HitDeleteAction hitDeleteAction = new HitDeleteAction(i);
                deleteButton.addActionListener(hitDeleteAction);

                hitPanel.add(colorPanel);
                hitPanel.add(hitButton);
                hitPanel.add(deleteButton);

                listPanel.add(hitPanel);
            }
            hittableListPane.setViewportView(listPanel);//更新物体列表界面
            previewPanel.getParent().repaint();//更新主界面显示区域
        }
        else{
            hittableListPane.setViewportView(null);
        }
    }

    //物体数值修改事件
    public class HitValueAction implements ActionListener{
        private int index;
        HitValueAction(int index){
            this.index=index;
        }
        @Override
        public void actionPerformed(ActionEvent e){
            shapeValuePanel.remove(shapeCurrentPanel);
            shapeValuePanel.repaint();
            if(world.getIndexObj(this.index).getShape().equals("Sphere")){
                shapeCurrentPanel=getSpherePanel(this.index);
            }
            else if(world.getIndexObj(this.index).getShape().equals("Rect")){
                shapeCurrentPanel=getRectPanel(this.index);
            }
            else if(world.getIndexObj(this.index).getShape().equals("Box")){
                shapeCurrentPanel=getBoxPanel(this.index);
            }
            shapeValuePanel.add(shapeCurrentPanel);
            shapeValuePanel.revalidate();
            updateHittableList();
        }
    }

    //物体删除事件
    public class HitDeleteAction implements ActionListener{
        private int index;
        HitDeleteAction(int index){
            this.index=index;
        }
        @Override
        public void actionPerformed(ActionEvent e){
            world.delete(this.index);
            shapeValuePanel.revalidate();
            updateHittableList();
        }
    }

}
