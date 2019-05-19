package com.abuqool.sqool.biz;

public class BizException extends RuntimeException{

    private static final long serialVersionUID = -7905327443556825171L;

    public BizException(String code) {
        super(code);
    }

    public static class Code{
        public static final String ADMIN_INVALID_ACCESS="2001";
        public static final String ADMIN_INVALID_PARAM="2002";//access invalid obj
        public static final String ADMIN_MISSING_REQUIRED_PARAM="2003";
        public static final String ADMIN_OBJECT_DELETED="2004";
        public static final String ADMIN_INCORRECT_PARAM="2005";//violate biz logic
        public static final String ADMIN_ERROR_ON_GET_WX_USERINFO = "2006";
    }
}
