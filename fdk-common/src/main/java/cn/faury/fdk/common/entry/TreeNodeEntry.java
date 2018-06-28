/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.entry;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.anotation.Nullable;
import cn.faury.fdk.common.utils.JsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点Bean
 */
public class TreeNodeEntry implements Serializable {

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点显示名
     */
    private String text;

    /**
     * 节点图标
     */
    private String iconCls;

    /**
     * 是否选中
     */
    private boolean checked = false;

    /**
     * 节点状态
     */
    private String state;

    /**
     * 是否为叶子节点
     */
    private boolean leaf=false;

    /**
     * 节点属性
     */
    private Map<String, String> attributes = new HashMap<String, String>();

    /**
     * 孩子节点的List
     */
    private List<TreeNodeEntry> children = new ArrayList<TreeNodeEntry>();

    /**
     * 构造函数
     *
     * @param id     id值
     * @param text   显示文本
     */
    public TreeNodeEntry(@NonNull String id, @NonNull String text) {
        this.id = id;
        this.text = text;
    }
    /**
     * 构造函数
     *
     * @param id     id值
     * @param text   显示文本
     * @param isLeaf 是否叶子节点
     */
    public TreeNodeEntry(@NonNull String id, @NonNull String text, boolean isLeaf) {
        this.id = id;
        this.text = text;
        this.leaf = isLeaf;
    }

    /**
     * 构造函数
     *
     * @param id     id值
     * @param text   显示文本
     * @param isLeaf 是否叶子节点
     * @param iconCls 节点icon
     * @param checked 是否选中
     * @param state 状态
     * @param attributes 附加属性
     * @param children 子节点
     */
    public TreeNodeEntry(@NonNull String id, @NonNull String text, boolean isLeaf, @Nullable String iconCls, boolean checked, @Nullable String state, @NonNull Map<String, String> attributes, @NonNull List<TreeNodeEntry> children) {
        this(id, text, isLeaf);
        this.iconCls = iconCls;
        this.checked = checked;
        this.state = state;
        this.attributes = attributes;
        this.children = children;
    }

    /**
     * 添加子节点
     *
     * @param child 子节点
     * @return 当前对象
     */
    public TreeNodeEntry addChildren(@NonNull TreeNodeEntry child) {
        if (child != null) {
            if (children == null) {
                children = new ArrayList<TreeNodeEntry>();
            }
            children.add(child);
        }
        return this;
    }

    /**
     * 添加子节点
     *
     * @param child 子节点
     * @return 当前对象
     */
    public TreeNodeEntry addChildren(@NonNull List<TreeNodeEntry> child) {
        if (child != null) {
            if (children == null) {
                children = new ArrayList<TreeNodeEntry>();
            }
            children.addAll(child);
        }
        return this;
    }

    /**
     * 获取id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id 值
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取text
     *
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * 设置text
     *
     * @param text 值
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 获取iconCls
     *
     * @return iconCls
     */
    public String getIconCls() {
        return iconCls;
    }

    /**
     * 设置iconCls
     *
     * @param iconCls 值
     */
    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    /**
     * 获取checked
     *
     * @return checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * 设置checked
     *
     * @param checked 值
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    /**
     * 获取state
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * 设置state
     *
     * @param state 值
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取leaf
     *
     * @return leaf
     */
    public boolean isLeaf() {
        return leaf;
    }

    /**
     * 设置leaf
     *
     * @param leaf 值
     */
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    /**
     * 获取attributes
     *
     * @return attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * 设置attributes
     *
     * @param attributes 值
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * 获取children
     *
     * @return children
     */
    public List<TreeNodeEntry> getChildren() {
        return children;
    }

    /**
     * 设置children
     *
     * @param children 值
     */
    public void setChildren(List<TreeNodeEntry> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
