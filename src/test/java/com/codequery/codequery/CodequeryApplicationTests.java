package com.codequery.codequery;


import com.codequery.entity.TProduct;
import com.codequery.service.TProductService;
import com.codequery.utils.DateUtil;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
class CodequeryApplicationTests {
    @Autowired
    private DataSource dataSource;
    @Resource
    private TProductService tProductService;


    @Test
    void contextLoads() throws SQLException {
        System.out.println("--------------------------------------");
        System.out.println("type:"+dataSource.getClass());
        System.out.println("date:"+ DateUtil.getTime());
        System.out.println("code:"+tProductService.queryByBarcode("123456"));

        System.out.println("--------------------------------------");
    }
    Log log = LogFactory.getLog(this.getClass());
    @Test
    void uodata(){

        TProduct user = tProductService.queryByBarcode("123456");
        TProduct newuser = new TProduct();
        BeanUtils.copyProperties(user,newuser);
        newuser.setLastTime(DateUtil.getTime());
        newuser.setCount(user.getCount()+1);
        newuser.setLastAddr("127.0.0.1");
        log.info(""+newuser);
        tProductService.update(newuser);
log.info(DateUtil.getTime());

    }

    @Test
    void inserttest(){
        TProduct t = new TProduct();
        t.setBarcode("111111");
        t.setCount(1);
        t.setLastAddr("111111");
        t.setPdCompany("fdssgfd");
        TProduct insert = null;
        try {
            insert = tProductService.insert(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(insert);

    }
}
