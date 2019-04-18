package com.gerry.yitao.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/16 19:29
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
// BO 业务对象=> 把请求的数据封装为BO从传入业务层(输入)
// DTO (输入=参数，输出=返回结果)
// VO （封装页面渲染的数据(来源不同实体情况可以选择定义)）
public class BrandBo implements Serializable {
    private Long id;
    private String name;
    private String image;
    private List<Long> cids;
    private Character letter;
}
