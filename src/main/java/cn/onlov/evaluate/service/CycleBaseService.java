package cn.onlov.evaluate.service;

import cn.onlov.evaluate.core.dao.entities.CycleBase;
import cn.onlov.evaluate.pojo.bo.CycleBaseBo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * Created by yangqj on 2017/4/25.
 */
public interface CycleBaseService{
	IPage<CycleBase> selectByPage(CycleBaseBo base);
	List<CycleBase> selectAll();
	void deleteByKey(Integer key);
	//根据基地获取轮转科室
}
