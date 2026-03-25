package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.dto.EmailRequest;
import com.ecommerce.service.Emailservice;
import java.util.List;

@RestController
@RequestMapping("api/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private Emailservice emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody EmailRequest request) {
        emailService.sendEmail(
                request.getToEmail(),
                request.getSubject(),
                request.getMessage()
        );
        return ResponseEntity.ok("Email Sent Successfully");
    }

    @GetMapping("/receive")
    public ResponseEntity<List<String>> receiveMail(@RequestParam(defaultValue = "10") int max) {
        List<String> subjects = emailService.fetchInboxSubjects(max);
        return ResponseEntity.ok(subjects);
    }
}