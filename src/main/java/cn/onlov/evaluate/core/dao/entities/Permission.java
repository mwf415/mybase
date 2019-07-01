package cn.onlov.evaluate.core.dao.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 *
 * </p>
 *
 * @author kaifa
 * @since 2019-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

        /**
     * 主键
     */
         @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

        /**
     * url描述
     */
        private String name;

        /**
     * url地址
     */
         private String url;

    private Integer pid;

    private Integer type;

    @TableField("system_id")
    private Integer systemId;  //2 规培，2  osce

    private Integer sort;

    @TableField(exist = false)
    private String checked;//是否选中

@TableField(exist = false)
    private List children;//子菜单


}
