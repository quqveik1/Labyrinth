package com.kurlic.labirints;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.kurlic.labirints.Fragments.HowToPlayFragment;
import com.kurlic.labirints.Fragments.MainGameFragment;
import com.kurlic.labirints.Fragments.MyCommonFragment;
import com.kurlic.labirints.Fragments.SettingsFragment;
import com.kurlic.labirints.Fragments.UserStatisticFragment;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MainGameFragment mainGameFragment;
    private UserStatisticFragment userStatisticFragment;

    private SettingsFragment settingsFragment;
    private HowToPlayFragment howToPlayFragment;
    private LabyrinthView labyrinthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            //getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.leftMenu);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        mainGameFragment = new MainGameFragment();

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            searchFragmentAndReplace(R.id.fragmentContainer, mainGameFragment.uniqueTag, mainGameFragment, fragmentManager, fragmentTransaction);
            fragmentTransaction.commit();
        }


    }

    public LabyrinthView getLabyrinthView()
    {
        return labyrinthView;
    }

    public void setLabyrinthView(LabyrinthView labyrinthView)
    {
        this.labyrinthView = labyrinthView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    MyCommonFragment previousFragment = null;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.userStatisticItem)
        {
            if(userStatisticFragment == null)
            {
                userStatisticFragment = new UserStatisticFragment();
            }

            searchFragmentAndAdd(R.id.fragmentContainer, userStatisticFragment.uniqueTag, userStatisticFragment, fragmentManager, fragmentTransaction);
        }
        if (id == R.id.howToPlayItem)
        {
            if(howToPlayFragment == null)
            {
                howToPlayFragment = new HowToPlayFragment();
            }

            searchFragmentAndAdd(R.id.fragmentContainer, howToPlayFragment.uniqueTag, howToPlayFragment, fragmentManager, fragmentTransaction);
        }

        if (id == R.id.mainGameItem)
        {
            if(mainGameFragment == null)
            {
                mainGameFragment = new MainGameFragment();
            }
            searchFragmentAndReplace(R.id.fragmentContainer, mainGameFragment.uniqueTag, mainGameFragment, fragmentManager, fragmentTransaction);
        }

        if (id == R.id.settingsItem)
        {
            if(settingsFragment == null)
            {
                settingsFragment = new SettingsFragment();
            }
            searchFragmentAndReplace(R.id.fragmentContainer, settingsFragment.uniqueTag, settingsFragment, fragmentManager, fragmentTransaction);
        }


        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    void searchFragmentAndAdd(int containerId, String tag, Fragment fragment, @NonNull FragmentManager fragmentManager, FragmentTransaction fragmentTransaction)
    {
        MyCommonFragment previousThisTypeFragment = (MyCommonFragment) fragmentManager.findFragmentByTag(tag);
        if(previousThisTypeFragment == null)
        {
            fragmentTransaction.add(containerId, fragment, tag);
        }
        else
        {
            fragmentTransaction.show(previousThisTypeFragment);
        }
        if(previousFragment != null && previousFragment != fragment)
        {
            previousFragment.onNavigationItemClicked();
            fragmentTransaction.hide(previousFragment);
        }
        previousFragment = (MyCommonFragment) fragment;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        
    }

    void searchFragmentAndReplace(int containerId, String tag, Fragment fragment, @NonNull FragmentManager fragmentManager, FragmentTransaction fragmentTransaction)
    {
        Fragment previousThisTypeFragment = fragmentManager.findFragmentByTag(tag);
        if(previousThisTypeFragment == null)
        {
            fragmentTransaction.replace(containerId, fragment, tag);
        }
        else
        {
            fragmentTransaction.show(previousThisTypeFragment);
        }
        if(previousFragment != null && previousFragment != fragment)
        {
            previousFragment.onNavigationItemClicked();
            fragmentTransaction.hide(previousFragment);
        }
        previousFragment = (MyCommonFragment) fragment;
    }
}