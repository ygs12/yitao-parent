package com.gerry.yitao.seller.service;

import com.gerry.yitao.seller.bo.BrandBo;
import com.gerry.yitao.domain.Brand;
import com.gerry.yitao.domain.Category;
import com.gerry.yitao.entity.PageResult;

import java.util.List;


public interface BrandService {

    PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void saveBrand(Brand brand, List<Long> cids);

    List<Category> queryCategoryByBid(Long bid);

    void updateBrand(BrandBo brandbo);

    void deleteBrand(Long bid);

    List<Brand> queryBrandByCid(Long cid);

    Brand queryBrandByBid(Long id);

    List<Brand> queryBrandByIds(List<Long> ids);

}
