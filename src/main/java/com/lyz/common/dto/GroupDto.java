package com.lyz.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GroupDto implements Serializable {
    private String groupName;
    private List<Integer> memberList;
}
