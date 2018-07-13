package cn.faury.fdk.mobile.shiro.filter.oauth2;

import cn.faury.fdk.common.entry.RestResultCode;
import cn.faury.fdk.common.exception.TipsException;
import cn.faury.fdk.common.utils.AssertUtil;
import cn.faury.fdk.common.utils.JsonUtil;
import cn.faury.fdk.common.utils.StringUtil;
import cn.faury.fdk.http.client.HttpUtil;
import cn.faury.fdk.http.client.core.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 手机端OAuth2登录验证处理器：通用适配器
 */
public abstract class AdapterOAuth2Handler implements OAuth2Handler {

    /**
     * 日志记录器
     */
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * APP应用授权信息
     * <p>
     * <pre>
     *     格式："appCode.appid.secret,appCode.appid.secret"
     *      appCode：APP编码
     *      appid：应用唯一标识，在微信开放平台提交应用审核通过后获得
     *      secret：应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
     * </pre>
     */
    private String keys = "";

    /**
     * 获取授权APPID和密钥
     *
     * @param appCode app编码
     * @return 正确的结果
     */
    public String[] getAppIdAndSecret(String appCode) {
        // 参数校验
        AssertUtil.assertNotEmpty(appCode, new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录", "APP编码或授权code不可以为空"));
        AssertUtil.assertNotEmpty(getKeys(), new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录", "后台参数配置错误，缺少对应的appid和secret"));

        // 拆分得到每个APP的字符串
        String[] keyArr = getKeys().trim().split(",");
        AssertUtil.assertTrue(keyArr != null && keyArr.length > 0, new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录", "后台参数配置错误，缺少对应的appid和secret"));

        String[] cfg = null;
        for (String key : keyArr) {
            // 格式校验：appCode.appid.secret
            String[] appKey = key.trim().split("\\.");
            if (appKey == null || appKey.length < 3) {
                log.error("错误的配置参数【{}】，正确的格式为：appCode.appid.secret", key);
                continue;
            }
            // 获取对应AppCode的配置
            if (appCode.equals(appKey[0])) {
                cfg = new String[]{appKey[1], appKey[2]};
                break;
            }
        }

        AssertUtil.assertTrue(cfg != null && cfg.length == 2, new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录", "后台参数配置错误，缺少对应的appid和secret"));
        AssertUtil.assertNotEmpty(cfg[0], new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录", "后台参数配置错误，缺少对应的appid"));
        AssertUtil.assertNotEmpty(cfg[1], new TipsException(RestResultCode.CODE500.getCode(), "不支持的第三方登录", "后台参数配置错误，缺少对应的secret"));
        return cfg;
    }

    /**
     * 通过URL获取网络JSON数据，并转化为Map形式
     *
     * @param url 网络URL
     * @return Map结果的JSON数据
     */
    public Map<String, Object> getUrlJsonToMap(String url) {
        Map<String, Object> map = null;
        String result;
        try {
            HttpResponse response = HttpUtil.get(url);
            result = response.getStringResult();
        } catch (Exception e) {
            result = null;
            if (log.isErrorEnabled()) {
                log.error("网络请求异常：" + url, e);
            }
        }
        if (StringUtil.isNotEmpty(result)) {
            try {
                map = JsonUtil.jsonToMap(result);
            } catch (Exception e) {
                map = null;
                if (log.isErrorEnabled()) {
                    log.error("网络返回JSON格式解析异常：" + result, e);
                }
            }
        }
        return map;
    }


    /**
     * 获取返回值中超时字段时间
     *
     * @param result 得到的返回结果JSON
     * @param key    时间字段的key
     * @return 超时时间，未获取到默认为0
     */
    public static Long getExpiresIn(Map<String, Object> result, String key) {
        String expiresKey = StringUtil.emptyDefault(key, "expires_in");
        Long expires_in = 0L;
        if (result != null) {
            try {
                expires_in = Long.parseLong(String.valueOf(result.get(key)));
            } catch (Exception ignored) {
            }
        }
        return expires_in;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }
}
