package cn.faury.fdk.mybatis.dao.impl;

import cn.faury.fdk.common.db.PageParam;
import cn.faury.fdk.mybatis.dao.CommonDao;
import cn.faury.fdk.common.db.PageInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageRowBounds;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * 通用Dao操作类
 */
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectOne(java.lang.String)
	 */
	@Override
	public <T> T selectOne(String statement) {
		return this.sqlSessionTemplate.selectOne(statement);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectOne(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> T selectOne(String statement, Object parameter) {
		return this.sqlSessionTemplate.selectOne(statement, parameter);				
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectList(java.lang.String)
	 */
	@Override
	public <E> List<E> selectList(String statement) {
		return this.sqlSessionTemplate.selectList(statement);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectList(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		return this.sqlSessionTemplate.selectList(statement,parameter);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectList(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds)
	 */
	@Override
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return this.sqlSessionTemplate.selectList(statement,parameter,rowBounds);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.String)
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return this.sqlSessionTemplate.selectMap(statement, mapKey);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.Object, java.lang.String)
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return this.sqlSessionTemplate.selectMap(statement, parameter, mapKey);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#selectMap(java.lang.String, java.lang.Object, java.lang.String, org.apache.ibatis.session.RowBounds)
	 */
	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return this.sqlSessionTemplate.selectMap(statement, parameter, mapKey, rowBounds);
	}

	@Override
	public <T> Cursor<T> selectCursor(String s) {
		return this.sqlSessionTemplate.selectCursor(s);
	}

	@Override
	public <T> Cursor<T> selectCursor(String s, Object o) {
		return this.sqlSessionTemplate.selectCursor(s,o);
	}

	@Override
	public <T> Cursor<T> selectCursor(String s, Object o, RowBounds rowBounds) {
		return this.sqlSessionTemplate.selectCursor(s,o,rowBounds);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#select(java.lang.String, java.lang.Object, org.apache.ibatis.session.ResultHandler)
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public void select(String statement, Object parameter, ResultHandler handler) {
		this.sqlSessionTemplate.select(statement, parameter, handler);;
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#select(java.lang.String, org.apache.ibatis.session.ResultHandler)
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public void select(String statement, ResultHandler handler) {
		this.sqlSessionTemplate.select(statement, handler);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#select(java.lang.String, java.lang.Object, org.apache.ibatis.session.RowBounds, org.apache.ibatis.session.ResultHandler)
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		this.sqlSessionTemplate.select(statement, parameter, rowBounds, handler);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#insert(java.lang.String)
	 */
	@Override
	public int insert(String statement) {
		return this.sqlSessionTemplate.insert(statement);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public int insert(String statement, Object parameter) {
		return this.sqlSessionTemplate.insert(statement, parameter);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#update(java.lang.String)
	 */
	@Override
	public int update(String statement) {
		return this.sqlSessionTemplate.update(statement);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#update(java.lang.String, java.lang.Object)
	 */
	@Override
	public int update(String statement, Object parameter) {
		return this.sqlSessionTemplate.update(statement, parameter);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#delete(java.lang.String)
	 */
	@Override
	public int delete(String statement) {
		return this.sqlSessionTemplate.delete(statement);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#delete(java.lang.String, java.lang.Object)
	 */
	@Override
	public int delete(String statement, Object parameter) {
		return this.sqlSessionTemplate.delete(statement, parameter);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#commit()
	 */
	@Override
	public void commit() {
		this.sqlSessionTemplate.commit();
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#commit(boolean)
	 */
	@Override
	public void commit(boolean force) {
		this.sqlSessionTemplate.commit(force);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#rollback()
	 */
	@Override
	public void rollback() {
		this.sqlSessionTemplate.rollback();
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#rollback(boolean)
	 */
	@Override
	public void rollback(boolean force) {
		this.sqlSessionTemplate.rollback(force);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#flushStatements()
	 */
	@Override
	public List<BatchResult> flushStatements() {
		return this.sqlSessionTemplate.flushStatements();
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#close()
	 */
	@Override
	public void close() {
		this.sqlSessionTemplate.close();
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#clearCache()
	 */
	@Override
	public void clearCache() {
		this.sqlSessionTemplate.clearCache();
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#getConfiguration()
	 */
	@Override
	public Configuration getConfiguration() {
		return this.sqlSessionTemplate.getConfiguration();
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#getMapper(java.lang.Class)
	 */
	@Override
	public <T> T getMapper(Class<T> type) {
		return this.sqlSessionTemplate.getMapper(type);
	}

	/* (non-Javadoc)
	 * @see org.apache.ibatis.session.SqlSession#getConnection()
	 */
	@Override
	public Connection getConnection() {
		return this.sqlSessionTemplate.getConnection();
	}

	/**
	 * 分页检索
	 *
	 * @param statement sql语句
	 * @param parameter sql占位参数
	 * @param pageParam 分页参数
	 * @return 分页结果
	 */
	@Override
	public <T> PageInfo<T> selectPage(String statement, Object parameter, PageParam pageParam) {
		PageHelper.startPage(pageParam.getPageNo(),pageParam.getPageSize());
		List<T> list = selectList(statement, parameter, new PageRowBounds(pageParam.getPageNo(),pageParam.getPageSize()));
		return new PageInfo<>(list);
	}

	/**
	 * 分页检索
	 *
	 * @param statement sql语句
	 * @param pageParam 分页参数
	 * @return 分页结果
	 */
	@Override
	public <T> PageInfo<T> selectPage(String statement, PageParam pageParam) {
		return selectPage(statement, null, pageParam);
	}
}
