package com.yj.elk.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MainController {


    @GetMapping("/hello")
    public ResponseEntity hellow(){
        log.debug("this is interesting program with debug");
        log.info("test it with info");
        log.error("test it with error");
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
