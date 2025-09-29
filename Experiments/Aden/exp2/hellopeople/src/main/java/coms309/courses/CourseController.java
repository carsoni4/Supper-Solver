package coms309.courses;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Aden Koziol
 */

@RestController
public class CourseController
{
    HashMap<String, Course> courseList = new HashMap<>();

    @GetMapping("/courses")
    public HashMap<String,Course> getCourse()
    {
        return courseList;
    }

    @PostMapping("/courses")
    public String createCourse(@RequestBody Course course) {
        System.out.println(course);
        courseList.put(course.getCourseName(), course);
        return "New course " + course.getCourseName() + " saved";
    }

    @PutMapping("/courses/{className}")
    public Course updateCourse(@PathVariable String className, @RequestBody Course course) {
        courseList.replace(className, course);
        return courseList.get(className);
    }

    @DeleteMapping("/courses/{className}")
    public HashMap<String, Course> deleteCourse(@PathVariable String className) {
        courseList.remove(className);
        return courseList;
    }
}
