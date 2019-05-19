package com.abuqool.sqool.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.abuqool.sqool.biz.BizException;
import com.abuqool.sqool.dao.mgmt.OperatorInfo;
import com.abuqool.sqool.integration.wx.WxService;
import com.abuqool.sqool.integration.wx.WxService.WxUserInfo;
import com.abuqool.sqool.service.mgmt.OperatorService;
import com.abuqool.sqool.vo.Operator;

@Component
public class AuthService {

    protected static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private WxService wxService;

    @Autowired
    private OperatorService operatorService;


    public Operator wechatLogon(String code) {
        WxUserInfo info = wxService.getWxUserInfo(code);
        if(info == null) {
            throw new BizException(BizException.Code.ADMIN_ERROR_ON_GET_WX_USERINFO);
        }
        Date now = new Date();
        OperatorInfo o = operatorService.findOperatorInfoByOpenId(info.getOpenId());
        if(o == null) {
            //1st time
            o = new OperatorInfo();
            o.setCreateTime(now);
            o.setOpenId(info.getOpenId());
            o.setUnionId(info.getUnionId());
            o.setNickname(info.getNickname());//default name
        }
        o.setAvatarUrl(info.getAvatarUrl());
        String sessionToken = genSessionToken(info.getAccessToken());
        o.setSessionToken(sessionToken);
        o.setUpdateTime(now);
        operatorService.saveOperatorInfo(o);
        return Operator.populate(o);
    }

    public boolean isAdmReqAllowed(String token, MgmtGraphQLOps operation) {
        logger.info("[admin_access] Token {"+token+"} is requesting operation {"+operation.name()+"}");
        OperatorInfo operator = operatorService.findOperatorInfoByToken(token);
        if (operator == null) {
            logger.info("Invalid token {"+token+"}");
            throw new BizException(BizException.Code.ADMIN_INVALID_ACCESS);
        }
        boolean hasAccess = (operation == MgmtGraphQLOps.QUERY_WHOAMI) || (operation == MgmtGraphQLOps.MUTATION_SAVE_OPERATOR) || operator.isActivated();
        if(!hasAccess) {
            logger.info("Operator {"+operator.getId()+"} has no access to operation {"+operation.name()+"}");
            throw new BizException(BizException.Code.ADMIN_INVALID_ACCESS);
        }
        return true;
    }

    private String genSessionToken(String sessionKey) {
        String sessionToken = md5(sessionKey+UUID.randomUUID().toString());
        return sessionToken;
    }

    private String md5(String str){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            //字符数组转换成字符串
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString().toUpperCase();
            // 16位的加密
             //return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error on MD5 with input {"+str+"}", e);
            return null;
        }
    }

    public boolean isAdmReqAllowed(String token, MgmtRestfulOps operation) {
        logger.info("[admin_access] Token {"+token+"} is requesting operation {"+operation.name()+"}");
        OperatorInfo operator = operatorService.findOperatorInfoByToken(token);
        if (operator == null) {
            logger.info("Invalid token {"+token+"}");
            throw new BizException(BizException.Code.ADMIN_INVALID_ACCESS);
        }
        boolean hasAccess = operator.isActivated();
        if(!hasAccess) {
            logger.info("Operator {"+operator.getId()+"} has no access to operation {"+operation.name()+"}");
            throw new BizException(BizException.Code.ADMIN_INVALID_ACCESS);
        }
        return true;
    }
}
