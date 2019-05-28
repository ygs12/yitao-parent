package com.gerry.yitao.yitaoseckillwebapi.ex;

import com.gerry.yitao.yitaoseckillwebapi.entity.StockParam;
import lombok.Data;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/28 21:17
 * @Description: 自定义异常处理类
 */
@Data
public class StockException extends RuntimeException {
    private StockParam param;

    public StockException() {

    }

    public StockException(StockParam param, String message) {
        super(message);
        this.param = param;

    }
}
