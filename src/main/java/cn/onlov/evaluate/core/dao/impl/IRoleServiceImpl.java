package cn.onlov.evaluate.core.dao.impl;

import cn.onlov.evaluate.core.dao.entities.CycleRole;
import cn.onlov.evaluate.core.dao.interfaces.IRoleService;
import cn.onlov.evaluate.core.dao.mapper.RoleMapper;
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
public class IRoleServiceImpl extends ServiceImpl<RoleMapper, CycleRole> implements IRoleService {


    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<CycleRole> queryRoleListByUserId(Integer roleId) {
        List<CycleRole> cycleRoles = roleMapper.queryRoleListByUserId(roleId);

        return cycleRoles;
    }
}
