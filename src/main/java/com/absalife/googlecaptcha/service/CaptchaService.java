/**
 * 
 */
package com.absalife.googlecaptcha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.absalife.googlecaptcha.model.ReCaptchaResponse;
/**
 * @author AB016TZ
 *
 */

@Service
public class CaptchaService {

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${recaptcha.url}")
	private String url;
	
	@Value("${recaptcha.secret}")
	private String secret;

	
	public ResponseEntity<ReCaptchaResponse> validateCaptcha(String captchaResponse) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("secret", secret);
		map.add("response", captchaResponse);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map,headers);
		ResponseEntity<ReCaptchaResponse> reCaptchaResponse = restTemplate.exchange(url, HttpMethod.POST, request,ReCaptchaResponse.class);
		System.out.println(" Response body   "+reCaptchaResponse.getBody().getChallenge_ts()+"   "+reCaptchaResponse.getBody().getHostName()+"  "+reCaptchaResponse.getBody().isSuccess());
		return reCaptchaResponse;
	}

}
