package org.exmple.mysqlbatis.entity;
import lombok.Data;

import java.util.List;
//用于封装查询到的List, 包含链表大小,方便前端进行分页操作
@Data
public class PageBean<E> {

    private List<E> list;

    public PageBean(List<E> list) {

        this.list = list;
    }
}
