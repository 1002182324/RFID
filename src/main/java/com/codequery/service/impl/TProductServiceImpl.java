package com.codequery.service.impl;

import com.codequery.dao.TProductDao;
import com.codequery.entity.TProduct;
import com.codequery.service.TProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TProduct)表服务实现类
 *
 * @author makejava
 * @since 2020-03-13 11:26:30
 */

@Service("tProductService")
public class TProductServiceImpl implements TProductService {
    @Resource
    private TProductDao tProductDao;

    /**
     * 通过code查询单条数据
     *
     * @param Barcode 商品码
     * @return 实例对象
     */
    @Override
    public TProduct queryByBarcode(String Barcode) {
        return this.tProductDao.queryByBarcode(Barcode);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<TProduct> queryAllByLimit(int offset, int limit) {
        return this.tProductDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param tProduct 实例对象
     * @return 实例对象
     */
    @Override
    public TProduct insert(TProduct tProduct) {
        this.tProductDao.insert(tProduct);
        return tProduct;
    }

    /**
     * 修改数据
     *
     * @param tProduct 实例对象
     * @return 实例对象
     */
    @Override
    public TProduct update(TProduct tProduct) {
        this.tProductDao.update(tProduct);
        return this.queryByBarcode(tProduct.getBarcode());
    }

    /**
     * 通过主键删除数据
     *
     * @param Barcode 商品码
     * @return 是否成功
     */
    @Override
    public boolean deleteByBarcode(String Barcode) {
        return this.tProductDao.deleteByBarcode(Barcode) > 0;
    }
}