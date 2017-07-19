package org.smart4j.chapter1.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yz on 2017/7/16.
 */

public class Customer {
    @Getter
    @Setter
    private long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String contact;
    @Getter
    @Setter
    private String telephone;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String remark;

}
