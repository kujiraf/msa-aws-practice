package com.example.msa.frontend.app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
