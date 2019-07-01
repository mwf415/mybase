package cn.onlov.evaluate.controller;


import cn.onlov.evaluate.core.dao.entities.Permission;
import cn.onlov.evaluate.core.dao.interfaces.IPermissionService;
import cn.onlov.evaluate.pojo.bo.CyclePermissionBo;
import cn.onlov.evaluate.service.CyclePermissionService;
import cn.onlov.evaluate.shiro.ShiroService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Resource
    private CyclePermissionService cyclePermissionService;
    @Resource
    private IPermissionService iPermissionService;
    @Resource
    private ShiroService shiroService;

    @RequestMapping
    public Map<String,Object> getAll(Permission permissions, String draw,
                                     @RequestParam(required = false, defaultValue = "1") int start,
                                     @RequestParam(required = false, defaultValue = "10") int length){
        Map<String,Object> map = new HashMap<>();
        CyclePermissionBo bo  = new CyclePermissionBo();
        BeanUtils.copyProperties(permissions,bo);
        bo.setCurr(start);
        bo.setPageSize(length);

        IPage<Permission> pageInfo = cyclePermissionService.selectByPage(bo);
        map.put("draw",draw);
        map.put("recordsTotal",pageInfo.getTotal());
        map.put("recordsFiltered",pageInfo.getTotal());
        map.put("data", pageInfo.getRecords());
        return map;
    }

    @RequestMapping("/permissionsWithSelected")
    public List<Permission> permissionsWithSelected(Integer rid){
        return cyclePermissionService.queryCyclePermissionsListWithSelected(rid);
    }

    @RequestMapping("/loadMenu")
    public List<Permission> loadMenu(){
        Integer userId = (Integer) SecurityUtils.getSubject().getSession().getAttribute("userSessionId");
        List<Permission> permissionsList = cyclePermissionService.loadUserCyclePermissionsTree(userId);
        return permissionsList;
    }

    @CacheEvict(cacheNames="permissions", allEntries=true)
    @RequestMapping(value = "/add")
    public String add(Permission permission){
        try{
            iPermissionService.saveOrUpdate(permission);
            //更新权限
            shiroService.updatePermission();
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
    
    @CacheEvict(cacheNames="permissions", allEntries=true)
    @RequestMapping(value = "/update")
    public String update(Permission permission){
        try{
            iPermissionService.saveOrUpdate(permission);
            //更新权限
            shiroService.updatePermission();
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
    
    @CacheEvict(cacheNames="permissions", allEntries=true)
    @RequestMapping(value = "/delete")
    public String delete(String ids){
        try{
        	if(StringUtils.isNotBlank(ids)){
        		cyclePermissionService.deleteByKeys(ids.split(","));
        		//更新权限
        		shiroService.updatePermission();
        		return "success";
        	}
        	 return "fail";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }
    
}
