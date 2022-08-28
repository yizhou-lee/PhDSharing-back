package com.lyz.common.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    @NotBlank(message = "user name should be filled")
    @TableField("username")
    private String username;

    @NotBlank(message = "password should be filled")
    private String password;

}
