/*##############################################################################
 # 基础类库：fdk-common
 #
 # @date 5/2018
 # @author faury
 #############################################################################*/
package cn.faury.fdk.common.entry;

import cn.faury.fdk.common.anotation.NonNull;
import cn.faury.fdk.common.anotation.Nullable;

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
    private boolean leaf;

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
     * @param isLeaf 是否叶子节点
     */
    public TreeNodeEntry(@NonNull String id, @NonNull String text, boolean isLeaf) {
        this.id = id;
        this.text = text;
        this.leaf = isLeaf;
    }

    public TreeNodeEntry(@NonNull String id,@NonNull  String text,@Nullable String iconCls, boolean checked, @Nullable String state, boolean leaf,@NonNull Map<String, String> attributes,@NonNull List<TreeNodeEntry> children) {
        this(id,text,leaf);
        this.iconCls = iconCls;
        this.checked = checked;
        this.state = state;
        this.attributes = attributes;
        this.children = children;
    }

    /**
     * 添加子节点
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

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(@Nullable String iconCls) {
        this.iconCls = iconCls;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getState() {
        return state;
    }

    public void setState(@Nullable String state) {
        this.state = state;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(@NonNull Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<TreeNodeEntry> getChildren() {
        return children;
    }

    public void setChildren(@NonNull List<TreeNodeEntry> children) {
        this.children = children;
    }
}
