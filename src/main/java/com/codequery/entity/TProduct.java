package com.codequery.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * (TProduct)实体类
 *
 * @author makejava
 * @since 2020-03-13 15:29:09
 */
@Data
public class TProduct implements Serializable {
    private static final long serialVersionUID = -99714310939263378L;

    private Integer id;
    /**
     * 商品码
     */
    private String barcode;
    /**
     * 商品名称
     */
    private String pdName;
    /**
     * 生产日期
     */
    private String pdStarttime;
    /**
     * 生产企业
     */
    private String pdCompany;
    /**
     * 有效期
     */
    private String pdUsedata;
    /**
     * 有效期至
     */
    private String pdEndtime;
    /**
     * 许可证号
     */
    private String pdLicencekey;
    /**
     * 上次查询时间
     */
    private String lastTime;
    /**
     * 上次查询地址
     */
    private String lastAddr;

    /**
     * 查询次数
     */
    private Integer count;

}
