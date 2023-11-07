import RayTracing.Camera;
import RayTracing.HittableList;
import RayTracing.Vector3;

import java.util.ArrayList;

public class AnimeFrame {

//    private Vector3 cameraPosition;
//    private Vector3 cameraLookAt;
//    private Vector3 cameraUp;
//    private double cameraVfov;


    private ArrayList<Vector3> sceneObjectsPosition=new ArrayList<Vector3>();
    private int id;

    public AnimeFrame(Camera camera, HittableList world, int id){
        this.id=id;

//        this.cameraPosition=camera.getLookfrom();
//        this.cameraLookAt=camera.getLookat();
//        this.cameraUp=camera.getVup();

        for(int i=0;i<world.length();++i){
            Vector3 pos=world.getIndexObj(i).getPosition();
            sceneObjectsPosition.add(pos);
        }
    }

    public AnimeFrame(AnimeFrame frame){
//        this.cameraPosition=frame.getCameraPosition();
//        this.cameraLookAt=frame.getCameraLookAt();
//        this.cameraUp=frame.getCameraUp();
        this.sceneObjectsPosition=frame.getSceneObjectsPosition();
        this.id=frame.getId();
    }

    public AnimeFrame(){};
//    public Vector3 getCameraPosition() {
//        return cameraPosition;
//    }
//
//    public void setCameraPosition(Vector3 cameraPosition) {
//        this.cameraPosition = cameraPosition;
//    }
//
//    public Vector3 getCameraLookAt() {
//        return cameraLookAt;
//    }
//
//    public void setCameraLookAt(Vector3 cameraLookAt) {
//        this.cameraLookAt = cameraLookAt;
//    }
//
//    public Vector3 getCameraUp() {
//        return cameraUp;
//    }
//
//    public void setCameraUp(Vector3 cameraUp) {
//        this.cameraUp = cameraUp;
//    }
//
//    public double getCameraVfov() {
//        return cameraVfov;
//    }
//
//    public void setCameraVfov(double cameraVfov) {
//        this.cameraVfov = cameraVfov;
//    }

    public ArrayList<Vector3> getSceneObjectsPosition() {
        return sceneObjectsPosition;
    }

    public void setSceneObjectsPosition(ArrayList<Vector3> sceneObjectsPosition) {
        this.sceneObjectsPosition = sceneObjectsPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
