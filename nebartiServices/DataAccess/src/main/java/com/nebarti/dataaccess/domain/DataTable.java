/*
 * Nebarti
 * Copyright © 2013 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Used to construct an object whose data is converted to JSON for the 
 * jQuery DataTables
 * 
 */
@XmlRootElement
public class DataTable {
    String sEcho;
    Integer iTotalRecords;
    Integer iTotalDisplayRecords;
    
    List<DataTableRecord> aaData = new ArrayList<DataTableRecord>();

    public DataTable() {};

    public Integer getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(Integer iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }
    
    public List<DataTableRecord> getaaData() {
        return aaData;
    }
    
    public void setaaData(List<DataTableRecord> recordList) {
        aaData = recordList;
    }

    public Integer getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(Integer iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }
    
}
