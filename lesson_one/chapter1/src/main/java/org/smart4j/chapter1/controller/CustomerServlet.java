package org.smart4j.chapter1.controller;

import org.smart4j.chapter1.model.Customer;
import org.smart4j.chapter1.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by yz on 2017/7/18.
 */
@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private CustomerService customerService;

    public void init() throws ServletException {
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customerList = customerService.getCustomerList();
        req.setAttribute("customerList", customerList);
        req.getRequestDispatcher("/view/customer.jsp").forward(req, resp);;
    }
}