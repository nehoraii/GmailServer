package com.example.gmailserver.controller;
import com.example.gmailserver.functionGmail.GmailQuickStart;
import com.example.gmailserver.vo.GmailVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/sendGmail")
public class SendGmail {
    @PostMapping("/send")
    public ResponseEntity<String> sendGmail(@RequestBody GmailVo gmailVo){
        GmailQuickStart.sendGmail(gmailVo.getSendTo(),gmailVo.getMassage());
        String jsonResponse = "GOOD!!!";
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);

    }
}
