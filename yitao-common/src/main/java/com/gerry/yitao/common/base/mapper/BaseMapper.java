package com.gerry.yitao.common.base.mapper;

import tk.mybatis.mapper.common.Mapper;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/14 14:59
 * @Description: 所有Mapper类的基类(只针对单表)
 */
public interface BaseMapper<T, PK> extends Mapper<T> {

}
