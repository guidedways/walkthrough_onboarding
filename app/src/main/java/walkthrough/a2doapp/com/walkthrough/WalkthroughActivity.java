package walkthrough.a2doapp.com.walkthrough;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WalkthroughActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Fragment fragment = getSupportFragmentManager().findFragmentByTag("walkthroughFragment");
    if (fragment == null) {
      getSupportFragmentManager().beginTransaction()
        .replace(android.R.id.content, new WalkthroughActivityFragment(), "walkthroughFragment")
        .commit();
    }
  }
}
