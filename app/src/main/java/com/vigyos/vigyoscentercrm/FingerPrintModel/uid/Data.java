package com.vigyos.vigyoscentercrm.FingerPrintModel.uid;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Data {

    public Data() { }

    @Attribute(name = "type", required = false)
    public String type;

    @Text(required = true)
    public String value;

}
