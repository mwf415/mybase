package cn.onlov.evaluate.core.dao.impl;

import cn.onlov.evaluate.core.dao.entities.Permission;
import cn.onlov.evaluate.core.dao.interfaces.IPermissionService;
import cn.onlov.evaluate.core.dao.mapper.PermissionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kaifa
 * @since 2019-01-04
 */
@Service
public class IPermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> loadUserPermissions(int id, int type){
        List<Permission> list = permissionMapper.loadUserPermissions(id, type);
        return list;
    }


    @Override
    public List<Permission> queryPermissionsListWithSelected(int rid){
        List<Permission> list = permissionMapper.queryPermissionsListWithSelected(rid);
        return list;
    }

}
