package cn.onlov.evaluate.service.impl;

import cn.onlov.evaluate.core.dao.entities.User;
import cn.onlov.evaluate.core.dao.interfaces.IUserService;
import cn.onlov.evaluate.pojo.bo.UserBo;
import cn.onlov.evaluate.service.UserService;
import cn.onlov.evaluate.util.MyStringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    @Resource
    private IUserService iUserService;


    @Override
    public IPage<User> getBusinessPageUser(UserBo bo) {
        IPage<User> page = new Page<>();
        page.setCurrent(bo.getCurr()).setSize(bo.getPageSize());

        System.out.println("getRealName = [" + MyStringUtils.isNotEmpty(bo.getRealName()));
        System.out.println("getBaseName = [" +MyStringUtils.isNotEmpty(bo.getBaseName()));
        System.out.println("getRoomName = [" +MyStringUtils.isNotEmpty(bo.getRoomName()));
        System.out.println("getGrade = [" +MyStringUtils.isNotEmpty(bo.getGrade()));

        IPage<User> res = iUserService.page(page, new QueryWrapper<User>().lambda()
                .like(MyStringUtils.isNotEmpty(bo.getRealName()), User :: getRealName , bo.getRealName())
                .eq(MyStringUtils.isNotEmpty(bo.getBaseName()),User :: getBaseName, bo.getBaseName())
                .eq(MyStringUtils.isNotEmpty(bo.getRoomName()),User::getRoomName,bo.getRoomName())
//                .eq(MyStringUtils.isNotEmpty(bo.getGrade()),User::getGrade, bo.getGrade())
//                .eq(MyStringUtils.isNotEmpty(bo.getIdentityId()),User::getIdentityId,bo.getIdentityId())
//                .eq(MyStringUtils.isNotEmpty(bo.getIsAt()),User::getIsAt,bo.getIsAt())
                .orderByDesc(User::getId));
        return res;
    }





    @Override
    public User selectByLoginName(String loginName) {
        User user = (User)iUserService.getObj(new QueryWrapper<User>().lambda().eq(User::getLoginName,loginName));

        return user;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor={Exception.class})
    public void delUser(Integer userid) {

        iUserService.removeById(userid);
    }

    @Override
    public List<User> selectByLoginNames(String[] loginNames) {
        List<User> list = iUserService.list(new QueryWrapper<User>().lambda().in(User::getLoginName, loginNames));

        return list;
    }






}
