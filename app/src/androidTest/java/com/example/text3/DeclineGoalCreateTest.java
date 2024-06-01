package com.example.text3;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DeclineGoalCreateTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void declineGoalCreateTest() throws InterruptedException {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.menu_button5), withContentDescription("Tikslai"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigation),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        Thread.sleep(2000);

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.categoryButton), withText("Sukurti nauja"),
                        childAtPosition(
                                allOf(withId(R.id.linear_layout_tasks),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.GoalTypeItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.GoalTypeFrame),
                                        0),
                                1)));
        appCompatSpinner.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(0);
        appCompatCheckedTextView.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.blackButton), withText("Atšaukti"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatButton2.perform(scrollTo(), click());

        Thread.sleep(2000);

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.categoryButton), withText("Sukurti nauja"),
                        childAtPosition(
                                allOf(withId(R.id.linear_layout_tasks),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.GoalTypeItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.GoalTypeFrame),
                                        0),
                                1)));
        appCompatSpinner2.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.GoalAppItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.GoalAppFrame),
                                        0),
                                1)));
        appCompatSpinner3.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView3 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.blackButton), withText("Atšaukti"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatButton4.perform(scrollTo(), click());

        Thread.sleep(2000);

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.categoryButton), withText("Sukurti nauja"),
                        childAtPosition(
                                allOf(withId(R.id.linear_layout_tasks),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner4 = onView(
                allOf(withId(R.id.GoalTypeItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.GoalTypeFrame),
                                        0),
                                1)));
        appCompatSpinner4.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(2);
        appCompatCheckedTextView4.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.blackButton), withText("Atšaukti"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatButton6.perform(scrollTo(), click());

        Thread.sleep(2000);

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.categoryButton), withText("Sukurti nauja"),
                        childAtPosition(
                                allOf(withId(R.id.linear_layout_tasks),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                0)),
                                1)));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner5 = onView(
                allOf(withId(R.id.GoalTypeItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.GoalTypeFrame),
                                        0),
                                1)));
        appCompatSpinner5.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView5 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(3);
        appCompatCheckedTextView5.perform(click());

        ViewInteraction appCompatSpinner6 = onView(
                allOf(withId(R.id.GoalAppItem),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.GoalAppFrame),
                                        0),
                                1)));
        appCompatSpinner6.perform(scrollTo(), click());

        DataInteraction appCompatCheckedTextView6 = onData(anything())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView6.perform(click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.blackButton), withText("Atšaukti"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatButton8.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}