package cms.fx.Controller.Memorial;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class views {
    @RequestMapping("/hello")
    public String hello() {
        return "hello world!";
    }
}
