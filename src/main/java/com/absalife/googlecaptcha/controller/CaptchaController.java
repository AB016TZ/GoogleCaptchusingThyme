/**
 * 
 */
package com.absalife.googlecaptcha.controller;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.absalife.googlecaptcha.model.Login;
import com.absalife.googlecaptcha.model.ReCaptchaResponse;
import com.absalife.googlecaptcha.service.CaptchaService;
import java.util.Random;

/**
 * @author AB016TZ
 *
 */
@Controller
@RequestMapping("/")
public class CaptchaController {
	
	
@Autowired
CaptchaService service;

@Autowired
RestTemplate restTemplate;



@Value("${recaptcha.url}")
private String url;

@Value("${recaptcha.secret}")
private String secret;

@GetMapping("/")
public String showHomePage(Model model) {
	model.addAttribute("pageName", "HomePage");
	return "add";
}


@GetMapping("/success")
public String showProjects(Model model) {
	model.addAttribute("pageName", "success");
	return "projects";	
}

@GetMapping("/invalid")
public String showInvalid(Model model) {
	model.addAttribute("pageName", "invalid");
	return "invalid";
}

@PostMapping("/save")
public String save(@ModelAttribute("loginForm") Login login, @RequestParam(name="g-recaptcha-response") String captchaResponse) {
    System.out.println("In Captcha Controller           " + captchaResponse);
    HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
	map.add("secret", secret);
	map.add("response", captchaResponse);
	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
	ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url, HttpMethod.POST, request,ReCaptchaResponse.class).getBody();
	System.out.println(" Response body   "+reCaptchaResponse.getChallenge_ts()+"   "+reCaptchaResponse.getHostName()+"  "+reCaptchaResponse.isSuccess());
	if(reCaptchaResponse.isSuccess()) {
		return "redirect:/success";
	} else {
		return "redirect:/invalid";
	}
  }

@PostMapping("/ValidateCaptcha")
public String validateCaptcha (@RequestParam String captchaResponse ) {
    System.out.println("In Captcha Controller" + captchaResponse);
    HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
	map.add("secret", secret);
	map.add("response",captchaResponse);
	System.out.println("************* MAP ************"+map);
	HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
	System.out.println("REQUEST ************" +request);
	ReCaptchaResponse reCaptchaResponse = restTemplate.exchange(url, HttpMethod.POST, request,ReCaptchaResponse.class).getBody();
	System.out.println(" Response body   "+reCaptchaResponse.getChallenge_ts()+"   "+reCaptchaResponse.getHostName()+"  "+reCaptchaResponse.isSuccess());
	if(reCaptchaResponse.isSuccess()) {
		return "redirect:/success";
	} else {
		return "Invalid Captcha";
	}
  }

/**
 *  Generate a CAPTCHA String consisting of random lowercase & uppercase letters, and numbers.
 */
Random random = new Random();
public String generateCaptchaString() {
	int length = 7 + (Math.abs(random.nextInt()) % 3);

	StringBuffer captchaStringBuffer = new StringBuffer();
	for (int i = 0; i < length; i++) {
		int baseCharNumber = Math.abs(random.nextInt()) % 62;
		int charNumber = 0;
		if (baseCharNumber < 26) {
			charNumber = 65 + baseCharNumber;
		}
		else if (baseCharNumber < 52){
			charNumber = 97 + (baseCharNumber - 26);
		}
		else {
			charNumber = 48 + (baseCharNumber - 52);
		}
		captchaStringBuffer.append((char)charNumber);
	}

	return captchaStringBuffer.toString();
}
}
