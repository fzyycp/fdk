/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.entry;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.utils.JsonUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 下拉框Bean
 */
public class ComboBoxEntry implements Serializable {
    /**
     * 值
     */
    private String value;

    /**
     * 文本
     */
    private String text;

    /**
     * 是否选中
     */
    private boolean selected;

    /**
     * 扩展属性
     */
    private Map<String, Object> attrs;

    /**
     * 构造函数
     *
     * @param value 值
     * @param text  文本
     */
    public ComboBoxEntry(@NonNull String value, @NonNull String text) {
        super();
        this.value = value;
        this.text = text;
        this.selected = false;
        this.attrs = new HashMap<String, Object>();
    }
    /**
     * 构造函数
     *
     * @param value 值
     * @param text  文本
     */
    public ComboBoxEntry(@NonNull String value, @NonNull String text,boolean selected) {
        this(value,text);
        this.selected = selected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(@NonNull Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
