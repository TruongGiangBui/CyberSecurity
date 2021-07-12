package com.cybersecurity.repository;

import com.cybersecurity.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,String> {
    @Query("select usersession from UserSession usersession where usersession.sessionID=:sessionID")
    List<UserSession> findBySessionID(@Param("sessionID")String sessionid);
    boolean existsBySessionID(@Param("sessionID") String sessionID);
    boolean existsBySessionIDAndUsername(String sessionID,String username);
    List<UserSession> findAll();
}
