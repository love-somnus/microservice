package com.somnus.microservice.xxljob.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author kevin.liu
 * @title: JobInfoPageResult
 * @projectName neshpub
 * @description: TODO
 * @date 2022/8/26 16:55
 */
@Data
public class JobInfoPageResult implements Serializable {
    private static final long serialVersionUID = 1316450425048690593L;

    private int recordsFiltered;

    private List<JobInfoPageItem> data;

    private int recordsTotal;
}