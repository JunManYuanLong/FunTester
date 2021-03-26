package com.funtester.java;

import com.funtester.frame.SourceCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Te extends SourceCode {

    private static final Logger logger = LoggerFactory.getLogger(Te.class);

    public static void main(String[] args) {
        output(3);
        logger.info("32423");
        logger.warn("32423");
        logger.error("32423");
        output(getJson("3243=432","32442424=32423"));
    }

}
