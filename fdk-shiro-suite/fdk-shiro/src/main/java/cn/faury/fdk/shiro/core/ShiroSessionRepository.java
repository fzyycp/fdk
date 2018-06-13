/*
 *
 * 基础类库 f-shiro
 *
 * @author faury
 *
 * 版权所有：秋刀鱼
 * Copyright (c) http://www.faury.cn
 *
 */

package cn.faury.fdk.shiro.core;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Session操作仓库
 */
public interface ShiroSessionRepository {

	/**
	 * 保存Session
	 * 
	 * @param session
	 *            Session信息
	 */
    void saveSession(Session session);

	/**
	 * 更新Session
	 * 
	 * @param session
	 *            Session信息
	 */
    void updateSession(Session session);

	/**
	 * 删除Session
	 * 
	 * @param sessionId
	 *            session id
	 */
    void deleteSession(Serializable sessionId);

	/**
	 * 获取Session
	 * 
	 * @param sessionId
	 *            session id
	 */
    Session getSession(Serializable sessionId);

	/**
	 * 删除登录用户
	 * 
	 * @param principal
	 *            登录名
	 */
	void deleteSessionOfPrincipal(Serializable principal);

	/**
	 * 删除登录用户
	 * 
	 * @param key
	 *            SessionBean 的key
	 */
	void deleteSessionOfKey(Serializable key);

	/**
	 * 获取所有用户Session
	 * 
	 * @return 所有用户Session
	 */
    Collection<Session> getAllSessions();

	/**
	 * 获取某用户所有已登录Session
	 * 
	 * @return 用户登录名
	 */
	Collection<Session> getAllSessionsOfPrincipal(Serializable principal);

	/**
	 * 查询SessionBean结果的对象
	 * 
	 * @param filter
	 *            过滤条件，只支持精确查询
	 * @return 查询结果
	 */
	List<SessionBean> querySessionBean(Map<String, Object> filter);
}