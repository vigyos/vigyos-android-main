package com.vigyos.vigyoscentercrm.FingerPrintModel.uid;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Skey {

    public Skey() { }

    @Attribute(name = "ci", required = false)
    public String ci;

    @Text(required = true)
    public String value;
}