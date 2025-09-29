package coms309.people;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
public class CustomObjController {

        HashMap<String, CustomObj> customList = new  HashMap<>();


        @GetMapping("/customObj")
        public @ResponseBody HashMap<String, CustomObj> getAllCustom() {
            return customList;
        }

        // THIS IS THE CREATE OPERATION
        // springboot automatically converts JSON input into a person object and
        // the method below enters it into the list.
        // It returns a string message in THIS example.
        // in this case because of @ResponseBody
        // Note: To CREATE we use POST method
        @PostMapping("/customObj")
        public @ResponseBody String createPerson(@RequestBody CustomObj custom) {
            System.out.println(custom);
            customList.put(custom.getCustomProp(), custom);
            return "New Custom Object "+ custom.getCustomProp() + " Saved";
        }
        //to create new obj:
    /*
    {
        "customProp": "",
            "id":
    }
    */

        @GetMapping("/custom/{customProp}")
        public @ResponseBody CustomObj getCustom(@PathVariable String customProp) {
            CustomObj c = customList.get(customProp);
            return c;
        }
    }



