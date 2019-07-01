package cn.onlov.evaluate.core.dao.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
public class CycleRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "roleId", type = IdType.AUTO)
    private Integer roleId;

        /**
     * 角色名称
     */
         @TableField("roleName")
    private String roleName;

        /**
     * 角色编号
     */
         @TableField("roleNum")
    private String roleNum;

        /**
     * 角色描述
     */
         @TableField("roleDes")
    private String roleDes;


}
