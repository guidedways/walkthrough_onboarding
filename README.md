# Walkthrough / Onboarding Tutorial

![alt tag](https://github.com/guidedways/walkthrough_onboarding/blob/master/example.gif)

This is a generic Walkthrough / Onboarding Tutorial activity for Android apps. The screen has two parts to it: a screen shot of your app, and some description below. Demo it live in [2Do](https://play.google.com/store/apps/details?id=com.guidedways.android2do&hl=en).

# Why another walkthrough project?

Most onboarding projects out there rely on some pretty icons, colors and a bit of parallax. This is fine, except you miss out on the ability to actually showcase your app. 

If your app is constantly improving, and you find yourself adding new features often, it becomes a burden to design and add new icons & colors to your onboarding view. With this project, all you do is take real screenshots of your app, create a new walk through section, as described below, and you're done. The activity will present you with screenshots of your app inside of a phone template, and you describe how you want these to transition and appear on screen.


## Requirements

- Android 5.0 Lollipop (API lvl 21) or greater (mainly since the project uses SVG drawables; replacing with PNGs would lower this down to API 14)
- Your favorite IDE


## How to customize

In `WalkthroughActivityFragment`, navigate to the `initSections()` method and define your sections there.

You simple construct a `WalkthroughSection` and define how you would like it to appear on screen. The embedded screenshot can move from left to right, top to bottom and have zoom applied to it. The image then accompanies a title and some description. In code, it's as simple as:

```
    WalkthroughSection section = new WalkthroughSection();
    
    /* Set the image, title and desription to show */
    section.sectionShotName = R.drawable.tut_inbox;
    section.sectionTitle = "Native Inbox for Getting Things Done®";
    section.sectionDescription = "Fancy David Allen's GTD® methodology? Enable the special Inbox to serve as your default collection list from Settings > Default Collection List.";
    
    /* 0 = just below bottom container rim, 100 = bottom of phone touching rim */
    section.sectionDeviceVerticalPercToShow = 40;
    
    /* 0 = screen center / normal, 100 = phone container's left edge touching Vertical center axis, -100 = phone's right edge touching center axis */
    section.sectionDeviceHorizontalPercToShow = 0;
    
    /* 0 = normal size, 100 = twice as large */
    section.sectionDeviceZoomPercToShow = 0;
    
    /* Added motion effect of slowly moving upwards*/
    section.prolongMovingUp = 28;
    
    ...
    
    walkthroughSectionList.add(section)
```

# **Output**
![alt tag](https://github.com/guidedways/walkthrough_onboarding/blob/master/demo.png)

## Licence
Walkthrough Onboarding is released under the MIT license.
See [LICENSE](./LICENSE) for details.

## Follow Us
[![Twitter URL](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=https://github.com/guidedways/walkthrough_onboarding)
[![Twitter Follow](https://img.shields.io/twitter/follow/2DoApp.svg?style=social&label=Follow)](https://twitter.com/2DoApp)

