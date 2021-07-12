package com.cybersecurity.service;

import com.cybersecurity.entity.User;
import com.cybersecurity.entity.UserSession;
import com.cybersecurity.repository.LoginRepository;
import com.cybersecurity.repository.UserSessionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.Random;

@Service
@Scope("prototype")
@Getter
@Setter
public class SessionService {
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private LoginRepository loginRepository;
    public String createSession(String username){
        String sessionID="";
        do{
            sessionID=generateSessionID(6);
        }while (userSessionRepository.existsBySessionID(sessionID));
        loginRepository.createSession(sessionID,username);
        return sessionID;
    }
    public void deleteSession(String sessionID){
        loginRepository.deleteSession(sessionID);
    }
    public boolean checkSession(String sessionID,String username){
        return userSessionRepository.existsBySessionIDAndUsername(sessionID,username);
    }
    private String generateSessionID(int targetStringLength){
        int leftLimit = 97;
        int rightLimit = 122;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        return generatedString;
    }
}
