package org.exmple.mysqlbatis.controller;

import lombok.extern.slf4j.Slf4j;
import org.exmple.mysqlbatis.entity.Classification;
import org.exmple.mysqlbatis.entity.Result;
import org.exmple.mysqlbatis.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin(origins="*")
@RestController
public class ClassificationController {
    @Autowired
    private ClassificationService classificationService;

    @PostMapping("/board/all")
    public Result getAllClassification(){
       List<Classification> classifications =classificationService.getAllClassification();
       return Result.success(classifications);
    }
}
