package com.codequery.dao;

import com.codequery.entity.TProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TProduct)表数据库访问层
 *
 * @author makejava
 * @since 2020-03-13 11:26:29
 */
public interface TProductDao {

    /**
     * 通过code查询单条数据
     *
     * @param Barcode 商品码
     * @return 实例对象
     */
    TProduct queryByBarcode(String Barcode);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TProduct> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tProduct 实例对象
     * @return 对象列表
     */
    List<TProduct> queryAll(TProduct tProduct);

    /**
     * 新增数据
     *
     * @param tProduct 实例对象
     * @return 影响行数
     */
    int insert(TProduct tProduct);

    /**
     * 修改数据
     *
     * @param tProduct 实例对象
     * @return 影响行数
     */
    int update(TProduct tProduct);

    /**
     * 通过主键删除数据
     *
     * @param Barcode 主键
     * @return 影响行数
     */
    int deleteByBarcode(String Barcode);

}