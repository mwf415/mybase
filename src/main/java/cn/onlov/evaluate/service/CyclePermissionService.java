package cn.onlov.evaluate.service;

import cn.onlov.evaluate.core.dao.entities.Permission;
import cn.onlov.evaluate.pojo.bo.CyclePermissionBo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * Created by yangqj on 2017/4/25.
 */
public interface CyclePermissionService  {
	IPage<Permission> selectByPage(CyclePermissionBo CyclePermission);
	List<Permission> queryAll();
	List<Permission> queryAllMenu();
	List<Permission> loadUserCyclePermissions(Map<String, Object> map);
	List<Permission> queryCyclePermissionsListWithSelected(Integer rid);
	List<Permission> loadUserCyclePermissionsTree(Integer userId);
	List<Permission> updateUserCyclePermissionsTree(Integer userId);
	void deleteByKeys(String[] keys);
}
