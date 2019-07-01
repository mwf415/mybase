package cn.onlov.evaluate.util.module.vo;


import cn.onlov.evaluate.util.module.annotation.CSVField;

/**
 * @Description:  
 * @author zhangxiaowei
 * @date 2018年8月15日
 */
public class TestVO {

    @CSVField(name="姓名")
    private String name;
    @CSVField(name="生日")
    private String birthDay;
    @CSVField(name="年龄")
    private int age;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBirthDay() {
        return birthDay;
    }
    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
    
}
