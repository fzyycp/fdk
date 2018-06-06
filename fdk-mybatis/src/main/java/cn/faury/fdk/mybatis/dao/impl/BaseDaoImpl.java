package cn.faury.fdk.mybatis.dao.impl;


import cn.faury.fdk.mybatis.dao.BaseDao;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * 基础DAO实现类，提供SqlSessionTemplate
 */
public class BaseDaoImpl implements BaseDao {

	/**
	 * Mybatis执行实体
	 */
	protected SqlSessionTemplate sqlSessionTemplate;

	/**
	 * @return the sqlSessionTemplate
	 */
	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	/**
	 * @param sqlSessionTemplate
	 *            the sqlSessionTemplate to set
	 */
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

}
