package com.cybersecurity.util;

import com.cybersecurity.dto.FormDTO;
import com.cybersecurity.dto.PasswordDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ValidateForm {
    public static final List<String> messages = Arrays.asList("Accepted",
            "Username is not valid",
            "Password is not valid",
            "Incorrect format parameter"
    );;
    public static int validatelayer1(FormDTO form){
        Pattern pattern=Pattern.compile("^[a-zA-Z0-9]*");
        if(form.getUsername().equals("")) return 1;
        else if(form.getPassword().equals("")) return 2;
        else if(!pattern.matcher(form.getUsername()).matches()) return 1;
        else if(!pattern.matcher(form.getPassword()).matches()) return 2;
        return 0;
    }
    public static int validatelayer2(PasswordDTO form){
        Pattern pattern=Pattern.compile("^(res)[0-9]*-[0-9]*\\.jpg");
        if(form.getPassword().equals("")) return 2;
        else if(!pattern.matcher(form.getPassword()).matches()) return 3;
        return 0;
    }
    public static int validatelayer3(List<String> pieces){

        Pattern pattern=Pattern.compile("^(res)[0-9]*-[0-9]*-[0-9]*");
        System.out.println(pattern.matcher(pieces.get(0)).matches());
        for(String piece:pieces){
            if(piece.equals("")) return 3;
            else if(!pattern.matcher(piece).matches()) return 3;
        }
        return 0;
    }
}
