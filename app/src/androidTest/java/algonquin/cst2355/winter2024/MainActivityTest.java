package algonquin.cst2355.winter2024;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

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
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void mainActivityTest() {
        // Test input: "1234"
        ViewInteraction appCompatEditText = onView(withId(R.id.edit));
        appCompatEditText.perform(replaceText("1234"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to find missing upper case letter in password.
     * Validates that the proper error message is displayed.
     */
    @Test
    public void testFindMissingUpperCase() {
        // Test input: "password1234#"
        ViewInteraction appCompatEditText = onView(withId(R.id.edit));
        appCompatEditText.perform(replaceText("password1234#"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to find missing lower case letter in password.
     * Validates that the proper error message is displayed.
     */
    @Test
    public void testFindMissingLowerCase() {
        // Test input: "PASSWORD@34"
        ViewInteraction appCompatEditText = onView(withId(R.id.edit));
        appCompatEditText.perform(replaceText("PASSWORD@34"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to find missing number in password.
     * Validates that the proper error message is displayed.
     */
    @Test
    public void testFindMissingNumber() {
        // Test input: "Pas@word"
        ViewInteraction appCompatEditText = onView(withId(R.id.edit));
        appCompatEditText.perform(replaceText("Pas@word"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    /**
     * Test case to find missing special character in password.
     * Validates that the proper error message is displayed.
     */
    @Test
    public void testFindMissingSpecials() {
        // Test input: "Pass4word"
        ViewInteraction appCompatEditText = onView(withId(R.id.edit));
        appCompatEditText.perform(replaceText("Pass4word"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("You shall not pass!")));
    }

    @Test
    public void testSuccessfulValidation(){
        // Test input: "Pass4word"
        ViewInteraction appCompatEditText = onView(withId(R.id.edit));
        appCompatEditText.perform(replaceText("P@ss4word"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.login));
        materialButton.perform(click());

        ViewInteraction textView = onView(withId(R.id.textView));
        textView.check(matches(withText("Your password meets the requirements")));
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
                        && view.equals(((ViewGroup)parent).getChildAt(position));
            }
        };
    }
}
