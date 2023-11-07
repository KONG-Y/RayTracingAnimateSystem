import RayTracing.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileOperator {
    private HittableList world;
    private Camera camera;

    private ArrayList<AnimeFrame> animeFrameList;
    public FileOperator(HittableList world,Camera camera){
        this.world=world;
        this.camera=camera;

    }
    public void write(String filePath){
        Gson gson = new Gson();
        try {
            File file = new File(filePath);
            // 创建上级目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 如果文件存在，则删除文件
            if (file.exists()) {
                file.delete();
            }
            // 创建文件
            file.createNewFile();
            // 写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

            String jsonString=gson.toJson(camera);
            jsonString+="\n";
            write.write(jsonString);
            write.flush();

            //场景物体数据
            for(int i=0;i<world.length();i++){
                jsonString=gson.toJson(world.getIndexObj(i));
                jsonString+="\n";
                write.write(jsonString);
                write.flush();
            }
            write.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void read(String filePath){
        try {
            //清除物体集合
            world.clear();
            FileInputStream inputStream = new FileInputStream(filePath);
            InputStreamReader inReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inReader);
            String line =null;

            Gson gson = new Gson();

            JsonArray value;
            JsonObject jsonObject;
            String materialType;
            double height,width,length;
            Vector3 position;
            double angle;
            while((line=bufferedReader.readLine())!=null){
                JsonObject object=gson.fromJson(line,JsonObject.class);
                String type=object.get("type").getAsString();
                switch(type){
                    case"Camera":
                        value = object.getAsJsonObject("lookfrom").get("e").getAsJsonArray();
                        Vector3 lookfrom = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());

                        value = object.getAsJsonObject("lookat").get("e").getAsJsonArray();
                        Vector3 lookat = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());

                        value = object.getAsJsonObject("vup").get("e").getAsJsonArray();
                        Vector3 vup = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());

                        double vfov = object.get("vfov").getAsDouble();
                        double aspectRatio = object.get("aspectRatio").getAsDouble();
                        double aperture = object.get("aperture").getAsDouble();
                        double focusDist = object.get("focusDist").getAsDouble();
                        int imgWidth = object.get("imgWidth").getAsInt();
                        int samplesPerPixel = object.get("samplesPerPixel").getAsInt();
                        int maxDepth = object.get("maxDepth").getAsInt();
                        double time0 = object.get("time0").getAsInt();
                        double time1 = object.get("time1").getAsInt();
                        camera.setCamera(lookfrom,lookat,vup,vfov,aspectRatio,aperture,focusDist,imgWidth,samplesPerPixel,maxDepth,time0,time1);
                        break;
                    case"Sphere":
                        value = object.getAsJsonObject("position").get("e").getAsJsonArray();
                        Vector3 center =  new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                        double radius = object.get("radius").getAsDouble();
                        materialType =  object.getAsJsonObject("material").get("type").getAsString();

                        if(materialType.equals("Lambertian")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            Sphere sphere = new Sphere(center,radius,new Lambertian(albedo));
                            world.add(sphere);
                        }
                        else if(materialType.equals("Metal")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            Sphere sphere = new Sphere(center,radius,new Metal(albedo,0.0));
                            world.add(sphere);
                        }
                        else if(materialType.equals("DiffuseLight")){
                            jsonObject=object.getAsJsonObject("material").get("emit").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            Sphere sphere = new Sphere(center,radius,new DiffuseLight(albedo));
                            world.add(sphere);
                        }
                        else if(materialType.equals("Dielectric")){
                            double ir =object.getAsJsonObject("material").get("ir").getAsDouble();
                            Sphere sphere = new Sphere(center,radius,new Dielectric(ir));
                            world.add(sphere);
                        }
                        break;
                    case"Box":
                        value = object.getAsJsonObject("position").get("e").getAsJsonArray();
                        position =  new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                        height= object.get("height").getAsDouble();
                        width= object.get("width").getAsDouble();
                        length= object.get("length").getAsDouble();
                        materialType =  object.getAsJsonObject("material").get("type").getAsString();

                        if(materialType.equals("Lambertian")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            Box box = new Box(position,height,width,length,new Lambertian(albedo));
                            world.add(box);
                        }
                        else if(materialType.equals("Metal")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            Box box = new Box(position,height,width,length,new Metal(albedo,0.0));
                            world.add(box);
                        }
                        else if(materialType.equals("DiffuseLight")){
                            jsonObject=object.getAsJsonObject("material").get("emit").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            Box box = new Box(position,height,width,length,new DiffuseLight(albedo));
                            world.add(box);
                        }
                        else if(materialType.equals("Dielectric")){
                            double ir =object.getAsJsonObject("material").get("ir").getAsDouble();
                            Box box = new Box(position,height,width,length,new Dielectric(ir));
                            world.add(box);
                        }
                        break;
                    case"XYRect":
                        value = object.getAsJsonObject("position").get("e").getAsJsonArray();
                        position =  new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                        height = object.get("height").getAsDouble();
                        width = object.get("width").getAsDouble();
                        materialType =  object.getAsJsonObject("material").get("type").getAsString();
                        if(materialType.equals("Lambertian")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            XYRect xyRect = new XYRect(position,height,width,new Lambertian(albedo));
                            world.add(xyRect);
                        }
                        else if(materialType.equals("Metal")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            XYRect xyRect = new XYRect(position,height,width,new Metal(albedo,0.0));
                            world.add(xyRect);
                        }
                        else if(materialType.equals("DiffuseLight")){
                            jsonObject=object.getAsJsonObject("material").get("emit").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            XYRect xyRect = new XYRect(position,height,width,new DiffuseLight(albedo));
                            world.add(xyRect);
                        }
                        else if(materialType.equals("Dielectric")){
                            double ir=object.getAsJsonObject("material").get("ir").getAsDouble();
                            XYRect xyRect = new XYRect(position,height,width,new Dielectric(ir));
                            world.add(xyRect);
                        }
                        break;
                    case"XZRect":
                        value = object.getAsJsonObject("position").get("e").getAsJsonArray();
                        position =  new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                        height = object.get("height").getAsDouble();
                        width = object.get("width").getAsDouble();
                        materialType =  object.getAsJsonObject("material").get("type").getAsString();
                        if(materialType.equals("Lambertian")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            XZRect xzRect = new XZRect(position,height,width,new Lambertian(albedo));
                            world.add(xzRect);
                        }
                        else if(materialType.equals("Metal")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            XZRect xzRect = new XZRect(position,height,width,new Metal(albedo,0.0));
                            world.add(xzRect);
                        }
                        else if(materialType.equals("DiffuseLight")){
                            jsonObject=object.getAsJsonObject("material").get("emit").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            XZRect xzRect = new XZRect(position,height,width,new DiffuseLight(albedo));
                            world.add(xzRect);
                        }
                        else if(materialType.equals("Dielectric")){
                            double ir=object.getAsJsonObject("material").get("ir").getAsDouble();
                            XZRect xzRect = new XZRect(position,height,width,new Dielectric(ir));
                            world.add(xzRect);
                        }
                        break;
                    case"YZRect":
                        value = object.getAsJsonObject("position").get("e").getAsJsonArray();
                        position =  new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                        height = object.get("height").getAsDouble();
                        width = object.get("width").getAsDouble();
                        materialType =  object.getAsJsonObject("material").get("type").getAsString();
                        if(materialType.equals("Lambertian")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            YZRect yzRect = new YZRect(position,height,width,new Lambertian(albedo));
                            world.add(yzRect);
                        }
                        else if(materialType.equals("Metal")){
                            jsonObject=object.getAsJsonObject("material").get("albedo").getAsJsonObject();
                            value = jsonObject.get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            YZRect yzRect = new YZRect(position,height,width,new Metal(albedo,0.0));
                            world.add(yzRect);
                        }
                        else if(materialType.equals("DiffuseLight")){
                            jsonObject=object.getAsJsonObject("material").get("emit").getAsJsonObject();
                            value = jsonObject.getAsJsonObject("colorValue").get("e").getAsJsonArray();
                            Vector3 albedo = new Vector3(value.get(0).getAsDouble(),value.get(1).getAsDouble(),value.get(2).getAsDouble());
                            YZRect yzRect = new YZRect(position,height,width,new DiffuseLight(albedo));
                            world.add(yzRect);
                        }
                        else if(materialType.equals("Dielectric")){
                            double ir=object.getAsJsonObject("material").get("ir").getAsDouble();
                            YZRect yzRect = new YZRect(position,height,width,new Dielectric(ir));
                            world.add(yzRect);
                        }
                        break;
                }
            }
            bufferedReader.close();
            inReader.close();
            inputStream.close();

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
