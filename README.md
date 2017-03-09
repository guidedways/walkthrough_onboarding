# Walkthrough / Onboarding Tutorial

![alt tag](https://github.com/guidedways/walkthrough_onboarding/blob/master/example.gif)

This is a generic Walkthrough / Onboarding Tutorial activity for Android apps. The screen has two parts to it: a screen shot of your app, and some description below.

Instead of a typical parallax sliding screen, the activity presents you with screenshots of your app inside of a phone template, thus making it as simple as taking screenshots of your existing app and adding those to this project. 

You can construct each *WalkThrough Section* and define how you would like it to appear on screen. The embedded screenshot can move from left to right, top to bottom and have zoom applied to it. In code, it's as simple as:

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
[![Twitter Follow](https://img.shields.io/twitter/url/http/twitter.com/2DoApp.svg?style=social)](https://twitter.com/2DoApp)

