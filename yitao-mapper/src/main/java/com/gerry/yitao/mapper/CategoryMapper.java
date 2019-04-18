package com.gerry.yitao.mapper;

import com.gerry.yitao.domain.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/14 15:19
 * @Description:
 */

public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category, Long> {
}
