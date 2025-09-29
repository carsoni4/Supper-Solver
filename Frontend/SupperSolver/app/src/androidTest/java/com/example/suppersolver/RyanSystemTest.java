package com.example.suppersolver;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;


public class RyanSystemTest {
    private static final int SIMULATED_DELAY_MS = 500;

    @Rule   // needed to launch the activity
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void test1() {
        String name1 = "johndoe123";
        String name2 = "john";

        onView(withId(R.id.login)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.homefeed_nextBtn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

    }

    @Test
    public void test2() {

        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.inventory_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
    }

    @Test
    public void test3() {
        String name1 = "recipe";

        onView(withId(R.id.login)).perform(click());
        onView(withId(R.id.login_username_edt)).perform(typeText("johndoe123"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("password123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.recipe_btn)).check(matches(withText(name1)));
        onView(withId(R.id.homefeed_nextBtn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.recipe_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.back_button)).perform(click());

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
        onView(withId(R.id.homefeed_postBtn)).perform(click());
    }

    @Test
    public void test4() {
        onView(withId(R.id.signup)).perform(click());
        onView(withId(R.id.name)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.username)).perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.confirm_password)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.signup_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.login_username_edt)).perform(typeText("testuser"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edt)).perform(typeText("pass"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.view_my_profile)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.welcomeMessage)).check(matches(withText(endsWith("testuser"))));
        onView(withId(R.id.editProfile)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}

        onView(withId(R.id.delete_profile_btn)).perform(click());
        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {}
    }
}
