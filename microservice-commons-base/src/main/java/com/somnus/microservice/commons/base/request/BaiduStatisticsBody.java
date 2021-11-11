package com.somnus.microservice.commons.base.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Kevin
 * @packageName com.somnus.microservice.commons.base.request
 * @title: BaiduStatisticsBody
 * @description: TODO
 * @date 2019/12/13 15:46
 */
@Data
@ApiModel(value = "必填参数")
public class BaiduStatisticsBody {

    /**
     * 站点id
     */
    @ApiModelProperty(value = "站点id", required = true, example = "13037963")
    private String site_id;

    /**
     * 查询起始时间
     */
    @ApiModelProperty(value = "查询起始时间", required = true, example = "20191201")
    private String start_date;

    /**
     * 查询结束时间
     */
    @ApiModelProperty(value = "查询结束时间", required = true, example = "20191231")
    private String end_date;

    /**
     * 通常对应要查询的报告
     */
    @ApiModelProperty(value = "通常对应要查询的报告", required = true, example = "overview/getTimeTrendRpt")
    private String method;

    /**
     * 自定义指标选择，多个指标用逗号分隔
     */
    @ApiModelProperty(value = "自定义指标选择，多个指标用逗号分隔", required = true, example = "pv_count,visitor_count")
    private String metrics;
}
