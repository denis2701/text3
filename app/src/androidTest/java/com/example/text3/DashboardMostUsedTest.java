package com.example.text3;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DashboardMostUsedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testTest() {
        onView(withId(R.id.mostUsedButton)).perform(scrollTo());

        onView(allOf(withId(R.id.mostUsedButton), isDisplayed())).perform(click());

        onView(withId(R.id.appUsageButton)).perform(scrollTo());

        onView(allOf(withId(R.id.appUsageButton), isDisplayed())).perform(click());

        onView(withId(android.R.id.button1)).perform(click());
    }
}
