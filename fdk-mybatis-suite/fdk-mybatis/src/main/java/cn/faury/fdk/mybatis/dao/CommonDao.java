package cn.faury.fdk.mybatis.dao;

import cn.faury.fdk.common.db.PageParam;
import cn.faury.fdk.common.db.PageInfo;
import org.apache.ibatis.session.SqlSession;

/**
 * 共通DAO操作
 */
public interface CommonDao extends BaseDao,SqlSession {

    /**
     * 分页检索
     *
     * @param statement  sql语句
     * @param parameter  sql占位参数
     * @param pageParam 分页参数
     * @return 分页结果
     */
    <T> PageInfo<T> selectPage(String statement, Object parameter, PageParam pageParam);

    /**
     * 分页检索
     *
     * @param statement  sql语句
     * @param pageParam 分页参数
     * @return 分页结果
     */
    <T> PageInfo<T> selectPage(String statement, PageParam pageParam);
	
}
