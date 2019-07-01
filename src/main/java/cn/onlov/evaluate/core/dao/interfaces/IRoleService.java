package cn.onlov.evaluate.core.dao.interfaces;

import cn.onlov.evaluate.core.dao.entities.CycleRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kaifa
 * @since 2019-01-04
 */
public interface IRoleService extends IService<CycleRole> {

    List<CycleRole> queryRoleListByUserId(Integer roleId);

}
