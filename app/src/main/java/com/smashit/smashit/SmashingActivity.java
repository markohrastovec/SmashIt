package com.smashit.smashit;

import android.app.Activity;
import android.os.Bundle;

import com.smashit.smashit.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SmashingActivity extends Activity {

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate (savedInstanceState);

    setContentView (R.layout.activity_smashing);
  }
}
