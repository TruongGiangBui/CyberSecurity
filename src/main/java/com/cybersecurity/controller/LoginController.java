package com.cybersecurity.controller;

import com.cybersecurity.dto.FormDTO;
import com.cybersecurity.dto.PasswordDTO;
import com.cybersecurity.entity.UserSession;
import com.cybersecurity.repository.LoginRepository;
import com.cybersecurity.repository.UserRepository;
import com.cybersecurity.repository.UserSessionRepository;
import com.cybersecurity.service.JWTService;
import com.cybersecurity.service.LoginService;
import com.cybersecurity.service.SessionService;
import com.cybersecurity.util.ValidateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private SessionService sessionService;
    @PostMapping("api/login/layer1")
    @ResponseBody
    public ResponseEntity<List<String>> layer1processing(@RequestBody FormDTO form){
        int validate= ValidateForm.validatelayer1(form);
        List<String> err=new ArrayList<>();
        if(validate!=0){
            err.add(ValidateForm.messages.get(validate));
            return new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
        Integer result=loginService.layer1processing(form.getUsername(),form.getPassword());
        if(result.equals(0)) {
            return new ResponseEntity<>(loginService.getLayer2pictures(),HttpStatus.OK);
        }
        else {
            err.add("Wrong username or password");
            return  new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PostMapping("api/login/layer2")
    @ResponseBody
    public ResponseEntity<List<String>> layer2processing(@RequestBody PasswordDTO form){
        List<String> err=new ArrayList<>();
        int validate=ValidateForm.validatelayer2(form);
        if(validate!=0){
            err.add(ValidateForm.messages.get(validate));
            return new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
        if(form.getPassword().equals("")){
            err.add("Please select picture before submit");
            return  new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
        Integer result=loginService.layer2processing(form.getPassword());
        if(result.equals(0)){
            List<String> res=loginService.getlayer3pieces(form.getPassword());
            if(res.size()>0) {
                return new ResponseEntity<>(res, HttpStatus.OK);
            }else {
                err.add("Internal Server Error");
                return  new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else {
            if(result.equals(new Integer(1))) err.add("");
            return  new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PostMapping("api/login/layer3")
    @ResponseBody
    public ResponseEntity<List<String>> getlayer3images(@RequestBody List<String> pieces, HttpServletResponse response){
        Integer result=loginService.layer3processing(pieces);
        String username=loginService.getUser().getUsername();
        List<String> res=new ArrayList<>();

        if(result.equals(1)){
            res.add("Wrong password");
            return new ResponseEntity<>(res,HttpStatus.NOT_ACCEPTABLE);
        }
        else {
            String token="";
            String sessionID=sessionService.createSession(username);
            System.out.println(sessionID);
            try{
                token=jwtService.generateToken(username+"_"+sessionID);
            }catch (Exception e){
                e.printStackTrace();
            }
            res.add(token);

            return new ResponseEntity<>(res,HttpStatus.OK);
        }
    }
    @GetMapping("data/message")
    @ResponseBody
    public ResponseEntity<String> mess(){
        return new ResponseEntity<>("Logged in",HttpStatus.OK);
    }
    @PostMapping("api/logout")
    public void logout(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token= request.getHeader("Authorization");
        String decodeString=jwtService.decode(token);
        String sessionID=decodeString.split("_")[1];
        System.out.println(sessionID);
        sessionService.deleteSession(sessionID);
    }
    @Autowired
    UserSessionRepository userSessionRepository;
    @Autowired
    LoginRepository loginRepository;
    @GetMapping("test")
    @ResponseBody
    public ResponseEntity<String> ets(ServletRequest servletRequest){
        loginRepository.createSession("sdfsf","giang");
        return new ResponseEntity<>(userSessionRepository.findAll().toString(),HttpStatus.OK);
    }

}
