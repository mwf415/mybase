package cn.onlov.evaluate.service.impl;


import cn.onlov.evaluate.core.dao.entities.CycleRoom;
import cn.onlov.evaluate.core.dao.interfaces.ICycleRoomService;
import cn.onlov.evaluate.pojo.bo.CycleRoomBo;
import cn.onlov.evaluate.service.CycleRoomService;
import cn.onlov.evaluate.util.MyStringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CycleRoomServiceImpl  implements CycleRoomService {
	@Autowired
	private ICycleRoomService iCycleRoomService;


	@Override
	public IPage<CycleRoom> selectByPage(CycleRoomBo bo) {

		IPage<CycleRoom> page = new Page<>();
		page.setCurrent(bo.getCurr()).setSize(bo.getPageSize());
		QueryWrapper<CycleRoom> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().like(MyStringUtils.isNotEmpty(bo.getValue()),CycleRoom::getValue,bo.getValue());
		IPage<CycleRoom> res = iCycleRoomService.page(page, new QueryWrapper<CycleRoom>().lambda().orderByDesc(CycleRoom::getId));
		return res;

}
	
	@Override
	@Cacheable(value = "rooms", key = "'all_room'")
	public List<CycleRoom> selectAll() {

		QueryWrapper<CycleRoom> queryWrapper = new QueryWrapper<>();
		List<CycleRoom> list = iCycleRoomService.list(queryWrapper);

		return list;
	}

	@Override
	public void deleteByKey(Integer key) {
		iCycleRoomService.removeById(key);

	}



   
}
