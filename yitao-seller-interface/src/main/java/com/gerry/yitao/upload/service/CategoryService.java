package com.gerry.yitao.upload.service;

import com.gerry.yitao.domain.Category;

import java.util.List;


public interface CategoryService {


    List<Category> queryCategoryByPid(Long pid);

    List<Category> queryCategoryByIds(List<Long> ids);

    List<Category> queryAllByCid3(Long id);
}
