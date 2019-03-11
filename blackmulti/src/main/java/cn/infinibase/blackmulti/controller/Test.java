package cn.infinibase.blackmulti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author: fanchao
 * @description:
 * @date: Create in 14:53 2017/11/6
 * @modified By:
 */
@RequestMapping("/test")
@Controller
public class Test {




//    @ResponseBody
    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public String testTemplateEntity( )  {
       return "/test/index";
    }


}



