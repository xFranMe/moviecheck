package es.unex.moviecheck;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import es.unex.moviecheck.activities.LoginActivity;
import es.unex.moviecheck.model.Films;
import es.unex.moviecheck.room.FilmsDatabase;
import es.unex.moviecheck.support.AppExecutors;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CU03_AddCommnetTest extends Application {
    public Films films;
    public Context c = ApplicationProvider.getApplicationContext();

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);



    @Before
    public void initTest(){
        List<Integer> genresids = new ArrayList<>();
        genresids.add(28);
        genresids.add(14);
        films = new Films(true, "", "es", 0.0,"Tequeños para ti",false,0,0,
                "Tequeños para ti",genresids, "","Así se hacen los buenos tequeños, de nada","2022-12-07",0.0,0,0);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FilmsDatabase db = FilmsDatabase.getInstance(c);
                db.filmDAO().insertFilm(films);
                for(Integer genre: genresids){
                    db.filmsGenresListDAO().insertFilmGenre(films.getId(), genre);
                    db.filmsGenresListDAO().deleteFilmGenre(films.getId());
                }
            }
        });
    }

    @After
    public void finishTest(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FilmsDatabase db = FilmsDatabase.getInstance(c);
                db.filmDAO().deleteFilm(films);
            }
        });
    }

    @Test
    public void cU03_AddCommnetTest() {
        String user = "Usuario";
        String comment = "Muy rico";
        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.tvRegisterLogin), withText(R.string.register),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        materialTextView.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.etUsernameRegister),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                6)));
        appCompatEditText.perform(scrollTo(), replaceText(user), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.etEmailRegister),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        appCompatEditText2.perform(scrollTo(), replaceText("usuario@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.etPasswordRegister),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                9)));
        appCompatEditText3.perform(scrollTo(), replaceText("Usuario1"), closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.etRepeatPasswordRegister),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                10)));
        appCompatEditText4.perform(scrollTo(), replaceText("Usuario1"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.bRegister), withText(R.string.register),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                7)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.fragment_explore),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction tabView = onView(
                allOf(withContentDescription(R.string.detail_social),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tlDetail),
                                        0),
                                1),
                        isDisplayed()));
        tabView.perform(click());

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.ibSendComment), withContentDescription(R.string.detail_comment),
                        withParent(allOf(withId(R.id.constraintLayout),
                                withParent(withId(R.id.detail_social)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.etCommentSocial),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withId(R.id.detail_social),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText(comment), closeSoftKeyboard());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.etCommentSocial),
                        withParent(allOf(withId(R.id.constraintLayout),
                                withParent(withId(R.id.detail_social)))),
                        isDisplayed()));
        editText2.check(matches(withText(comment)));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.ibSendComment), withContentDescription(R.string.detail_comment),
                        childAtPosition(
                                allOf(withId(R.id.constraintLayout),
                                        childAtPosition(
                                                withId(R.id.detail_social),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.tvUsernameComment),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                        isDisplayed()));
        textView.check(matches(withText(user)));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.tvCommentText),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                        isDisplayed()));
        textView2.check(matches(withText(comment)));

        pressBack();

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.fragment_explore),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0)));
        recyclerView2.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction tabView2 = onView(
                allOf(withContentDescription("Social"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.tlDetail),
                                        0),
                                1),
                        isDisplayed()));
        tabView2.perform(click());

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.tvUsernameComment),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                        isDisplayed()));
        textView3.check(matches(withText(user)));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.tvCommentText),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(androidx.cardview.widget.CardView.class))),
                        isDisplayed()));
        textView4.check(matches(withText(comment)));

        pressBack();

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.navigation_profile), withContentDescription(R.string.title_profile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.bDeleteAccount), withText(R.string.profile_delete_account),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                8)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.bDelete), withText(R.string.profile_delete_permanently),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        materialButton3.perform(scrollTo(), click());
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
