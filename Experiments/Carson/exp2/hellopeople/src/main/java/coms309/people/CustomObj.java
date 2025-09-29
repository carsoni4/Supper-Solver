package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class CustomObj {

   public String customProp;
   public String id;

    public CustomObj() {

    }

    //A custom object to show understanding
    public CustomObj(String customProp, String id) {
        this.customProp = customProp;
        this.id = id;
    }

   public String getCustomProp(){
        return this.customProp;
   }
   public void setCustomProp(String in){
        this.customProp = in;
   }

    //Updated the people to string method to include my new columns
    @Override
    public String toString() {
        return id + " " + customProp;
    }
}
