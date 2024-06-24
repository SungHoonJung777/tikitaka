package org.fullstack4.tikitaka.utils;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void setCookies(HttpServletResponse resp, String domain, String path, int expire, String name, String val) {
        Cookie cookieLogin = new Cookie(name, val);
        if (domain != null && !domain.isEmpty()) {
            cookieLogin.setDomain(domain);
        }
        cookieLogin.setPath((path != null && !path.isEmpty()) ? path : "/");
        cookieLogin.setMaxAge(expire);
        resp.addCookie(cookieLogin);
    }
    public static String getCookieInfo(HttpServletRequest req, String name) {
        String result = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (name.equals(c.getName())) {
                    result = c.getValue();

                    break;
                }
            }
        }
        return result;
    }
    //setDeleteCookie(response, "", "/", 0, 쿠키이름, ""); 이렇게 호출하면 삭제 완료!
    public static void setDeleteCookie(HttpServletResponse resp, String domain, String path, int expire, String name, String val) {
        setCookies(resp, domain, path, expire, name, val);
    }
}
