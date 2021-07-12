package com.cybersecurity.controller;

import com.cybersecurity.dto.FormDTO;
import com.cybersecurity.dto.PasswordDTO;
import com.cybersecurity.service.RegisterService;
import com.cybersecurity.util.ValidateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @PostMapping("api/register/layer1")
    @ResponseBody
    public ResponseEntity<List<String>> layer1processing(@RequestBody FormDTO form){
        System.out.println(form);
        int validate=ValidateForm.validatelayer1(form);
        System.out.println(validate);
        List<String> err=new ArrayList<>();
        if(validate!=0){
            err.add(ValidateForm.messages.get(validate));
            return new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
        Integer result=registerService.layer1processing(form.getUsername(),form.getPassword());
        if(result.equals(0)){
            return new ResponseEntity<>(registerService.getLayer2pictures(),HttpStatus.OK);
        }else {
            err.add("User existed");
            return  new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PostMapping("api/register/layer2")
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
        Integer result=registerService.layer2processing(form.getPassword());
        if(result.equals(0)){
            List<String> res=registerService.getlayer3pieces(form.getPassword());
            if(res.size()>0) {
                return new ResponseEntity<>(res, HttpStatus.OK);
            }else {
                if(result.equals(new Integer(1))) err.add("Internal Server Error");
                return  new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }else {
            if(result.equals(new Integer(1))) err.add("");
            return  new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PostMapping("api/register/layer3")
    @ResponseBody
    public ResponseEntity<String> layer3processing(@RequestBody List<String> pieces){
        Integer result=registerService.layer3processing(pieces);
        registerService.close();
        String err;
        int validate=ValidateForm.validatelayer3(pieces);
        if(validate!=0){
            err=ValidateForm.messages.get(validate);
            return new ResponseEntity<>(err,HttpStatus.NOT_ACCEPTABLE);
        }
        if(result.equals(0)){
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
