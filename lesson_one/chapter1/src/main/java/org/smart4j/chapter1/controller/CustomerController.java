package org.smart4j.chapter1.controller;

import or.smert4j.framework.annotation.Action;
import or.smert4j.framework.annotation.Aspect;
import or.smert4j.framework.annotation.Controller;
import or.smert4j.framework.annotation.Inject;
import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.service.CustomerService;
import org.smart4j.charpter1.aspect.TimeCostAspect;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;

import java.util.List;

/**
 * Created by yz on 2017/7/18.
 */
@Controller
public class CustomerController {

    @Inject
    private CustomerService customerService;

    /**
     * 进入客户列表界面
     * @return
     */
    @Action("get:/customer")
    public View index(){
        List<Customer> customerList = customerService.getCustomerList();
        return new View("customer.jsp").addModel("customerList", customerList);
    }

    /**
     * 显示客户基本信息
     * @param param
     * @return
     */
    @Action("get:/customer_show")
    public View show(Param param) {
        long id = param.getLong("id");
        Customer customer = customerService.getCustomer(id);
        return new View("customer_show.jsp").addModel("customer", customer);
    }

    /**
     * 进入创建客户界面
     */
    @Action("get:/customer_create")
    public View create(Param param){
        return new View("customer_create.jsp");
    }
}
