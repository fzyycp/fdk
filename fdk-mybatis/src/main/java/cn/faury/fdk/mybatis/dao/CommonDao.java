package cn.faury.fdk.mybatis.dao;

import cn.faury.fdk.common.db.Page;
import cn.faury.fdk.mybatis.page.PageBounds;
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
     * @param pageBounds 分页参数
     * @return 分页结果
     */
    <T> Page<T> selectPage(String statement, Object parameter, PageBounds pageBounds);

    /**
     * 分页检索
     *
     * @param statement  sql语句
     * @param pageBounds 分页参数
     * @return 分页结果
     */
    <T> Page<T> selectPage(String statement, PageBounds pageBounds);
	
}
