package com.cybersecurity.service;

import com.cybersecurity.entity.Picture;
import com.cybersecurity.repository.PictureRepository;
import com.cybersecurity.repository.UserRepository;
import com.cybersecurity.util.EncodeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;

import java.io.File;
import java.util.*;

@Service
@AllArgsConstructor
@Getter
@Setter
public abstract class PictureService {
    protected static int serviceid = 0;
    @Value("${myapp.config.path}")
    protected String path;
    @Value("${myapp.config.url}")
    protected String url;
    protected List<String> imglist;
    protected Map<String, String> imagemapping = new HashMap<>();
    protected Map<String, String> piecesmapping = new HashMap<>();
    @Autowired
    protected PictureRepository pictureRepository;

    public PictureService() {
        serviceid++;
    }
    public int getServiceid(){return serviceid;}
    public List<String> getlayer3pieces(String layer2picture) {
        MappingPiece(layer2picture);
        List<String> urls = new ArrayList<>();
        for (String key : piecesmapping.keySet()) {
            urls.add(url + "/pieces/"+key+".jpg");
        }
        System.out.println(piecesmapping);
        return urls;
    }

    public List<String> getLayer2pictures() {
        imglist = pictureRepository.findAllPictures();
        List<String> urls = new ArrayList<>();
        MappingPicture(imglist);
        for (String key : imagemapping.keySet()) {
            urls.add(url + key.replace("static", ""));
        }
        System.out.println(imagemapping);
        return urls;
    }

    public void close() {
        for (String key : imagemapping.keySet()) {
            try {
                File file = new File(path + key);
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
               try {
                   File file=new File(path+"static/pieces/"+"res"+serviceid+"-"+i+"-"+j+".jpg");
                   file.delete();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        }
    }
    public void MappingPiece(String picture){
        List<String> pieceslist = new ArrayList<>();
        List<String> returnlist = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                pieceslist.add(i + "-" + j);
                returnlist.add("res"+serviceid+"-"+i + "-" + j);
            }
        }
        Collections.shuffle(returnlist);
        for (int i=0;i<pieceslist.size();i++) {
            piecesmapping.put(returnlist.get(i), pieceslist.get(i));
        }
        try {
            BufferedImage image = ImageIO.read(new File(path +"static/pictures/"+picture));
            int height = image.getHeight() / 5;
            int width = image.getWidth() / 5;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    BufferedImage subimage=image.getSubimage(i*width,j*height,width,height);
                    File file=new File(path+"static/pieces/"+returnlist.get(i*5+j)+".jpg");
                    ImageIO.write(subimage, "jpg", file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void MappingPicture(List<String> pictures) {
        List<String> mappedpicture = new ArrayList<>();
        for (String path : pictures) {
            path = path.replace("sourcepicture/", "static/pictures/" +"res"+ serviceid + "-");
            mappedpicture.add(path);
        }
        Collections.shuffle(mappedpicture);
        for (int i = 0; i < pictures.size(); i++) {
            imagemapping.put(mappedpicture.get(i), pictures.get(i));
            copypicture(pictures.get(i), mappedpicture.get(i));
        }
    }
    public void copypicture(String src, String dest) {
        try {
            BufferedImage image = ImageIO.read(new File(path + src));
            File file = new File(path + dest);
            file.createNewFile();
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
