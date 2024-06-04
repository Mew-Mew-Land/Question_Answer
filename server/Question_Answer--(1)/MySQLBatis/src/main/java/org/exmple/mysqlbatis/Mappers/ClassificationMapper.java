package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.exmple.mysqlbatis.entity.Classification;

import java.util.List;

@Mapper
public interface ClassificationMapper {

     List<Classification> getAllClassification();
}
