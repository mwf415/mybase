package cn.onlov.evaluate.service;

import cn.onlov.evaluate.core.dao.entities.User;
import cn.onlov.evaluate.pojo.bo.UserBo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface UserService {

	IPage<User> getBusinessPageUser(UserBo bo) ;
	
	User selectByLoginName(String loginName);
	
	void delUser(Integer userid);
	
	List<User> selectByLoginNames(String[] loginNames);

}
