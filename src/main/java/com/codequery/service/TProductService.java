package com.codequery.service;

import com.codequery.entity.TProduct;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (TProduct)表服务接口
 *
 * @author makejava
 * @since 2020-03-13 11:26:30
 */
@Service
public interface TProductService {

    /**
     * code
     *
     * @param Barcode 商品码
     * @return 实例对象
     */
    TProduct queryByBarcode(String Barcode);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TProduct> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tProduct 实例对象
     * @return 实例对象
     */
    TProduct insert(TProduct tProduct);

    /**
     * 修改数据
     *
     * @param tProduct 实例对象
     * @return 实例对象
     */
    TProduct update(TProduct tProduct);

    /**
     * 通过主键删除数据
     *
     * @param Barcode 商品码
     * @return 是否成功
     */
    boolean deleteByBarcode(String Barcode);

}