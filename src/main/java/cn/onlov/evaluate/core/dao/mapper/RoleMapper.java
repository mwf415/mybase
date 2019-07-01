package cn.onlov.evaluate.core.dao.mapper;

import cn.onlov.evaluate.core.dao.entities.CycleRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kaifa
 * @since 2019-01-04
 */
public interface RoleMapper extends BaseMapper<CycleRole> {

    @Select(  "   SELECT r.* FROM cycle_role r" +
            "LEFT JOIN cycle_user_role ur ON r.roleId=ur.rid" +
            "LEFT JOIN `user` u ON ur.uid=u.id" +
            "WHERE u.id=#{userId} ORDER BY r.roleId")
    List<CycleRole> queryRoleListByUserId(@Param("roleId") Integer roleId);


}
