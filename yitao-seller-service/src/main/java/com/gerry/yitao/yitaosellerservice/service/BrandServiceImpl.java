package com.gerry.yitao.yitaosellerservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.gerry.yitao.bo.BrandBo;
import com.gerry.yitao.common.exception.ServiceException;
import com.gerry.yitao.domain.Brand;
import com.gerry.yitao.domain.Category;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.mapper.BrandMapper;
import com.gerry.yitao.upload.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 品牌业务处理类
 */
@Component
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;


    @Override
    public PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            example.createCriteria().orLike("name", "%" + key + "%").orEqualTo("letter", key);
        }
        if (StringUtils.isNotBlank(sortBy)) {
            String sortByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(sortByClause);
        }
        if (rows < 0) {
            rows = brandMapper.selectCountByExample(example);
        }

        //开启分页
        PageHelper.startPage(page, rows);
        List<Brand> brandList = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new ServiceException("查询的品牌列表为空");
        }

        PageInfo<Brand> pageInfo = new PageInfo<>(brandList);

        return new PageResult<>(pageInfo.getTotal(), brandList);
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brand.setId(null);
        int resultCount = brandMapper.insert(brand);
        if (resultCount == 0) {
            throw new ServiceException("保存品牌失败");
        }
        //更新品牌分类表
        for (Long cid : cids) {
            resultCount = brandMapper.saveCategoryBrand(cid, brand.getId());

            if (resultCount == 0) {
                throw new ServiceException("更新的品牌分类失败");
            }
        }
    }

    @Override
    public List<Category> queryCategoryByBid(Long bid) {
        return brandMapper.queryCategoryByBid(bid);
    }

    @Transactional
    @Override
    public void updateBrand(BrandBo brandBo) {
        Brand brand = new Brand();
        brand.setId(brandBo.getId());
        brand.setName(brandBo.getName());
        brand.setImage(brandBo.getImage());
        brand.setLetter(brandBo.getLetter());

        //更新
        int resultCount = brandMapper.updateByPrimaryKey(brand);
        if (resultCount == 0) {
            throw new ServiceException("更新的品牌失败");
        }
        List<Long> cids = brandBo.getCids();
        //更新品牌分类表
        brandMapper.deleteCategoryBrandByBid(brandBo.getId());

        for (Long cid : cids) {
            resultCount = brandMapper.saveCategoryBrand(cid, brandBo.getId());
            if (resultCount == 0) {
                throw new ServiceException("更新的品牌分类失败");
            }
        }
    }

    @Transactional
    @Override
    public void deleteBrand(Long bid) {
        int result = brandMapper.deleteByPrimaryKey(bid);
        if (result == 0) {
            throw new ServiceException("删除品牌失败");
        }
        //删除中间表
        result = brandMapper.deleteCategoryBrandByBid(bid);
        if (result == 0) {
            throw new ServiceException("删除品牌和分类之间关系失败");
        }
    }

    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> brandList = brandMapper.queryBrandByCid(cid);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new ServiceException("没有找到分类下的品牌");
        }
        return brandList;
    }

    @Override
    public Brand queryBrandByBid(Long id) {
        Brand brand = new Brand();
        brand.setId(id);
        Brand b1 = brandMapper.selectByPrimaryKey(brand);
        if (b1 == null) {
            throw new ServiceException("查询品牌不存在");
        }
        return b1;
    }

    @Override
    public List<Brand> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)) {
            throw new ServiceException("查询品牌不存在");
        }
        return brands;
    }

}
