package com.gerry.yitao.mapper;

import com.gerry.yitao.domain.Sku;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/14 15:24
 * @Description:
 */

public interface SkuMapper extends Mapper<Sku>, IdListMapper<Sku, Long> {
}
