package org.exmple.mysqlbatis.Mappers;

import org.apache.ibatis.annotations.Mapper;
import org.exmple.mysqlbatis.entity.Employee;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    List<Employee> employeeList();
    List<Employee> searchEmployee(Integer ID, String name, Integer job, Character gender);
    //注意int,char等这些是没有null(空值)的
    //为了实现实际查询条件为空的情况
    //需要使用包装类Integer,Character
}
