package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {

    private String firstName;

    private String lastName;

    private String age;

    private String telephone;

    private String score;

    public Person() {

    }

    //Carson 9/9/24
    //I have added an age and score column, I have created a get score and age method
    //I have added a set score method
    public Person(String firstName, String lastName, String age, String telephone, String score) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.score = score;
        this.age = age;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //grabs age
    public String getAge() {
        return this.age;
    }

    //returns score
    public String getScore(){
        return this.score;
    }

    //set score
    public void setScore(String score) {
        this.score = score;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    //Updated the people to string method to include my new columns
    @Override
    public String toString() {
        return firstName + " "
                + lastName + " "
                + age + " "
                + telephone + " "
                + score;
    }
}
