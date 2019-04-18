package com.gerry.yitao.mapper;

import com.gerry.yitao.domain.Brand;
import com.gerry.yitao.domain.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/14 15:16
 * @Description: 品牌Mapper
 *
 * in 子查询 in (1,3,4,6,7)
 * exists / not exists (循环嵌套)
 */

public interface BrandMapper extends Mapper<Brand>, SelectByIdListMapper<Brand, Long> {
    // 第二期课程(就业基础内容)
    // mybatis 注解【如果需要处理2个参数必须加@Param注解，如果一个可加可不加】
    @Insert("insert into tb_category_brand (category_id, brand_id) values (#{cid}, #{bid})")
    int saveCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id = #{bid})")
    List<Category> queryCategoryByBid(Long bid);

    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    int deleteCategoryBrandByBid(Long bid);

    @Select("select * from tb_brand where id in (select brand_id from tb_category_brand where category_id = #{cid})")
    List<Brand> queryBrandByCid(Long cid);


}
