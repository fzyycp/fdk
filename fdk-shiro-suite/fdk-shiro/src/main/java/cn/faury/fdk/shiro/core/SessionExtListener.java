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
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Session监听器
 */
public class SessionExtListener implements SessionListener {

	/**
	 * 日志记录器
	 */
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void onStart(Session session) {
		log.debug(String.format("onStart[sessionId=%s]", session == null ? "" : session.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStop(Session session) {
		log.debug(String.format("onStop[sessionId=%s],will be deleted!", session == null ? "" : session.getId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onExpiration(Session session) {
		log.debug(String.format("onExpiration[sessionId=%s],will be deleted!", session == null ? "" : session.getId()));
    }
}
