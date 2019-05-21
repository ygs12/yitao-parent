package com.gerry.yitao.seller.service;

import com.gerry.yitao.domain.SpecGroup;
import com.gerry.yitao.domain.SpecParam;

import java.util.List;

public interface SpecService {

    List<SpecGroup> querySpecGroupByCid(Long cid);

    void saveSpecGroup(SpecGroup specGroup);

    void deleteSpecGroup(Long id);

    void updateSpecGroup(SpecGroup specGroup);

    List<SpecParam> querySpecParams(Long gid, Long cid, Boolean searching, Boolean generic);

    void saveSpecParam(SpecParam specParam);

    void updateSpecParam(SpecParam specParam);

    void deleteSpecParam(Long id);

    List<SpecGroup> querySpecsByCid(Long cid);
}
