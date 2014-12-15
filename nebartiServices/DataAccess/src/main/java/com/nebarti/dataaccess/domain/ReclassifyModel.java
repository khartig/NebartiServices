/*
 * Nebarti
 * Copyright © 2012 All rights reserved.
 */
package com.nebarti.dataaccess.domain;

import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reclassifymodel")
@XmlRootElement
public class ReclassifyModel {
    private Boolean reclassify = false;
    public static final Logger logger = Logger.getLogger(ReclassifyModel.class.getName());

    public ReclassifyModel() {}; // needed for JAXB

    public Boolean getReclassify() {
        return reclassify;
    }

    public void setReclassify(Boolean reclassify) {
        this.reclassify = reclassify;
    }
    
}
