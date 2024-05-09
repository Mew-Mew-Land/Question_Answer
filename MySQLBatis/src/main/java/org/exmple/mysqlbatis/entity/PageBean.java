package org.exmple.mysqlbatis.entity;
import lombok.Data;

import java.util.List;
//用于封装查询到的List, 包含链表大小,方便前端进行分页操作
@Data
public class PageBean<E> {
    private int amount;
    private List<E> items;

    public PageBean(int amount, List<E> items) {
        this.amount = amount;
        this.items = items;
    }
}
