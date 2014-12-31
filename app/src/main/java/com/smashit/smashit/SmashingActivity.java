package com.smashit.smashit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SmashingActivity extends Activity {
  private static final int MENU_HAMMER = 1;
  private static final int MENU_JIGSAW = 2;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate (savedInstanceState);
    setContentView (R.layout.activity_smashing);
  }

  /**
   * Invoked during init to give the Activity a chance to set up its Menu.
   *
   * @param menu the Menu to which entries may be added
   * @return true
   */
  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater ().inflate (R.menu.menu_main, menu);
    return true;

    /*super.onCreateOptionsMenu (menu);

    menu.add (0, MENU_HAMMER, 0, R.string.menu_hammer);
    menu.add (0, MENU_JIGSAW, 0, R.string.menu_jigsaw);
    return true;*/
  }

  /**
   * Invoked when the user selects an item from the Menu.
   *
   * @param item the Menu entry which was selected
   * @return true if the Menu item was legit (and we consumed it), false
   *         otherwise
   */
  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    int id = item.getItemId ();

    //noinspection SimplifiableIfStatement
    if (id == R.id.menu_hammer) {
    }
    else if (id == R.id.menu_jigsaw) {
    }

    return super.onOptionsItemSelected (item);

    /*switch (item.getItemId ()) {
      case MENU_HAMMER:
        return true;
      case MENU_JIGSAW:
        return true;
    }
    return false;*/
  }
}
