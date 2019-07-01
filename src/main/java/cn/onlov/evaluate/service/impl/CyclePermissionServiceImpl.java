package cn.onlov.evaluate.service.impl;

import cn.onlov.evaluate.constants.Constants;
import cn.onlov.evaluate.core.dao.entities.Permission;
import cn.onlov.evaluate.core.dao.entities.RolePermission;
import cn.onlov.evaluate.core.dao.interfaces.IPermissionService;
import cn.onlov.evaluate.core.dao.interfaces.IRolePermissionService;
import cn.onlov.evaluate.pojo.bo.CyclePermissionBo;
import cn.onlov.evaluate.service.CyclePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CyclePermissionServiceImpl implements CyclePermissionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private IPermissionService iPermissionService;

    @Autowired
    private IRolePermissionService iRolePermissionService;


    @Override
    public IPage<Permission> selectByPage(CyclePermissionBo bo) {
        IPage<Permission> page = new Page<>();
        page.setCurrent(bo.getCurr()).setSize(bo.getPageSize());

        IPage<Permission> res = iPermissionService.page(page, new QueryWrapper<Permission>().lambda().orderByDesc(Permission::getId));
        return res;

    }

    @Override
    @Cacheable(value = "CyclePermissions", key = "'all'")
    public List<Permission> queryAll() {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();

        List<Permission> list = iPermissionService.list(queryWrapper);
        return list;
    }

    @Override
    @Cacheable(value = "permissions", key = "'all_menu'")
    public List<Permission> queryAllMenu() {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Permission::getSystemId, Constants.SYSTEM_EVALUATE_ID).eq(Permission::getType, Constants.MENU_TYPE).orderByAsc(Permission::getId);
        List<Permission> list = iPermissionService.list(queryWrapper);
        return list;
    }

    @Override
    @Cacheable(value = "permissions", key = "'list_'+#map['id'].toString()+'_'+#map['type']")
    public List<Permission> loadUserCyclePermissions(Map<String, Object> map) {
        logger.debug("loadUserPermissions id={},type={}", new Object[]{map.get("id"), map.get("type")});
        int id = (int) map.get("id");
        int type = (int) map.get("type");
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        List<Permission> list = iPermissionService.loadUserPermissions(id, type);
        return list;
    }

    @Override
    public List<Permission> queryCyclePermissionsListWithSelected(Integer rid) {
        List<Permission> list = iPermissionService.queryPermissionsListWithSelected(rid);
        return list;
    }

    @Override
    @Cacheable(value = "permissions", key = "'tree_'+#userId")
    public List<Permission> loadUserCyclePermissionsTree(Integer userId) {
        return getPermissions(userId);
    }

    @Override
    @CachePut(value = "permissions", key = "'tree_'+#userId")
    public List<Permission> updateUserCyclePermissionsTree(Integer userId) {
        return getPermissions(userId);
    }

    private List<Permission> getPermissions(Integer userId) {
        logger.debug("loadUserPermissionsTree userId={}", userId);
        Map<String, Object> map = new HashMap();
        map.put("type", 1);
        map.put("id", userId);
        List<Permission> userPermissions = null;
        if (userId == 1) {
            userPermissions = queryAllMenu();
        } else {
            userPermissions = loadUserCyclePermissions(map);
        }
        List<Permission> list = getChildren(userPermissions, 0);
        return list;
    }


    // 取节点的所有children
    private List<Permission> getChildren(List<Permission> results, Integer rootId) {

        List<Permission> list = new ArrayList();
        for (int i = 0; i < results.size(); i++) {
            Permission root = results.get(i);
            if (rootId .equals( root.getPid())) {
                List<Permission> children = this.getChildren(results, root.getId());
                if (!children.isEmpty()) {
                    root.setChildren(children);
                }
                list.add(root);
            }
        }
        return list;
    }

    @Override
    public void deleteByKeys(String[] keys) {
        //删除资源
        if (keys != null) {
            //删除关联数据
            QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
            iPermissionService.remove(queryWrapper.lambda().in(Permission::getId, keys));


            QueryWrapper<RolePermission> queryWrapperRole = new QueryWrapper<>();
            iRolePermissionService.remove(queryWrapperRole.lambda().in(RolePermission::getPid, keys));

        }
    }


}
