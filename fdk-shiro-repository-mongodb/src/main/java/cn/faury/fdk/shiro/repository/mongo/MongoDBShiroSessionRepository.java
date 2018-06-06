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

package cn.faury.fdk.shiro.repository.mongo;

import cn.faury.fdk.common.utils.RandomUtil;
import cn.faury.fdk.common.utils.SerializeUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.shiro.core.SessionBean;
import cn.faury.fdk.shiro.core.ShiroSessionRepository;
import com.mongodb.MongoClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.util.*;

/**
 * 采用MongoDB存储
 */
public class MongoDBShiroSessionRepository implements ShiroSessionRepository {

    /**
     * 日志记录器
     */
    private static final Logger log = LoggerFactory.getLogger(MongoDBShiroSessionRepository.class);

    // 认证session
    private static final String CONST_RELOGIN_SESSION = "fdk-shiro-re-login-session";
    /**
     * 默认数据库名
     */
    public static final String DFT_SESSION_DB_NAME = "FdkSessionDB";

    /**
     * 默认集合名
     */
    public static final String DFT_SESSION_COLL_NAME = "FdkSessions";

    /**
     * Mongo操作工具
     */
    private MongoClient mongoClient;
    // 操作模板
    private MongoTemplate mongoTemplate;

    /**
     * 配置的数据库名
     */
    private String dbName = DFT_SESSION_DB_NAME;

    /**
     * 配置的集合名
     */
    private String collName = DFT_SESSION_COLL_NAME;

    /**
     * 构造函数
     *
     * @param mongoClient 模板对象
     */
    public MongoDBShiroSessionRepository(MongoClient mongoClient) {
        this(mongoClient, DFT_SESSION_DB_NAME, DFT_SESSION_COLL_NAME);
    }

    /**
     * 构造函数
     *
     * @param mongoClient MongoDB客户端
     * @param dbName      DB名称
     * @param collName    Collection名称
     */
    public MongoDBShiroSessionRepository(MongoClient mongoClient, String dbName, String collName) {
        this.mongoClient = mongoClient;
        this.setDbName(dbName);
        this.setCollName(collName);
        this.mongoTemplate = new MongoTemplate(mongoClient, this.getDbName());
    }

    @Override
    public void saveSession(Session session) {
        if (session == null || session.getId() == null) {
            log.error("saveSession：session或者session id为空");
            return;
        }
        log.trace("{}", "saveSession..." + session.getId());
        SessionBean bean = new SessionBean();
        bean.setKey(getSessionKey(session.getId()));
        bean.setValue(SerializeUtil.serialize(session));
        if (session.getAttribute("principal") != null) {
            bean.setPrincipal(session.getAttribute("principal").toString());
        }
        bean.setHost(session.getHost());
        if (session.getAttribute("sysCode") != null) {
            bean.setSysCode(session.getAttribute("sysCode").toString());
        }
        bean.setStartTimestamp(session.getStartTimestamp());
        bean.setLastAccessTime(session.getLastAccessTime());
        bean.setTimeoutTime(getTimeoutTime(session.getStartTimestamp(), session.getTimeout()));
        this.mongoTemplate.insert(bean, collName);
        log.trace("{}", String.format("saveSession[id=%s,dbname=%s,collection=%s]", bean.getKey(), dbName, collName));
    }

    @Override
    public void updateSession(Session session) {
        if (session == null || session.getId() == null) {
            log.error("updateSession：session或者session id为空");
            return;
        }
        if (log.isTraceEnabled()) {
            log.trace("updateSession..." + session.getId());
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(getSessionKey(session.getId())));

        Update update = new Update();
        update.set("value", SerializeUtil.serialize(session));
        Subject s = SecurityUtils.getSubject();
        if (s != null && s.getPrincipal() != null) {
            update.set("principal", s.getPrincipal());
        } else {
            Object principal = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (principal != null && principal instanceof PrincipalCollection) {
                PrincipalCollection spc = (PrincipalCollection) principal;
                if (spc != null) {
                    update.set("principal", spc.getPrimaryPrincipal());
                }
            } else {
                update.set("principal", null);
            }
        }
        if (StringUtil.isNotEmpty(session.getHost())) {
            update.set("host", session.getHost());
        } else {
            update.set("host", null);
        }
        update.set("startTimestamp", session.getStartTimestamp());
        update.set("lastAccessTime", session.getLastAccessTime());
        update.set("timeoutTime", getTimeoutTime(session.getLastAccessTime(), session.getTimeout()));
        this.mongoTemplate.updateFirst(query, update, SessionBean.class);
    }

