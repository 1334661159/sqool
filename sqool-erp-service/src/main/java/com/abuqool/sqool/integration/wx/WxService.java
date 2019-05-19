package com.abuqool.sqool.integration.wx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Component
public class WxService {

    protected static final Logger logger = LoggerFactory.getLogger(WxService.class);

    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    @Value("${wx-open.app-id}")
    private String openAppId;

    @Value("${wx-open.secret}")
    private String openSecret;

    private String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId
                + "&secret=" + secret;
        String response = get(url);
        JSONObject object = JSON.parseObject(response);
        String accessToken = object.getString("access_token");
        String errCode = object.getString("errcode");
        if(StringUtils.isEmpty(accessToken) || !StringUtils.isEmpty(errCode)) {
            logger.error("Error on getting WX access_token with errcode "+errCode);
            logger.error(response);
            return null;
        }
        logger.info("accessToken is "+accessToken);
        return accessToken;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public byte[] getQr4Scene(String sceneStr) {
        RestTemplate rest = new RestTemplate();
        byte[] result = null;
        try {
            String accessToken = getAccessToken();
            if(!StringUtils.isEmpty(accessToken)) {
                String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;
                Map<String,Object> param = new HashMap<>();
                param.put("scene", sceneStr);
                param.put("page", "pages/index/index");
                param.put("width", 430);
                param.put("auto_color", false);
                Map<String,Object> line_color = new HashMap<>();
                line_color.put("r", 0);
                line_color.put("g", 0);
                line_color.put("b", 0);
                param.put("line_color", line_color);
                logger.info("调用生成微信URL接口传参:" + param);
                MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
                HttpEntity requestEntity = new HttpEntity(param, headers);
                ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
                logger.info("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());
                result = entity.getBody();
            }
        } catch (Exception e) {
            logger.error("调用小程序生成微信永久小程序码URL接口异常",e);
        }
        return result;
    }

    private String get(String url) {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();
        try {
            URL u = new URL(url);
            URLConnection connection = u.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            logger.error("Error on requesting URL {"+url+"}", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                logger.error("Error on requesting URL {"+url+"}", e);
            }
        }
        return sb.toString();
    }

    public static class WxUserInfo{
        private String accessToken;
        private String openId;
        private String refreshToken;
        private int expiresIn;
        private String unionId;
        private String nickname;
        private String avatarUrl;

        public String getAccessToken() {
            return accessToken;
        }
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
        public String getOpenId() {
            return openId;
        }
        public void setOpenId(String openId) {
            this.openId = openId;
        }
        public String getRefreshToken() {
            return refreshToken;
        }
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
        public String getUnionId() {
            return unionId;
        }
        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }
        public int getExpiresIn() {
            return expiresIn;
        }
        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }
        public String getNickname() {
            return nickname;
        }
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public String getAvatarUrl() {
            return avatarUrl;
        }
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
    }
    public WxUserInfo getWxUserInfo(String code) {
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + openAppId + "&secret=" + openSecret + "&code="
                    + code + "&grant_type=authorization_code";
            String authinfo = get(url);
            JSONObject object = JSON.parseObject(authinfo.trim());
            String accessToken = object.getString("access_token");
            String openId = object.getString("openid");
            String refreshToken = object.getString("refresh_token");
            int expiresIn = object.getLong("expires_in").intValue();
            String unionId = object.getString("unionid");

            url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
            String userinfo = get(url);
            object = JSON.parseObject(userinfo.trim());

//            String country = object.getString("country");
            String nickname = object.getString("nickname");
//            String province = object.getString("province");
//            String city = object.getString("city");
//            String sex = object.getString("sex");
            String headimgurl = object.getString("headimgurl");
//            String language = object.getString("language");

            WxUserInfo info = new WxUserInfo();
            info.setAccessToken(accessToken);
            info.setExpiresIn(expiresIn);
            info.setOpenId(openId);
            info.setRefreshToken(refreshToken);
            info.setUnionId(unionId);
            info.setNickname(nickname);
            info.setAvatarUrl(headimgurl);
            return info;
        } catch (Exception e) {
            logger.error("Error on getting WX user info with code {"+code+"}", e);
        }
        return null;
    }
}
