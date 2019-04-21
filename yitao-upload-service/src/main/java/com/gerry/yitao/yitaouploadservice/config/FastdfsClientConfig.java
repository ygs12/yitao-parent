package com.gerry.yitao.yitaouploadservice.config;

import com.gerry.yitao.common.upload.FastdfsClient;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/20 02:04
 * @Description:
 */
@SpringBootConfiguration
public class FastdfsClientConfig {
    @Bean
    public FastdfsClient fastdfsClient() {
        return new FastdfsClient("classpath:fdfs/fdfs_client.conf");
    }
}
