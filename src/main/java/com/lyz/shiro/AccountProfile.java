package com.lyz.shiro;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {
    private Integer id;

    private String username;

    private String avatar;

    private String email;

    private Integer age;

    private Integer sex;

}
