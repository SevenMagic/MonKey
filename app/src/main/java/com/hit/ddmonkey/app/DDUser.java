package com.hit.ddmonkey.app;

import cn.bmob.v3.BmobUser;

/**
 * Created by 道谊戎 on 2016/3/2.
 */
public class DDUser extends BmobUser {

    private Boolean sex;
    private Integer age;
    private Boolean ishit;

    public boolean getSex() {
        return this.sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    public boolean ishit() {
        return this.ishit;
    }


}

