package walkthrough.a2doapp.com.walkthrough;

import android.support.annotation.DrawableRes;

/**
 * Created by fahad on 22/09/2016.
 */ //region Walkthrough Representation
public class WalkthroughSection {
  public String sectionTitle;
  public String sectionDescription;
  public @DrawableRes int sectionShotName;

  // Dynamic effects
  public int prolongZoomIn;
  public int prolongZoomOut;
  public int prolongMovingUp;
  public int prolongMovingDown;

  /**
   * 0 = just below bottom container rim, 100 = bottom of phone touching rim
   */
  public int sectionDeviceVerticalPercToShow;

  /**
   * 0 = screen center / normal, 100 = phone container's left edge touching Vertical center axis, -100 = phone's right edge touching center axis
   */
  public int sectionDeviceHorizontalPercToShow;

  /**
   * 0 = normal, 100 = twice as large
   */
  public int sectionDeviceZoomPercToShow;
}
