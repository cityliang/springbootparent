package com.city.sprinbboot.database2code.com.xjs.common.token;

import com.city.sprinbboot.database2code.com.xjs.common.util.Identities;
import com.city.sprinbboot.database2code.com.xjs.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TokenUtils {


    /**
     * 保存token值的默认命名空间
     */
    public static final String TOKEN_NAMESPACE = "tokens";

    /**
     * 持有token名称的字段名
     */
    public static final String TOKEN_NAME_FIELD = "token";

    /**
     * 使用UUID字串作为token名字保存token
     *
     * @param request
     * @return token
     */
    public static String setToken(HttpServletRequest request) {
        return setToken(request, generateGUID());
    }

    /**
     * 使用给定的字串作为token名字保存token
     *
     * @param request
     * @param tokenName
     * @return token
     */
    private static String setToken(HttpServletRequest request, String tokenName) {
        String token = tokenName;
        setCacheToken(request, tokenName, token);
        return token;
    }

    /**
     * 保存一个给定名字和值的token
     *
     * @param request
     * @param tokenName
     * @param token
     */
    private static void setCacheToken(HttpServletRequest request,
                                      String tokenName, String token) {
        try {
            String tokenName0 = buildTokenCacheAttributeName(tokenName);
            RedisUtil.set(tokenName0, token, 600);
            request.setAttribute(TOKEN_NAME_FIELD, tokenName);
            request.setAttribute(tokenName, token);
        } catch (IllegalStateException e) {
            String msg = "Error creating HttpSession due response is commited to client. You can use the CreateSessionInterceptor or create the HttpSession from your action before the result is rendered to the client: "
                    + e.getMessage();
            log.error(msg, e);
            throw new IllegalArgumentException(msg);
        }

    }

    /**
     * 构建一个基于token名字的带有命名空间为前缀的token名字
     *
     * @param tokenName
     * @return the name space prefixed session token name
     */
    public static String buildTokenCacheAttributeName(String tokenName) {
        return TOKEN_NAMESPACE + ":" + tokenName;
        //return tokenName;
    }

    /**
     * 从请求域中获取给定token名字的token值
     *
     * @param tokenName
     * @return the token String or null, if the token could not be found
     */
    public static String getToken(HttpServletRequest request, String tokenName) {
        if (tokenName == null) {
            return null;
        }

        Map<?, ?> params = request.getParameterMap();
        String[] tokens = (String[]) params.get(tokenName);
        String token;
        if ((tokens == null) || (tokens.length < 1)) {
            log.warn("Could not find token mapped to token name " + tokenName);
            return null;
        }
        token = tokens[0];
        return token;

    }

    /**
     * 从请求参数中获取token名字
     *
     * @return the token name found in the params, or null if it could not be
     * found
     */
    public static String getTokenName(HttpServletRequest request) {
        Map<?, ?> params = request.getParameterMap();
        if (!params.containsKey(TOKEN_NAMESPACE)) {
            log.warn("Could not find token name in params.");
            return null;
        }

        String[] tokenNames = (String[]) params.get(TOKEN_NAMESPACE);
        String tokenName;
        if ((tokenNames == null) || (tokenNames.length < 1)) {
            log.warn("Got a null or empty token name.");
            return null;
        }

        tokenName = tokenNames[0];
        return tokenName;

    }

    /**
     * 验证当前请求参数中的token是否合法，如果合法的token出现就会删除它，它不会再次成功合法的token
     *
     * @return 验证结果
     */
    public static boolean validToken(HttpServletRequest request) {

        String tokenName = getTokenName(request);
        if (tokenName == null) {
            log.warn("no token name found -> Invalid token ");
            return false;
        }

        String tokenCacheName = buildTokenCacheAttributeName(tokenName);
        String cacheToken = (String) RedisUtil.get(tokenCacheName);
        if (!tokenName.equals(cacheToken)) {
            log.warn("invalid.token Form token " + tokenName
                    + " does not match the session token "
                    + cacheToken + ".");
            return false;

        }
        request.setAttribute("tempTokenKey", tokenCacheName);
        request.setAttribute("tempTokenValue", cacheToken);
        RedisUtil.remove(tokenCacheName);
        return true;
    }

    /**
     * 验证当前请求参数中的token是否合法，如果合法的token出现就会删除它，它不会再次成功合法的token
     *
     * @return 验证结果
     */
    public static String validToken1(HttpServletRequest request) {

        String tokenName = getTokenName(request);
        if (tokenName == null) {
            log.warn("no token name found -> Invalid token ");
            return "0";
        }

        String tokenCacheName = buildTokenCacheAttributeName(tokenName);
        String cacheToken = (String) RedisUtil.get(tokenCacheName);
        if (!tokenName.equals(cacheToken)) {
            log.warn("invalid.token Form token " + tokenName
                    + " does not match the session token "
                    + cacheToken + ".");
            return "1";

        }
        request.setAttribute("tempTokenKey", tokenCacheName);
        request.setAttribute("tempTokenValue", cacheToken);
        RedisUtil.remove(tokenCacheName);
        return "2";
    }

    public static String generateGUID() {
        return Identities.uuid();
    }

}