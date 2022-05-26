package com.example.msa.frontend.app.web;

import javax.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.msa.frontend.app.web.security.CustomUserDetails;

@Controller
public class FrontendController {

  @GetMapping("/error")
  public String error() {
    return "error";
  }

  @GetMapping(value = "/login")
  public String login() {
    return "login";
  }

  @GetMapping(value = "/timeout")
  public String timeout() {
    return "timeout";
  }

  @GetMapping(value = "/portal")
  public String portal(
      @AuthenticationPrincipal CustomUserDetails customUserDetails,
      Model model,
      HttpSession httpSession) {
    //    model.addAttribute(
    //        "portalInformation",
    //
    // PortalInformation.builder().userResource(customUserDetails.getUserResource()).build());
    String sessionId = httpSession.getId();
    model.addAttribute("sessionId", sessionId);
    return "portal";
  }
}
