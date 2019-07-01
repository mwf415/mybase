package cn.onlov.evaluate.util.module.vo;

import cn.onlov.evaluate.util.module.annotation.ExcelField;

public class UserVO {

    @ExcelField(name = "id", column = "A", hlink = "http://www.baidu.com?id=id")
    private Long id;

    @ExcelField(name = "名字", column = "B")
    private String name;

    @ExcelField(name = "手机号", column = "c", prompt = "你好！")
    private String phone;

    @ExcelField(name = "性别", column = "d", combo = { "男", "女" })
    private String sex;

    @ExcelField(name = "民族", column = "e")
    private String nation;

    @ExcelField(name = "密码", column = "f", isExport = false)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}