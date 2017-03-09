package walkthrough.a2doapp.com.walkthrough;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 *
 * The only thing you need to do is customize the {@link #initSections()} method
 * and add your walkthrough sections there
 *
 * Created by fahad on 21/09/2016.
 */

public class WalkthroughActivityFragment extends Fragment implements ViewPager.OnPageChangeListener {
  //region Properties
  ViewGroup viewRoot;
  ViewGroup topLabelsContainer;
  ViewGroup topNameContainer;

  TextView lbl2DoName;
  TextView lbl2DoVersion;
  TextView lblWelcomeTitle;
  TextView lblWatchVideo;

  ViewGroup phoneContainer;
  AppCompatImageView imgPhoneFrame;
  ScaleImageView imgPhoneShot;
  ViewGroup bottomContainer;
  ViewPager tutorialPager;
  CircleIndicator circularIndicator;
  AppCompatButton btnDone;


  List<WalkthroughSection> walkthroughSectionList = new ArrayList<>();
  private int bounceDPs = 10;

  private int screenHeight;
  private int screenWidth;

  private int phoneContainerHeight;
  private int phoneContainerWidth;

  private int phoneLeft;
  private int phoneContainerTop;

  private int bottomContainerTop;
  private int tutorialButtonBottom;
  private Handler uiHandler;
  private WalkthroughInternalPagerAdapter pagerAdapter;

  //endregion

  //region View Creation
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.activity_walkthrough, container, false);

    viewRoot = (ViewGroup) rootView.findViewById(R.id.viewRoot);
    topLabelsContainer = (ViewGroup) rootView.findViewById(R.id.topLabelsContainer);
    topNameContainer = (ViewGroup) rootView.findViewById(R.id.topNameContainer);

    lbl2DoName = (TextView) rootView.findViewById(R.id.lbl2DoName);
    lbl2DoVersion = (TextView) rootView.findViewById(R.id.lbl2DoVersion);
    lblWelcomeTitle = (TextView) rootView.findViewById(R.id.lblWelcomeTitle);

    lblWatchVideo = (TextView) rootView.findViewById(R.id.lblWatchVideo);

    phoneContainer = (ViewGroup) rootView.findViewById(R.id.phoneContainer);
    imgPhoneFrame = (AppCompatImageView) rootView.findViewById(R.id.imgPhoneFrame);
    imgPhoneShot = (ScaleImageView) rootView.findViewById(R.id.imgPhoneShot);

    bottomContainer = (ViewGroup) rootView.findViewById(R.id.bottomContainer);
    tutorialPager = (ViewPager) rootView.findViewById(R.id.tutorialPager);

    circularIndicator = (CircleIndicator) rootView.findViewById(R.id.circularIndicator);

    btnDone = (AppCompatButton) rootView.findViewById(R.id.btnDone);
    btnDone.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        doneClicked();
      }
    });

    return rootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initUI();
  }

  private void initUI() {
    uiHandler = new Handler(Looper.getMainLooper());

    initSections();

    pagerAdapter = new WalkthroughInternalPagerAdapter(getActivity().getSupportFragmentManager());
    tutorialPager.setAdapter(pagerAdapter);
    tutorialPager.addOnPageChangeListener(this);
    circularIndicator.setViewPager(tutorialPager);

    lblWatchVideo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse("https://www.youtube.com/watch?v=OO_GYy12usc")));
      }
    });

    viewRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
      @Override
      public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
                                 int oldTop, int oldRight, int oldBottom) {
        // its possible that the layout is not complete in which case
        // we will get all zero values for the positions, so ignore the event
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
          return;
        }

        viewRoot.removeOnLayoutChangeListener(this);

        phoneContainerHeight = phoneContainer.getHeight();
        phoneContainerWidth = phoneContainer.getWidth();
        phoneLeft = phoneContainer.getLeft();

        screenHeight = viewRoot.getHeight();
        screenWidth = viewRoot.getWidth();

        bottomContainerTop = (int) bottomContainer.getY();
        tutorialButtonBottom = (int) lblWatchVideo.getBottom();

        phoneContainerTop = (int) phoneContainer.getY();

        loadImageResource(R.drawable.tut_welcome);

        showPhone(-5, 0, 0, false, null);

        uiHandler.postDelayed(new Runnable() {
          @Override
          public void run() {
            revealPhoneFirstTime(700, false);
          }
        }, 200);
      }
    });

    phoneContainer.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (tutorialPager.getCurrentItem() == 0) {
          revealPhoneFirstTime(300, true);
        }
      }
    });
  }

  /**
   * This is the only section you need to customize in order to get as many Walkthrough Sections as you want
   */
  private void initSections() {
    lbl2DoVersion.setText("| v2.3");

    // Welcome
    WalkthroughSection section = new WalkthroughSection();
    section.sectionTitle = "Welcome";
    section.sectionDescription = "2Do lets you take a completely different approach to managing your tasks. In fact, there is no wrong way of using 2Do, as it won't force you in adhering to a particular task management methodology.\n\nNo matter how you fancy doing it, 2Do helps get stuff done.";
    section.sectionDeviceVerticalPercToShow = 25;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = 0;
    section.sectionShotName = R.drawable.tut_welcome;
    walkthroughSectionList.add(section);


    // Inbox
    section = new WalkthroughSection();
    section.sectionTitle = "Native Inbox for Getting Things Done®";
    section.sectionDescription = "Fancy David Allen's GTD® methodology? Enable the special Inbox to serve as your default collection list from Settings > Default Collection List.";
    section.sectionDeviceVerticalPercToShow = 40;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = 0;
    section.prolongMovingUp = 28;
    section.sectionShotName = R.drawable.tut_inbox;
    walkthroughSectionList.add(section);


    // Pinch Zooming
    section = new WalkthroughSection();
    section.sectionTitle = "Pinch Zooming";
    section.sectionDescription = "Pinch-in on the task list to show less information associated with each task in order to fit more tasks on the screen, and pinch-out to show multi-line titles and notes.";
    section.sectionDeviceVerticalPercToShow = 45;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = 16;
    section.prolongZoomIn = 3;
    section.sectionShotName = R.drawable.tut_zoom;
    walkthroughSectionList.add(section);


    // Zoom in on demand
    section = new WalkthroughSection();
    section.sectionTitle = "Zoom-in on-demand";
    section.sectionDescription = "Is the title for a task too long to fit in a single line? Tap on the little arrow displayed to the right side of a task to expand. You can also use this extended view as means to quickly change the priority for a given task.";
    section.sectionDeviceVerticalPercToShow = 45;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = 5;
    section.prolongZoomOut = 2;
    section.sectionShotName = R.drawable.tut_priority;
    walkthroughSectionList.add(section);


    // Organize your lists with list groups
    section = new WalkthroughSection();
    section.sectionTitle = "Organise your Lists with List Groups";
    section.sectionDescription = "Use List Groups for simple decluttering and grouping of related lists, or use them as areas of responsibilities. Tap on the List Group name to hide and unhide grouped lists.";
    section.sectionDeviceVerticalPercToShow = 63;
    section.sectionDeviceHorizontalPercToShow = 50;
    section.sectionDeviceZoomPercToShow = 0;
    section.prolongMovingUp = 25;
    section.sectionShotName = R.drawable.tut_lists;
    walkthroughSectionList.add(section);


    // Tags Panel
    section = new WalkthroughSection();
    section.sectionTitle = "The new Tags Panel";
    section.sectionDescription = "Swipe the task list to the left to reveal the Tags Panel. Manage your Tags and Tag Groups all in one place. Tap on a Tag to quickly filter your tagged Tasks.";
    section.sectionDeviceVerticalPercToShow = 65;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = -5;
    section.prolongZoomOut = 3;
    section.sectionShotName = R.drawable.tut_tags;
    walkthroughSectionList.add(section);


    // Sorting
    section = new WalkthroughSection();
    section.sectionTitle = "Changing Sort method and using Focus";
    section.sectionDescription = "Tap on the sort bar, directly above the main task list, to reveal different sorting options. Tap on ☼ on the right to toggle the Focus filter ON or OFF. You can change the Focus Filter criteria for each list by touching and holding on ☼.";
    section.sectionDeviceVerticalPercToShow = 25;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = 15;
    section.prolongMovingDown = 5;
    section.sectionShotName = R.drawable.tut_sort1;
    walkthroughSectionList.add(section);


    // Editors
    section = new WalkthroughSection();
    section.sectionTitle = "Fast and fluid mini editors";
    section.sectionDescription = "With focus on reducing the number of taps required to switch between editors for a given task, the newly designed and refined mini editors are fast to use and allow you to quickly toggle between screens without wasting your valuable time.";
    section.sectionDeviceVerticalPercToShow = 75;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = -25;
    section.prolongZoomOut = 2;
    section.sectionShotName = R.drawable.tut_editors;
    walkthroughSectionList.add(section);


    // Batch Editing
    section = new WalkthroughSection();
    section.sectionTitle = "Batch editing";
    section.sectionDescription = "Tap on ... at the top right hand side of the task list to enter Batch Edit mode. Select tasks to edit and then select one of the options from the edit bar.";
    section.sectionDeviceVerticalPercToShow = 75;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = -22;
    section.prolongZoomIn = 3;
    section.sectionShotName = R.drawable.tut_batch;
    walkthroughSectionList.add(section);


    // Smart Lists
    section = new WalkthroughSection();
    section.sectionTitle = "Creating Smart Lists";
    section.sectionDescription = "Tap on Search above the task list to reveal search options. Tap on the Search Presets button or the Calendar button to pick a search preset or a date range, or enter a search term. Tap on {+} to save this search as a Smart List.";
    section.sectionDeviceVerticalPercToShow = 48;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = 0;
    section.prolongMovingDown = 10;
    section.sectionShotName = R.drawable.tut_smartlist;
    walkthroughSectionList.add(section);


    // Putting Tasks to Sleep
    section = new WalkthroughSection();
    section.sectionTitle = "Putting Tasks to Sleep";
    section.sectionDescription = "Selectively hide tasks that you will get to someday, or ones are no longer relevant, by assigning them a paused Tag. Tags can be paused and resumed with a long-tap on the selected tag in the Tags Panel.";
    section.sectionDeviceVerticalPercToShow = 66;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = -5;
    section.prolongMovingUp = 10;
    section.sectionShotName = R.drawable.tut_pause;
    walkthroughSectionList.add(section);


    // Indicators for tasks due today
    section = new WalkthroughSection();
    section.sectionTitle = "Indicator for tasks due today";
    section.sectionDescription = "Got lots of lists and want to get an overview of what's due today? A soft white glow will appear next to the list name if one or more tasks are due today. This even works for lists that have their visibility scope changed to not display tasks under Today and All.";
    section.sectionDeviceVerticalPercToShow = 40;
    section.sectionDeviceHorizontalPercToShow = 60;
    section.sectionDeviceZoomPercToShow = 50;
    section.prolongZoomIn = 3;
    section.sectionShotName = R.drawable.tut_duetoday;
    walkthroughSectionList.add(section);


    // Protecting your lists
    section = new WalkthroughSection();
    section.sectionTitle = "Protecting your lists";
    section.sectionDescription = "You can protect your lists individually or lockout the whole app. Explore these settings and more under Settings > Password protected lists.";
    section.sectionDeviceVerticalPercToShow = 70;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = -15;
    section.prolongMovingUp = 25;
    section.sectionShotName = R.drawable.tut_lock;
    walkthroughSectionList.add(section);


    // Start using 2Do
    section = new WalkthroughSection();
    section.sectionTitle = "Start using 2Do";
    section.sectionDescription = "Let's get productive!";
    section.sectionDeviceVerticalPercToShow = -5;
    section.sectionDeviceHorizontalPercToShow = 0;
    section.sectionDeviceZoomPercToShow = -15;
    section.sectionShotName = R.drawable.tut_lock;
    walkthroughSectionList.add(section);
  }
  //endregion

  //region Animations
  private void revealPhoneFirstTime(int duration, final boolean clicked) {
    // Show 25% above rim, but 56 below the tutorial button
    int twentyFiveAboverim = (bottomContainerTop - phoneContainerTop) - ((phoneContainerHeight * 25) / 100);
    int distanceFromCenterofPhonetoRight = phoneContainerWidth / 2;

    if (twentyFiveAboverim - convertDpToPx(getActivity(), 48) <= tutorialButtonBottom) {
      twentyFiveAboverim = (int) (tutorialButtonBottom + convertDpToPx(getActivity(), 48));
    }

    if (clicked) {
      twentyFiveAboverim -= convertDpToPx(getActivity(), 25);
    }

    if (lblWelcomeTitle != null) {
      lblWelcomeTitle.animate().alpha(1);
      lblWatchVideo.animate().alpha(1);
      lbl2DoName.animate().alpha(1);
      lbl2DoVersion.animate().alpha(1);

      phoneContainer.animate().withLayer().translationY(twentyFiveAboverim).translationX(0)
        .scaleX(1).scaleY(1).setDuration(duration).setInterpolator(new DecelerateInterpolator())
        .setListener(new AnimatorListenerAdapter() {
          boolean gotCancelled = false;

          @Override
          public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);

            if (!gotCancelled) {
              bounceDPs = 10 + (clicked ? 10 : 0);

              bouncePhoneDown();
            }
          }

          @Override
          public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);

            gotCancelled = true;
          }
        });
    }
  }

  private void bouncePhoneUp() {
    if (bounceDPs == 0 || phoneContainer == null) {
      return;
    }
    phoneContainer.animate().translationYBy(convertDpToPx(getActivity(), bounceDPs) * -1)
      .setDuration(900).setInterpolator(new DecelerateInterpolator())
      .setListener(new AnimatorListenerAdapter() {
        boolean gotCancelled = false;

        @Override
        public void onAnimationEnd(Animator animation) {
          super.onAnimationEnd(animation);

          if (!gotCancelled && bounceDPs != 0) {
            bounceDPs = 10;

            bouncePhoneDown();
          }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
          super.onAnimationCancel(animation);

          gotCancelled = true;
        }
      });
  }

  private void bouncePhoneDown() {
    if (bounceDPs == 0 || phoneContainer == null) {
      return;
    }
    phoneContainer.animate().translationYBy(convertDpToPx(getActivity(), bounceDPs))
      .setDuration(900).setInterpolator(new AccelerateInterpolator())
      .setListener(new AnimatorListenerAdapter() {
        boolean gotCancelled = false;

        @Override
        public void onAnimationEnd(Animator animation) {
          super.onAnimationEnd(animation);

          if (!gotCancelled && bounceDPs != 0) {
            bounceDPs = 10;

            bouncePhoneUp();
          }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
          super.onAnimationCancel(animation);

          gotCancelled = true;
        }
      });
  }

  /**
   * @param verticalPercentage   0 = just below bottom container rim, 100 = bottom of phone touching rim
   * @param horizontalPercentage 0 = screen center / normal, 100 = phone container's left edge touching Vertical center axis, -100 = phone's right edge touching center axis
   * @param zoomPercentage
   * @param animated
   * @param walkthroughSection
   */
  private void showPhone(int verticalPercentage, final int horizontalPercentage,
                         final int zoomPercentage, boolean animated,
                         final WalkthroughSection walkthroughSection) {
    if (animated) {
      // Don't bounce anymore
      bounceDPs = 0;
    }
    final int distanceFromCenterofPhonetoRight = phoneContainerWidth / 2;
    final int verticalTranslationY = (bottomContainerTop - phoneContainerTop) - ((phoneContainerHeight * verticalPercentage) / 100);

    boolean labelAndPhoneOverlaps = verticalTranslationY - convertDpToPx(getActivity(), 48) <= tutorialButtonBottom;
    boolean versionNumberAndPhoneOverlaps = verticalTranslationY - convertDpToPx(getActivity(), 24) <= lbl2DoVersion.getBottom() || zoomPercentage >= 30;

    if (animated) {
      lbl2DoName.animate().alpha(versionNumberAndPhoneOverlaps ? 0 : 1);
      lbl2DoVersion.animate().alpha(versionNumberAndPhoneOverlaps ? 0 : 1);
      lblWelcomeTitle.animate().alpha(labelAndPhoneOverlaps ? 0 : 1).setDuration(250);
      lblWatchVideo.animate().alpha(labelAndPhoneOverlaps ? 0 : 1).setDuration(250);

      final float finalScaleToUse = 1f + (((float) zoomPercentage) / 100);

      phoneContainer.animate().withLayer()
        .translationY(verticalTranslationY)
        .translationX((distanceFromCenterofPhonetoRight * horizontalPercentage) / 100)
        .scaleX(finalScaleToUse)
        .scaleY(finalScaleToUse)
        .setDuration(300)
        .setInterpolator(new DecelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
        boolean gotCancelled = false;

        @Override
        public void onAnimationEnd(Animator animation) {
          super.onAnimationEnd(animation);

          if (!gotCancelled) {
            // Prolong effect now
            if (walkthroughSection != null && phoneContainer != null) {
              if (walkthroughSection.prolongMovingUp > 0) {
                phoneContainer.animate().withLayer()
                  .translationY(verticalTranslationY - convertDpToPx(getActivity(), walkthroughSection.prolongMovingUp))
                  .setDuration(20 * 1000);
              } else if (walkthroughSection.prolongMovingDown > 0) {
                phoneContainer.animate().withLayer()
                  .translationY(verticalTranslationY + convertDpToPx(getActivity(), walkthroughSection.prolongMovingDown))
                  .setDuration(20 * 1000);
              } else if (walkthroughSection.prolongZoomIn > 0) {
                float extraDps = (((float) convertDpToPx(getActivity(), walkthroughSection.prolongZoomIn))) / 100.0f;

                phoneContainer.animate().withLayer()
                  .scaleX(finalScaleToUse + extraDps)
                  .scaleY(finalScaleToUse + extraDps)
                  .setDuration(20 * 1000);
              } else if (walkthroughSection.prolongZoomOut > 0) {
                float extraDps = (((float) convertDpToPx(getActivity(), walkthroughSection.prolongZoomOut))) / 100.0f;

                phoneContainer.animate().withLayer()
                  .scaleX(finalScaleToUse - extraDps)
                  .scaleY(finalScaleToUse - extraDps)
                  .setDuration(20 * 1000);
              }
            }
          }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
          super.onAnimationCancel(animation);

          gotCancelled = true;
        }
      });
    } else {
      lbl2DoName.setAlpha(versionNumberAndPhoneOverlaps ? 0 : 1);
      lbl2DoVersion.setAlpha(versionNumberAndPhoneOverlaps ? 0 : 1);
      lblWelcomeTitle.setAlpha(labelAndPhoneOverlaps ? 0 : 1);
      lblWatchVideo.setAlpha(labelAndPhoneOverlaps ? 0 : 1);
      phoneContainer.setTranslationY(verticalTranslationY);
      phoneContainer
        .setTranslationX((distanceFromCenterofPhonetoRight * horizontalPercentage) / 100);
      phoneContainer.setScaleX(1f + (((float) zoomPercentage) / 100));
      phoneContainer.setScaleY(1f + (((float) zoomPercentage) / 100));
    }
  }
  //endregion

  //region Button Actions
  public void doneClicked() {
    bounceDPs = 0;
    phoneContainer.animate().cancel();
    getActivity().finish();
  }
  //endregion

  //region Pager Listener
  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    Log.v("Walkthrough", "Page Selected: " + position);
    WalkthroughSection section = walkthroughSectionList.get(position);

    loadImageResource(section.sectionShotName);

    if (position == 0) {
      revealPhoneFirstTime(300, false);
    } else {
      showPhone(section.sectionDeviceVerticalPercToShow, section.sectionDeviceHorizontalPercToShow,
        section.sectionDeviceZoomPercToShow, true, section);
    }
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }
  //endregion

  //region Image Loading
  private void loadImageResource(@DrawableRes final int imageId) {
    new Thread(new Runnable() {
      public void run() {
        if (imgPhoneShot != null) {
          final Resources resources = getActivity().getResources();
          final Bitmap imageBitmap = decodeSampledBitmapFromResource(resources, imageId,
            (int) (phoneContainerWidth * 0.5f), (int) (phoneContainerHeight * .5f));

          // Set the new image to transition to
          TransitionDrawable transitionDrawable = null;

          final Drawable[] layers = new Drawable[2];

          Drawable oldDrawable = imgPhoneShot.getDrawable();
          BitmapDrawable oldBitmapDrawable = null;
          if (oldDrawable instanceof TransitionDrawable) {
            TransitionDrawable oldTransitionDrawable = (TransitionDrawable) oldDrawable;
            oldBitmapDrawable = (BitmapDrawable) (oldTransitionDrawable).getDrawable(1);
          } else if (oldDrawable instanceof BitmapDrawable) {
            oldBitmapDrawable = (BitmapDrawable) oldDrawable;
          }

          if (oldBitmapDrawable != null && imageBitmap != null) {
            layers[0] = oldBitmapDrawable;
            layers[1] = new BitmapDrawable(resources, imageBitmap);

            transitionDrawable = new TransitionDrawable(layers);
            transitionDrawable.setCrossFadeEnabled(true);
          }

          final TransitionDrawable finalTransitionDrawable = transitionDrawable;
          uiHandler.post(new Runnable() {
            public void run() {
              if (imgPhoneShot != null) {
                if (finalTransitionDrawable != null) {
                  imgPhoneShot.setImageDrawable(finalTransitionDrawable);
                  finalTransitionDrawable.startTransition(150);
                } else if (imageBitmap != null) {
                  imgPhoneShot.setImageBitmap(imageBitmap);
                }
              }
            }
          });
        }
      }

    }).start();
  }

  public static int calculateInSampleSize(
    BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      if (halfHeight > 0 && halfWidth > 0) {
        try {
          while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2;
          }
        } catch (ArithmeticException ex) {

        }
      }
    }

    return inSampleSize;
  }

  public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                       int reqWidth, int reqHeight) {

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(res, resId, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    try {
      return BitmapFactory.decodeResource(res, resId, options);
    } catch (Exception ex) {
      return null;
    }
  }
  //endregion

  //region Pager Adapter
  private class WalkthroughInternalPagerAdapter extends FragmentStatePagerAdapter {
    public WalkthroughInternalPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      WalkthroughContentFragment contentFragment = new WalkthroughContentFragment();
      contentFragment.setWalkthroughSection(walkthroughSectionList.get(position));
      return contentFragment;
    }

    @Override
    public int getCount() {
      return walkthroughSectionList.size();
    }
  }
  //endregion

  //region Helper Methods
  public static float convertDpToPx(Context context, float dp) {
    if (context == null) {
      return 0;
    }
    Resources res = context.getResources();

    return dp * (res.getDisplayMetrics().densityDpi / 160f);
  }
  //endregion
}
