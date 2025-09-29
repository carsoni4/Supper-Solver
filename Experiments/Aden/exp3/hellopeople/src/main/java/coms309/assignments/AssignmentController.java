package  coms309.assignments;

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
public class AssignmentController
{
    HashMap<String, Assignment> assignmentList = new HashMap<>();

    @GetMapping("/assignments")
    public HashMap<String,Assignment> getAssignments()
    {
        return assignmentList;
    }

    @PostMapping("/assignments")
    public String createAssignment(@RequestBody Assignment assignment) {
        System.out.println(assignment);
        assignmentList.put(assignment.getAssignmentName(), assignment);
        return "New assignment " + assignment.getAssignmentName() + " saved";
    }

    @PutMapping("/assignment/{assignmentName}")
    public Assignment updateAssignment(@PathVariable String assignmentName, @RequestBody Assignment assignment) {
        assignmentList.replace(assignmentName, assignment);
        return assignmentList.get(assignmentName);
    }

    @DeleteMapping("/assignment/{assignmentName}")
    public HashMap<String, Assignment> deleteAssignment(@PathVariable String assignmentName) {
        assignmentList.remove(assignmentName);
        return assignmentList;
    }
}