package cn.onlov.evaluate.service.impl;

import cn.onlov.evaluate.core.dao.entities.RolePermission;
import cn.onlov.evaluate.core.dao.interfaces.IRolePermissionService;
import cn.onlov.evaluate.service.CycleRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CycleRolePermissionServiceImpl  implements CycleRolePermissionService {
   @Autowired
   private IRolePermissionService iRolePermissionService;


	@Override
    //更新权限
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor={Exception.class})
    @CacheEvict(cacheNames="permissions", allEntries=true)
    public void addRolePermission(Integer rid, Integer[] pids){

		QueryWrapper<RolePermission> queryWrapper =  new QueryWrapper<>() ;
		iRolePermissionService.remove(queryWrapper.lambda().eq(RolePermission::getRid,rid));
        //添加
        for(Integer pid: pids){
			RolePermission record = new RolePermission();
        	record.setRid(rid);
        	record.setPid(pid);
        	iRolePermissionService.save(record);
        }
	}

	@Override
	public void deleteByPermissionKeys(String[] ids) {
		QueryWrapper<RolePermission> queryWrapper =  new QueryWrapper<>() ;
		iRolePermissionService.remove(queryWrapper.lambda().in(RolePermission::getPid,ids));
	}
}
