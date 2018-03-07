package io.igordonxiao.model;

import io.igordonxiao.pgdao.AbstractModel;

/**
 * User µÃÂ¿‡
 */
public class User extends AbstractModel {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{\"id\": "+this.getId()+", \"name\": "+this.getName()+", \"age\": "+this.getAge()+", \"createdDate\": "+this.getCreatedDate()+", \"updatedDate\": "+this.getUpdatedDate()+", \"delFlag\": "+this.getDelFlag()+"}";
    }
}
