package com.gerry.yitao.yitaosellerservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.gerry.yitao.common.exception.ServiceException;
import com.gerry.yitao.domain.SpecGroup;
import com.gerry.yitao.domain.SpecParam;
import com.gerry.yitao.mapper.SpecGroupMapper;
import com.gerry.yitao.mapper.SpecParamMapper;
import com.gerry.yitao.seller.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品的规格业务
 */
@Service(timeout = 30000)
public class SpecServiceImpl implements SpecService {

    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;


    @Override
    public List<SpecGroup> querySpecGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(specGroupList)) {
            throw new ServiceException("未找到规格组列表");
        }
        return specGroupList;
    }

    @Override
    public void saveSpecGroup(SpecGroup specGroup) {
        int count = specGroupMapper.insert(specGroup);
        if (count != 1) {
            throw new ServiceException("保存规则组失败");
        }
    }

    @Override
    public void deleteSpecGroup(Long id) {
        if (id == null) {
            throw new ServiceException("未传入规格组ID");
        }
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        int count = specGroupMapper.deleteByPrimaryKey(specGroup);
        if (count != 1) {
            throw new ServiceException("删除规格组失败");
        }
    }

    @Override
    public void updateSpecGroup(SpecGroup specGroup) {
        int count = specGroupMapper.updateByPrimaryKey(specGroup);
        if (count != 1) {
            throw new ServiceException("更新规格组失败");
        }
    }


    @Override
    public List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        specParam.setGeneric(generic);
        List<SpecParam> specParamList = specParamMapper.select(specParam);
        if (CollectionUtils.isEmpty(specParamList)) {
            throw new ServiceException("查询规则参数列表不存在");
        }
        return specParamList;
    }

    @Override
    public void saveSpecParam(SpecParam specParam) {
        int count = specParamMapper.insert(specParam);
        if (count != 1) {
            throw new ServiceException("保存规则参数失败");
        }
    }

    @Override
    public void updateSpecParam(SpecParam specParam) {
        int count = specParamMapper.updateByPrimaryKeySelective(specParam);
        if (count != 1) {
            throw new ServiceException("更新规格参数失败");
        }
    }

    @Override
    public void deleteSpecParam(Long id) {
        if (id == null) {
            throw new ServiceException("未传入格式参数ID");
        }
        int count = specParamMapper.deleteByPrimaryKey(id);
        if (count != 1) {
            throw new ServiceException("删除规格参数失败");
        }
    }

    @Override
    public List<SpecGroup> querySpecsByCid(Long cid) {
        List<SpecGroup> specGroups = querySpecGroupByCid(cid);

        List<SpecParam> specParams = querySpecParams(null, cid, null, null);

        Map<Long, List<SpecParam>> map = new HashMap<>();
        //遍历specParams
        for (SpecParam param : specParams) {
            Long groupId = param.getGroupId();
            if (!map.keySet().contains(param.getGroupId())) {
                //map中key不包含这个组ID
                map.put(param.getGroupId(), new ArrayList<>());
            }
            //添加进map中
            map.get(param.getGroupId()).add(param);
        }

        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(map.get(specGroup.getId()));
        }

        return specGroups;
    }
}


