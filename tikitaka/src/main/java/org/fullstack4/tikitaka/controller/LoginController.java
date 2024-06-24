package org.fullstack4.tikitaka.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.fullstack4.tikitaka.dto.MemberDTO;
import org.fullstack4.tikitaka.service.LoginServiceIf;
import org.fullstack4.tikitaka.utils.CookieUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginServiceIf loginService;

    @GetMapping("/login")
    public String loginGET(HttpServletRequest request, Model model) {
        String loginId = CookieUtil.getCookieInfo(request, "loginId");
        String saveCheck = CookieUtil.getCookieInfo(request, "save_check");

        if (loginId != null) {
            model.addAttribute("loginId", loginId);
        }
        if (saveCheck != null) {
            model.addAttribute("saveCheck", saveCheck.equals("checked"));
        } else {
            model.addAttribute("saveCheck", false);
        }
        return "login";
    }

    @ResponseBody
    @PostMapping("/login")
    public String loginPOST(@RequestParam(name = "userId", defaultValue = "") String id,
                            @RequestParam(name="password", defaultValue = "") String pwd,
                            @RequestParam(name="saveId", defaultValue = "N") String save_id,
                            HttpSession session,
                            HttpServletResponse resp,
                            RedirectAttributes redirectAttributes) {

        if (!id.isEmpty() || !pwd.isEmpty()) {
            MemberDTO memberInfo = loginService.memberInfo(id);
            if (memberInfo != null) {
                if (!pwd.equals(memberInfo.getPwd())) {
                    redirectAttributes.addFlashAttribute("id", id);
                    return "no";
                } else {
                    if (save_id != null && save_id.equals("Y")) {
                        CookieUtil.setCookies(resp, "", "/", 60 * 60 * 24, "loginId", id);
                        CookieUtil.setCookies(resp, "", "/", 60 * 60 * 24, "save_check", "checked");
                    }

                    if (save_id != null && save_id.equals("N")) {
                        CookieUtil.setDeleteCookie(resp, "", "/", 0, "loginId", id);
                        CookieUtil.setDeleteCookie(resp, "", "/", 0, "save_check", "checked");
                    }
                    session.setAttribute("name", memberInfo.getName());
                    session.setAttribute("id", id);
                    session.setAttribute("memberIdx", memberInfo.getMemberIdx());
                    return "ok";
                }
            }
        } else {
            redirectAttributes.addFlashAttribute("id", id);
            return "no";
        }
        return "no";
    }

    @GetMapping("/logout")
    public String logoutGET(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}
