package cn.faury.fdk.ftp.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 抽象对象池
 */
public abstract class Pool<T> {

	/**
	 * 日志记录器
	 */
	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 对象池
	 */
	private final GenericObjectPool<T> pool;

	/**
	 * 构造函数
	 * 
	 * @param poolConfig
	 *            缓冲池配置
	 * @param factory
	 *            池化对象工厂
	 */
	public Pool(GenericObjectPoolConfig poolConfig, PooledObjectFactory<T> factory) {
		this.pool = new GenericObjectPool<T>(factory, poolConfig);
	}

	/**
	 * 从缓冲池中获取对象
	 * 
	 * @return 缓冲对象
	 */
	public T getResource() {
		try {
			return this.pool.borrowObject();
		} catch (Exception e) {
			log.error("从缓冲池中获取对象异常！", e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将对象放入缓冲池
	 * 
	 * @param resource
	 *            要放入缓冲池的对象
	 */
	public void returnResource(T resource) {
		if (resource == null) {
			return;
		}
		try {
			this.pool.returnObject(resource);
		} catch (Exception e) {
			log.error("对象放入缓冲池异常！", e);
		}
	}

	/**
	 * 销毁缓冲池
	 */
	public void destory() {
		try {
			this.pool.close();
		} catch (Exception e) {
			log.error("销毁对象池异常！", e);
			e.printStackTrace();
		}
	}

	/**
	 * 获取对象池中空闲对象个数
	 * 
	 * @return 空闲对象个数
	 */
	public int getNumIdle() {
		try {
			return this.pool.getNumIdle();
		} catch (Exception e) {
			log.error("获取对象池中空闲对象个数异常！", e);
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取对象池中正在使用的对象个数
	 * 
	 * @return 对象池中正在使用的对象个数
	 */
	public int getNumActive() {
		try {
			return this.pool.getNumActive();
		} catch (Exception e) {
			log.error("获取对象池中正在使用的对象个数异常！", e);
			e.printStackTrace();
		}
		return 0;
	}
}
