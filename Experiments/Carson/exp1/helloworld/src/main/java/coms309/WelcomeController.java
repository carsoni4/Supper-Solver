package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/n/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + name;
    }

    @GetMapping("/Carson")
    public String poc(){
        return "This is a proof of concept!";
    }

    @GetMapping("/a/{age}")
    public String getAge(@PathVariable String age){
        return "Your age is " + age;
    }
    @GetMapping("/mathx2/{num}")
    public int mathx2(@PathVariable int num){

        return num * 2;

    }


}
