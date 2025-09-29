package com.example.suppersolver;

import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;


public class BrandonSystemTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Start the server and run this test
     */
    @Test
    public void TestUserProfile() {
        String testDescription = "I love cooking and sharing recipes.";

        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        onView(withId(R.id.view_my_profile)).perform(click());
        onView(withId(R.id.description)).check(matches(withText(endsWith(testDescription))));

        onView(withId(R.id.back_button)).perform(click());
        onView(withId(R.id.recipe_btn)).perform(click());
        onView(withId(R.id.recipe_comments_btn)).perform(click());

    }

    /**
     * Start the server and run this test
     */
    @Test
    public void TestUserComments() {
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        onView(withId(R.id.recipe_btn)).perform(click());
        onView(withId(R.id.recipe_comments_btn)).perform(click());
        onView(withId(R.id.comment_edt)).perform(typeText("Test Comment"));
        onView(withId(R.id.cmtSend)).perform(click());
    }

    /**
     * Start the server and run this test
     */
    @Test
    public void TestUserFriends() {
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        onView(withId(R.id.view_my_profile)).perform(click());
        onView(withId(R.id.viewFriends)).perform(click());

        String testFriendList = "john";
        onView(withId(R.id.friendsList)).check(matches(hasDescendant(withText(testFriendList))));
    }

    /**
     * Start the server and run this test
     */
    @Test
    public void TestSearchAndFilter() {
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
        }

        onView(withId(R.id.search_btn)).perform(click());

        onView(withId(R.id.filterUsers)).perform(click());
        onView(withId(R.id.searchListView)).check(matches(hasDescendant(withText("johndoe123"))));

        onView(withId(R.id.filterRecipes)).perform(click());
        onView(withId(R.id.searchListView)).check(matches(hasDescendant(withText("recipe"))));

    }

    /**
     * Start the server and run this test
     */
    @Test
    public void TestUserMessaging() {
        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());

        onView(withId(R.id.view_my_profile)).perform(click());
        onView(withId(R.id.viewFriends)).perform(click());
    }
}
