package com.cybersecurity.service;

import com.cybersecurity.entity.User;
import com.cybersecurity.repository.UserRepository;
import com.cybersecurity.util.EncodeUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("prototype")
@Getter
public class LoginService extends PictureService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    EntityManager entityManager;
    private User user=null;
    private String password2=null;
    private EncodeUtil encodeUtil=new EncodeUtil();
    private boolean password1excepted=false;
    private boolean password2excepted=false;
    public int layer1processing(String username,String password){
        if(!userRepository.existsByUsername(username)) return 1;
        String passwordlayer1= encodeUtil.getSHA256(password);
        user=userRepository.findByUsername(username);
        if(passwordlayer1.equals(user.getPassword1())) {
            password1excepted=true;
            return 0;
        }
        else return 1;
    }
    public int layer2processing(String imagename){
        if(!password1excepted) return 1;
        password2=imagemapping.get("static/pictures/"+imagename);
        password2excepted=true;
        return 0;
    }
    public int layer3processing(List<String> pieces){
        if(!password2excepted) return 1;
        StringBuilder stringBuilder=new StringBuilder();
        for (String piece:pieces){
            stringBuilder.append(piecesmapping.get(piece)+"\t");
        }
        String passwordlayer3=stringBuilder.toString().trim();
        if(password2.equals(user.getPicture())) {
            if (user.getPieces().equals(passwordlayer3)) {
                return 0;
            }
        }
        close();
        return 1;
    }
}
