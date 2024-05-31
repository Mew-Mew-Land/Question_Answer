package org.exmple.mysqlbatis.service;

import org.exmple.mysqlbatis.Mappers.ClassificationMapper;
import org.exmple.mysqlbatis.entity.Classification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificationServant implements ClassificationService{
    @Autowired
    private ClassificationMapper classificationMapper;

    @Override
    public List<Classification> getAllClassification() {
        return classificationMapper.getAllClassification();
    }
}
