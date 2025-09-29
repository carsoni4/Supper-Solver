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

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + name;
    }

    @GetMapping("/{message}/{numTimes}")
    public String welcome(@PathVariable String message, @PathVariable int numTimes)
    {
        String printToScreen = "";
        for (int i = 0; i < numTimes; i++)
        {
            printToScreen += message + " ";
        }

        return printToScreen;
    }
}
