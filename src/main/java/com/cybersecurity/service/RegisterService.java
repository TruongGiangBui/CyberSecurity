package com.cybersecurity.service;

import com.cybersecurity.entity.User;
import com.cybersecurity.repository.UserRepository;
import com.cybersecurity.util.EncodeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Scope("prototype")
@Getter
@Setter
public class RegisterService extends PictureService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    EntityManager entityManager;
    private String username;
    private String passwordlayer1;
    private String passwordlayer2;
    private String passwordlayer3;
    private EncodeUtil encodeUtil=new EncodeUtil();
    private boolean password1excepted=false;
    private boolean password2excepted=false;
    public int layer1processing(String username,String password){
        if(userRepository.existsByUsername(username)) return 1;
        this.username=username;
        this.passwordlayer1= encodeUtil.getSHA256(password);
        password1excepted=true;
        return 0;
    }
    public int layer2processing(String imagename){
        if(!password1excepted) return 1;
        String original=imagemapping.get("static/pictures/"+imagename);
        passwordlayer2=original;
        password2excepted=true;
        return 0;
    }
    @Transactional
    public int layer3processing(List<String> pieces){
        if(!password2excepted) return 1;
        System.out.println(piecesmapping);
        StringBuilder stringBuilder=new StringBuilder();
        for (String piece:pieces){
            stringBuilder.append(piecesmapping.get(piece)+"\t");
        }
        passwordlayer3=stringBuilder.toString().trim();
        User user=new User(username,passwordlayer1,passwordlayer2,passwordlayer3,1);
        entityManager.persist(user);
        close();
        return 0;
    }
}
