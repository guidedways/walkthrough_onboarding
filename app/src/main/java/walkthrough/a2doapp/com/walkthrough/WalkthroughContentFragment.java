package walkthrough.a2doapp.com.walkthrough;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This represents a single section that contains a title and some description
 * Created by fahad on 22/09/2016.
 */

public class WalkthroughContentFragment extends Fragment {
  TextView txtTitle;
  TextView txtDescription;

  private WalkthroughSection section;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater
      .inflate(R.layout.fragment_walkthrough_slide, container, false);

    txtTitle = (TextView) rootView.findViewById(R.id.txtTitle);
    txtDescription = (TextView) rootView.findViewById(R.id.txtContent);

    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();

    updateUI();
  }

  public void setWalkthroughSection(WalkthroughSection section) {
    this.section = section;

    updateUI();
  }

  private void updateUI() {
    if (txtTitle != null && this.section != null) {
      txtTitle.setText(this.section.sectionTitle);
      txtDescription.setText(this.section.sectionDescription);
    } else if (txtTitle != null) {
      txtTitle.setText("");
      txtDescription.setText("");
    }
  }
}
