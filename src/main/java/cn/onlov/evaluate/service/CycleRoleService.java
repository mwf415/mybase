package cn.onlov.evaluate.service;

import cn.onlov.evaluate.core.dao.entities.CycleRole;
import cn.onlov.evaluate.pojo.bo.CycleRoleBo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface CycleRoleService  {

    public List<CycleRole> queryCycleRoleListWithSelected(Integer uid);

    IPage<CycleRole> selectByPage(CycleRoleBo CycleRole);

    /**
     * 删除角色 同时删除角色资源表中的数据
     * @param CycleRoleid
     */
    public void delCycleRole(Integer CycleRoleid);

    /**
     * @Description:获取用户角色
     * @fieldName:
     * @return: List<CycleRole>
     */
    public List<CycleRole> queryCycleRoleListByUserId(Integer userId);

}
