package cn.faury.fdk.mobile.controller;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.entry.RestResultEntry;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 手机接口Action基类
 */
public abstract class MobileBaseController {

    /**
     * 日志记录
     */
    protected Logger log = LoggerFactory.getLogger(MobileBaseController.class);

    /**
     * 返回错误对象
     *
     * @param code    错误码
     * @param message 错误信息
     * @param tips    用户提示信息
     * @return 错误结果
     */
    protected RestResultEntry rendError(String code, String message, String tips) {
        return RestResultEntry.createResult(false, code, message, tips, null);
    }

    /**
     * 返回错误对象
     *
     * @param code 错误枚举
     * @return 错误结果
     */
    protected RestResultEntry rendError(RestResultCode code) {
        return rendError(code.getCode(), code.getMessage(), code.getTips());
    }

    /**
     * rendSystemError:(返回系统未响应错误). <br/>
     *
     * @return 错误对象
     */
    protected RestResultEntry rendNoSuchMethodError() {
        return rendError(RestResultCode.CODE404);
    }

    /**
     * rendSystemError:(返回系统异常错误). <br/>
     *
     * @return 错误对象
     */
    protected RestResultEntry rendSystemError() {
        return rendError(RestResultCode.CODE500);
    }

    /**
     * rendVersionObsoleteError:(返回需要升级版本错误)
     *
     * @return 错误对象
     */
    protected RestResultEntry rendVersionObsoleteError() {
        return rendError(RestResultCode.CODE406);
    }

    /**
     * rendNoLoginError:(返回未登录错误). <br/>
     *
     * @return 返回结果
     */
    protected RestResultEntry rendNoLoginError() {
        return rendError(RestResultCode.CODE401.getCode(), RestResultCode.CODE401.getMessage(),
                RestResultCode.CODE401.getTips());
    }

    /**
     * authenticationCookie:(进行权限验证). <br/>
     */
    protected boolean authenticationCookie() {
        String sessionId = getSessionId();
        // 从参数中获取JSESSIONID_S值
        if (sessionId == null || sessionId.trim().length() <= 0) {
            log.debug("{}", "输入参数S为空");
            return false;
        }

        log.debug("输入参数【S={}】", sessionId);

        // 验证是否已登录
        try {
            Subject sub = new Subject.Builder().sessionId(sessionId).buildSubject();
            if (sub != null) {
                log.debug("获取登录令牌成功：【S={}】", sessionId);
                if (sub.isAuthenticated()) {
                    log.debug("验证登录令牌已登录：【S={}】", sessionId);
                    // 更新最后登录时间
                    if (sub.getSession(false) != null) {
                        sub.getSession(false).touch();
                        log.debug("更新登录令牌成功：【S={}】", sessionId);
                    }
                    return true;
                } else {
                    log.debug("登录令牌未成功登录或已超时：【S={}】", sessionId);
                }
            } else {
                log.debug("{}", "获取登录令牌为NULL");
            }
        } catch (RuntimeException re) {
            log.error("获取登录令牌异常：{}", re.getMessage(), re);
        }

        return false;
    }

    protected abstract String getSessionId();
}
