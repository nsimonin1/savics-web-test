package org.savics.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by simon on 11/04/2017.
 */
@Controller
@SessionAttributes("urlForm")
public class StatuUrlController {

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/")
    public String defautPath(){
        return "redirect:/index.htm";
    }

    @GetMapping("/index.htm")
    public String index(){
        return "index";
    }

    @PostMapping("/index.htm")
    public ModelAndView postRequest(@Valid @ModelAttribute("urlForm")UrlForm urlForm, BindingResult result, ModelMap model) throws IOException {
        if (result.hasErrors()) {
            return new ModelAndView("index");
        }
        if(!urlForm.getUrl().startsWith("https://") && !urlForm.getUrl().startsWith("http://")){
            urlForm.setUrl("http://"+urlForm.getUrl());
        }
        ModelAndView mav=new ModelAndView("index");
        urlForm.setStatus(getStatus(urlForm.getUrl()));
        mav.addObject("urlForm",urlForm);
        return mav;
    }

    @ModelAttribute("urlForm")
    public UrlForm urlForm(){
        return new UrlForm();
    }
    private int getStatus(String url) throws IOException {
        try {
            URL siteURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) siteURL
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int code = connection.getResponseCode();
            return code;
        } catch (Exception e) {
           return -1;
        }

    }
}
