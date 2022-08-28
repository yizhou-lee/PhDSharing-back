package com.lyz.entity;

import java.sql.Date;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyz
 * @since 2022-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String authors;

    private String year;

    private String tags;

    @TableField("addedDate")
    private Date addedDate;

    @TableField("fileId")
    private Integer fileId;

    @TableField("urlPath")
    private String urlPath;

    @TableField("userId")
    private Integer userId;


}
