package com.example.gmailserver.controller;

import com.example.gmailserver.functionGmail.EmailSender;
import com.example.gmailserver.functionGmail.GmailQuickStart;
import com.example.gmailserver.vo.GmailVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/sendGmail")
public class SendGmail {
    @GetMapping("/send")
    public void sendGmail(@RequestBody GmailVo gmailVo){
        GmailQuickStart.sendGmail(gmailVo.getSendTo(),gmailVo.getMassage());

    }
}
