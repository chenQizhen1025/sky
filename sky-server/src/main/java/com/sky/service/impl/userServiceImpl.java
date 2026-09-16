package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class userServiceImpl implements UserService {
    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //使用httpClient调用微信接口服务，获取openId
        String openid = getOpenid(userLoginDTO.getCode());
        //判断是否成功获取，为空抛出异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断是否为新用户
        User user = userMapper.getByOpenid(openid);
        //新用户自动完成注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //返回用户对象
        return user;
    }

    /**
     * 根据用户code获取openid
     * @param code
     * @return
     */
    private String getOpenid(String code){
        Map<String, String> params = new HashMap<String, String>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, params);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
