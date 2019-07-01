package cn.onlov.evaluate.pojo.bo;

import cn.onlov.evaluate.core.dao.entities.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserBo extends User implements Serializable {

    private static final long serialVersionUID = 318399374157993931L;
    private int curr = 0; //当前页数
    private int pageSize = 10;// 每页几行



}