    @Override
    public void deleteSession(Serializable sessionId) {
        deleteSessionOfKey(getSessionKey(sessionId));
    }

    @Override
    public Session getSession(Serializable sessionId) {
        log.trace("{}", "getSession..." + sessionId);
        if (sessionId == null) {
            log.error("getSession：sessionId为空");
            return null;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(sessionId));
        List<SessionBean> sessionBeans = this.mongoTemplate.find(query, SessionBean.class, collName);
        SimpleSession simpleSession = null;
        for (SessionBean bean : sessionBeans) {
            Date date = bean.getTimeoutTime();
            if (bean.getValue() != null && (date.getTime() - System.currentTimeMillis()) > 0) {
                simpleSession = SerializeUtil.deserialize(bean.getValue(), SimpleSession.class);
                break;
            } else {
                deleteSession(getSessionKey(sessionId));
            }
        }

        return simpleSession;
    }

    @Override
    public void deleteSessionOfPrincipal(Serializable principal) {
        log.trace("{}", "deleteSessionOfPrincipal..." + principal);
        if (principal == null) {
            log.error("deleteSessionOfPrincipal：principal为空");
            return;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("principal").is(principal));
        this.mongoTemplate.remove(query, SessionBean.class);
    }

    @Override
    public void deleteSessionOfKey(Serializable key) {
        log.trace("{}", "deleteSessionOfKey..." + key);
        if (key == null) {
            log.error("{}", "deleteSessionOfKey：key为空");
            return;
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));
        this.mongoTemplate.remove(query, SessionBean.class);
    }

    //  转换为Session对象
    private Collection<Session> sessionBeansToSession(List<SessionBean> sessionBeans) {
        Collection<Session> collection = new ArrayList<>();
        if (sessionBeans != null) {
            sessionBeans.forEach(sessionBean -> {
                Date date = sessionBean.getTimeoutTime();
                if (sessionBean.getValue() != null && date != null && ((date.getTime() - System.currentTimeMillis()) > 0)) {
                    collection.add(SerializeUtil.deserialize(sessionBean.getValue(), SimpleSession.class));
                } else {
                    deleteSessionOfKey(sessionBean.getKey());
                }
            });
        }
        return collection;
    }

    @Override
    public Collection<Session> getAllSessions() {
        List<SessionBean> sessionBeans = this.mongoTemplate.findAll(SessionBean.class, collName);
        return sessionBeansToSession(sessionBeans);
    }

    @Override
    public Collection<Session> getAllSessionsOfPrincipal(Serializable principal) {
        Map<String, Object> filter = new HashMap<>();
        filter.put("principal", principal);
        return sessionBeansToSession(querySessionBean(filter));
    }

    @Override
    public List<SessionBean> querySessionBean(Map<String, Object> filter) {
        Query query = new Query();
        if (filter != null && filter.size() > 0) {
            filter.forEach((key, value) -> {
                query.addCriteria(Criteria.where(key).is(value));
            });
        }
        return this.mongoTemplate.find(query, SessionBean.class, collName);
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        if (StringUtil.isEmpty(dbName)) {
            this.dbName = DFT_SESSION_DB_NAME;
        } else {
            this.dbName = dbName;
        }
    }

    /**
     * @return the collName
     */
    public String getCollName() {
        return collName;
    }

    /**
     * @param collName the collName to set
     */
    public void setCollName(String collName) {
        if (StringUtil.isEmpty(collName)) {
            this.collName = DFT_SESSION_COLL_NAME;
        } else {
            this.collName = collName;
        }
    }

    /**
     * 获取session key
     *
     * @param sessionId
     * @return
     */
    private synchronized String getSessionKey(Serializable sessionId) {
        return RandomUtil.getMD5String(CONST_RELOGIN_SESSION + sessionId);
    }

    /**
     * @param startTime
     * @param timeout
     * @return
     */
    private Date getTimeoutTime(Date startTime, long timeout) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTimeInMillis(startTime.getTime() + timeout);
        return cal.getTime();
    }
}
