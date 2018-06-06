/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 6/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.entry;

/**
 * Rest返回码常量
 */
public enum RestResultCode {
	/**
	 * 500-系统异常
	 */
	CODE500("500", "系统异常", "软件错误"),
	/**
	 * 406-手机接口不兼容
	 */
	CODE406("406", "接口不兼容", "当前客户端版本已过时，请升级"),
	/**
	 * 404-接口不存在
	 */
	CODE404("404", "接口不存在", "软件错误"),
	/**
	 * 402-参数错误
	 */
	CODE402("402", "输入参数不合法", "参数错误"),
	/**
	 * 401-未登录
	 */
	CODE401("401", "用户未授权", "请登录"),
	/**
	 * 200-成功
	 */
	CODE200("200", "成功", "成功");

	/**
	 * 编码
	 */
	private String code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 用户提示信息
	 */
	private String tips;

	private RestResultCode(String code, String message, String tips) {
		this.setCode(code);
		this.setMessage(message);
		this.setTips(tips);
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the tips
	 */
	public String getTips() {
		return tips;
	}

	/**
	 * @param tips
	 *            the tips to set
	 */
	public void setTips(String tips) {
		this.tips = tips;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return String.format("{code=%s,message=%s,tips=%s}", this.getCode(), this.getMessage(), this.getTips());
	}

}
