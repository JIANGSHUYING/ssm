package cn.itcast.controller;

import cn.itcast.domain.User;
import cn.itcast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    //登录
    @RequestMapping("/login")
    public String login(User user, Integer ck, HttpServletResponse response, HttpSession session){
        User login = userService.login(user);
        if(login==null){
            return "loginError";
        }
        //记住用户名和密码
        Cookie cookieName = new Cookie("username",user.getUsername());
        Cookie cookiepassword = new Cookie("password",user.getPassword()+"");

        //只要能登录成功才需要记住密码
        if(ck!=null&&ck==1){

            //设置最大存活时间
            cookieName.setMaxAge(7*24*60*60);
            cookiepassword.setMaxAge(7*24*60*60);

        }
        else{
            //
            cookieName.setMaxAge(0);
            cookiepassword.setMaxAge(0);
        }
        cookieName.setPath("/");
        cookiepassword.setPath("/");

        //将cookie送回页面
        response.addCookie(cookieName);
        response.addCookie(cookiepassword);

        //将login存入session  在拦截器中进行判断是否拦截
        session.setAttribute("user",login);

        return "index1";
    }

    //查询所有
    @RequestMapping("/findAll")
    public String findAll(Model model){
        List<User> all = userService.findAll();
        model.addAttribute("userList",all);
        return "list";
    }


    //保存插入对象
    @RequestMapping("/saveUser")
    public String saveUser(User user,@RequestParam("username") String name){
        System.out.println(name);
        System.out.println(user.getUsername());
        System.out.println("---------------");
        userService.saveUser(user);
        return "redirect:findAll";
    }


    //删除用户
    @RequestMapping("/deleteUser")
    public String deleteUser(User user,@RequestParam("id")Integer id){
        System.out.println(user);
        System.out.println("000000000000000000");
        System.out.println(id);
        userService.deleteUser(id);
        return "forward:findAll";
    }

}
