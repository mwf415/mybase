package cn.onlov.evaluate.service.impl;


import cn.onlov.evaluate.core.dao.entities.CycleBase;
import cn.onlov.evaluate.core.dao.interfaces.ICycleBaseService;
import cn.onlov.evaluate.pojo.bo.CycleBaseBo;
import cn.onlov.evaluate.service.CycleBaseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CycleBaseServiceImpl  implements CycleBaseService {
    @Resource
    private ICycleBaseService iCycleBaseService;




    @Override
    public IPage<CycleBase> selectByPage(CycleBaseBo bo) {
        IPage<CycleBase> page = new Page<>();
        page.setCurrent(bo.getCurr()).setSize(bo.getPageSize());

        IPage<CycleBase> res = iCycleBaseService.page(page, new QueryWrapper<CycleBase>().lambda().orderByDesc(CycleBase::getId));
        return res;
    }

    @Override
    @Cacheable(value = "bases", key = "'all_base'")
    public List<CycleBase> selectAll() {
        List<CycleBase> res = iCycleBaseService.list( new QueryWrapper<CycleBase>().lambda().orderByDesc(CycleBase::getId));
        return res;
    }

    @Override
    public void deleteByKey(Integer key) {
        iCycleBaseService.remove( new QueryWrapper<CycleBase>().lambda().eq(CycleBase::getId,key));
    }

}
