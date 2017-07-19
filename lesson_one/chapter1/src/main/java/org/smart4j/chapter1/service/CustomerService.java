package org.smart4j.chapter1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.chapter1.helper.DatabaseHelper;
import org.smart4j.chapter1.model.Customer;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Created by yz on 2017/7/16.
 */
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    public List<Customer> getCustomerList(){
        String sql = "select * from customer";
        return DatabaseHelper.queryEntryList(sql, Customer.class);
    }

    public Customer getCustomer(long id){
        String sql = "select * from customer where id=" + id;
        return DatabaseHelper.queryEntry(sql, Customer.class);
    }

    public boolean createCustomer(Map<String, Object> fieldMap){
        return DatabaseHelper.insertEntry(Customer.class, fieldMap);
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap){
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    public boolean deleteCustomer(long id){
        return DatabaseHelper.deleteEntry(Customer.class, id);
    }
}
