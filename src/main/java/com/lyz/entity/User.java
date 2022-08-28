package com.lyz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyz
 * @since 2021-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "user name should be filled")
    @TableField("username")
    private String username;

    private String avatar;

    @NotBlank(message = "email should be filled")
    @Email(message = "email format is incorrect")
    private String email;

    @NotBlank(message = "password should be filled")
    private String password;

    @TableField("trueName")
    private String trueName;


    private String sex;

    private String role;


}
