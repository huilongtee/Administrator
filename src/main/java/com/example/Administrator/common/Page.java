package com.example.Administrator.common;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    
    private int total;
    private List<T> list;
}
