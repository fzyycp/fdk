/*
 * Copyright (c)
 */

package cn.faury.fdk.mybatis.autoconfigure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 用户信息表Mapper
 */
@Mapper
public interface TestMapper {

    @Select("select count(*) from idev_api_role")
    int selectCount(Map<String, Object> map);

}
