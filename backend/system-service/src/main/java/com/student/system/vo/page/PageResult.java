package com.student.system.vo.page;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> {
    private Long total;
    private Long current;
    private Long size;
    private List<T> records;
}
