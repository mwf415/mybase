package cn.onlov.evaluate.service;

import cn.onlov.evaluate.core.dao.entities.CycleRoom;
import cn.onlov.evaluate.pojo.bo.CycleRoomBo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * Created by yangqj on 2017/4/25.
 */
public interface CycleRoomService {
	
	IPage<CycleRoom> selectByPage(CycleRoomBo base);
	List<CycleRoom> selectAll();
	void deleteByKey(Integer key);
}
