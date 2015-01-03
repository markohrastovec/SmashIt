package com.smashit.smashit;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class SmashingActivity extends Activity {
  private DrawerLayout drawerLayout;
  private ListView weaponsList;
  private String[] weapons;
  private CharSequence drawerTitle;
  private CharSequence menuTitle;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate (savedInstanceState);
    setContentView (R.layout.activity_smashing);

    weapons = getResources ().getStringArray (R.array.weapons_array);
    drawerLayout = (DrawerLayout)findViewById (R.id.drawer_layout);
    weaponsList = (ListView)findViewById (R.id.weapons_drawer);

    weaponsList.setAdapter (new ArrayAdapter<String> (this, R.layout.drawer_list_item, weapons));
    weaponsList.setOnItemClickListener (new DrawerItemClickListener ());
  }

  private class DrawerItemClickListener implements ListView.OnItemClickListener {
    @Override
    public void onItemClick (AdapterView parent, View view, int position, long id) {
      selectItem (position);
    }
  }

  /**
   * Swaps fragments in the main content view
   */
  private void selectItem (int position) {
    // Create a new fragment and specify the planet to show based on position
    /*Fragment fragment = new PlanetFragment ();
    Bundle args = new Bundle ();
    args.putInt (PlanetFragment.ARG_PLANET_NUMBER, position);
    fragment.setArguments (args);

    // Insert the fragment by replacing any existing fragment
    FragmentManager fragmentManager = getFragmentManager ();
    fragmentManager.beginTransaction ()
            .replace (R.id.content_frame, fragment)
            .commit ();*/

    // Highlight the selected item, update the title, and close the drawer
    weaponsList.setItemChecked (position, true);
    setTitle (weapons[position]);
    drawerLayout.closeDrawer (weaponsList);
  }

  @Override
  public void setTitle (CharSequence title) {
    menuTitle = title;
    //getActionBar ().setTitle (menuTitle);
  }
}
