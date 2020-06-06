package com.codequery.controller;

import com.codequery.entity.TProduct;
import com.codequery.service.TProductService;
import com.codequery.utils.DateUtil;
import com.codequery.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * (TProduct)表控制层
 *
 * @author makejava
 * @since 2020-03-11 20:15:23
 */
@Controller
@RequestMapping("/")
@Slf4j
public class TProductController {
    // Log log = LogFactory.getLog(this.getClass());
    /**
     * 服务对象
     */
    @Autowired
    private TProductService tProductService;


    /**
     * 通过商品码查询单条数据
     *
     * @param code 商品码
     * @return 单条数据
     */
    @GetMapping("check")
    public String selectOne(HttpServletRequest request, String code, Model model) {
        TProduct user = tProductService.queryByBarcode(code);
        model.addAttribute("user", user);
        String ipAddr = IpUtil.getIpAddr(request);
        TProduct newuser = new TProduct();
        BeanUtils.copyProperties(user, newuser);
        newuser.setLastAddr(ipAddr);
        newuser.setLastTime(DateUtil.getTime());
        newuser.setCount(user.getCount() + 1);
        log.info("" + newuser);
        tProductService.update(newuser);
        return "index";
    }

}