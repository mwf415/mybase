package cn.onlov.evaluate.service.impl;

import cn.onlov.evaluate.core.dao.entities.CycleRole;
import cn.onlov.evaluate.core.dao.interfaces.IRoleService;
import cn.onlov.evaluate.pojo.bo.CycleRoleBo;
import cn.onlov.evaluate.service.CycleRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CycleRoleServiceImpl  implements CycleRoleService {
    @Autowired
    private IRoleService iRoleService;

    @Override
    public List<CycleRole> queryCycleRoleListWithSelected(Integer uid) {
        IPage<CycleRole> page = new Page<>();
//        page.setCurrent(bo.getCurr()).setSize(bo.getPageSize());
//        boolean a = MyStringUtils.isNotEmpty(bo.getBaseName());


        return null;
    }

    @Override
    public IPage<CycleRole> selectByPage(CycleRoleBo bo) {
        QueryWrapper<CycleRole> queryWrapper =  new QueryWrapper<>() ;

        IPage<CycleRole> page = new Page<>();
        page.setCurrent(bo.getCurr()).setSize(bo.getPageSize());

        IPage<CycleRole> res = iRoleService.page(page, new QueryWrapper<CycleRole>().lambda());

        return res;
    }

    @Override
    public void delCycleRole(Integer roleid) {
        QueryWrapper<CycleRole> queryWrapper =  new QueryWrapper<>();
        iRoleService.remove(queryWrapper.lambda().eq(CycleRole::getRoleId ,roleid));
    }

    @Override
    public List<CycleRole> queryCycleRoleListByUserId(Integer userId) {
        QueryWrapper<CycleRole> queryWrapper =  new QueryWrapper<>();
        List<CycleRole> cycleRoles = iRoleService.queryRoleListByUserId(userId);
        return cycleRoles;
    }

}
