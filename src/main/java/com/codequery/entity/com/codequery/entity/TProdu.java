package com.codequery.entity;

import lombok.Data;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hasee
 * @version $Id: TProdu.java, v 0.1 2020-04-15 13:07:16 hasee Exp $$
 */
class TProdu {

    /**
     *
     */
    @Getter
    @Setter
    private long serialVersionUID;

    /**
     *
     */
    @Getter
    @Setter
    private Integer id;

    /**
     *
     */
    @Getter
    @Setter
    private String barcode;

    /**
     *
     */
    @Getter
    @Setter
    private String pdName;

    /**
     *
     */
    @Getter
    @Setter
    private String pdStarttime;

    /**
     *
     */
    @Getter
    @Setter
    private String pdCompany;

    /**
     *
     */
    @Getter
    @Setter
    private String pdUsedata;

    /**
     *
     */
    @Getter
    @Setter
    private String pdEndtime;

    /**
     *
     */
    @Getter
    @Setter
    private String pdLicencekey;

    /**
     *
     */
    @Getter
    @Setter
    private String lastTime;

    /**
     *
     */
    @Getter
    @Setter
    private String lastAddr;

    /**
     *
     */
    @Getter
    @Setter
    private Integer count;


}
