/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Domain object containing data source information used to retrieve text.
 */
@XmlType(name = "datasource")
@XmlRootElement
public class DataSource {
    String name;
    
    public DataSource() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DataSource{" + "name=" + name + '}';
    }

}
