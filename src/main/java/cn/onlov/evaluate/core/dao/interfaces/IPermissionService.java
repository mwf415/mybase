package cn.onlov.evaluate.core.dao.interfaces;

import cn.onlov.evaluate.core.dao.entities.Permission;
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
public interface IPermissionService extends IService<Permission> {

    List<Permission> loadUserPermissions(int id, int type);
    List<Permission> queryPermissionsListWithSelected(int rid);


}
