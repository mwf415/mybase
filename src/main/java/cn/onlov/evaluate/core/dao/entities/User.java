package cn.onlov.evaluate.core.dao.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
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
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("userNum")
    private String userNum;

    @TableField("hospitalId")
    private String hospitalId;

    @TableField("loginName")
    private String loginName;

    private String realName;

    private String userPwd;

        /**
     * 1：老师 2：学生 3：助理老师
     */
         private Integer identityId;

    private String sex;

    private Integer qualificationId;

    private String dept;

    private Integer nation;

    private String cardNo;

    private Date birthTime;

    private Integer education;

    private String image;

    private Integer isAdmin;

    private Integer userTypeId;

    private Integer isLogin;

    private String loginIp;

    private Integer departmentId;

    private Integer usbKey;

    private String moduleManager;

    private String status;

    @TableField("baseName")
    private String baseName;

    @TableField("roomName")
    private String roomName;

    private String address;

    @TableField("gradSchool")
    private String gradSchool;

    private String major;

    private String degree;

    private String xuewei;

    @TableField("certificationNum")
    private String certificationNum;

    @TableField("cellPhone")
    private String cellPhone;

    private String staff;

    @TableField("trainTime")
    private Integer trainTime;

    private String email;

    private Integer grade;

        /**
     * 是否已经安排轮转
     */
         @TableField("isAt")
    private Integer isAt; // 0 代表没安排，1代表安排


}
