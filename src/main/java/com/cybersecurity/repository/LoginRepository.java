package com.cybersecurity.repository;

import com.cybersecurity.entity.User;
import com.cybersecurity.entity.UserSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@AllArgsConstructor
@Getter
@Setter
@Repository
public class LoginRepository {
    @Autowired
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserSessionRepository userSessionRepository;
    @Transactional
    @Modifying
    public UserSession createSession(String sessionID,String username){
        UserSession session=null;
        try{
            session=new UserSession(sessionID,username);
            System.out.println(session);
            entityManager.persist(session);
            System.out.println(userSessionRepository.findBySessionID(sessionID).get(0));
        }catch (Exception e){
            e.printStackTrace();
        }
        return session;
    }
    @Transactional
    @Modifying
    public String deleteSession(String sessionID){
        UserSession userSession=userSessionRepository.findBySessionID(sessionID).get(0);
        entityManager.remove(userSession);
        return userSession.getUsername();
    }
}
