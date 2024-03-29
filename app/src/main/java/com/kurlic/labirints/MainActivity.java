package com.kurlic.labirints;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.kurlic.labirints.Fragments.HowToPlayFragment;
import com.kurlic.labirints.Fragments.MainGameFragment;
import com.kurlic.labirints.Fragments.MyCommonFragment;
import com.kurlic.labirints.Fragments.SettingsFragment;
import com.kurlic.labirints.Fragments.UserStatisticFragment;
import com.kurlic.labirints.Notifications.RemindToPlayWorker;
import com.kurlic.labirints.view.Labyrinth.LabyrinthView;
import com.kurlic.labirints.web.LaunchService;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
        anrSolution();
        loadSettings();
        setLanguageFromSettings();

        setThemeFromSettings();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        firstLaunchActions();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.leftMenu);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        cleanLastActivityData();

        mainGameFragment = new MainGameFragment(this);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            searchFragmentAndReplace(R.id.fragmentContainer, mainGameFragment.uniqueTag, mainGameFragment, fragmentManager, fragmentTransaction);
            fragmentTransaction.commit();
        }

        connectServer();

        checkNotificationPermission();

        createReminderNotification();
    }

    void createReminderNotification() {
        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(RemindToPlayWorker.class, 6, TimeUnit.HOURS).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("LabyrinthReminder", ExistingPeriodicWorkPolicy.KEEP, periodicWork);
    }

    void anrSolution() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        } else {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        }
    }

    static final private String AppTag = "Kurlic.response";

    void connectServer() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://labyrinth-server-maven.onrender.com:443/").client(new OkHttpClient.Builder().build()).addConverterFactory(ScalarsConverterFactory.create()).build();

        LaunchService service = retrofit.create(LaunchService.class);

        Call<Integer> call = service.launch("abc");
        call.enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (!response.isSuccessful()) {
                    Log.e(AppTag, "bad response");
                } else {
                    Log.i(AppTag, "good response");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void firstLaunchActions() {
        if (!SharedData.getSettingsData().wasFirstLaunch()) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
    }


    public void setLocale(@NonNull Context context, String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    void setLanguageFromSettings() {
        String afterMap = SharedData.getSettingsData().getLanguageCode();
        if (afterMap == null) {
            return;
        }

        setLocale(this, afterMap);
        SharedData.getSettingsData().initSettings(this);
    }

    void setThemeFromSettings() {
        if (SharedData.getSettingsData().getTheme().equals(getResources().getString(R.string.optionLightTheme))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (SharedData.getSettingsData().getTheme().equals(getResources().getString(R.string.optionNightTheme))) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    String pathToSettings = "SettingsFile";
    String settingsDataKey = "SettingsKey";

    void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(pathToSettings, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(settingsDataKey, null);

        while (json != null) {
            Gson gson = new Gson();
            SettingsData settingsData = gson.fromJson(json, SettingsData.class);
            if (settingsData.getVersion() < 1) {
                break;
            }
            SharedData.setSettingsData(settingsData);
            settingsData.initSettings(this);
            return;
        }

        SharedData.setSettingsData(new SettingsData(this));
    }

    void saveSettings() {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(SharedData.getSettingsData());

            SharedPreferences sharedPreferences = getSharedPreferences(pathToSettings, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(settingsDataKey, json);
            editor.apply();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public LabyrinthView getLabyrinthView() {
        return labyrinthView;
    }

    public void setLabyrinthView(LabyrinthView labyrinthView) {
        this.labyrinthView = labyrinthView;
    }


    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, getString(R.string.onPermissionDeclined), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
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

    static MyCommonFragment previousFragment;
    static int r = -1;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (id == R.id.userStatisticItem) {
            if (userStatisticFragment == null) {
                userStatisticFragment = new UserStatisticFragment(this);
            }

            searchFragmentAndAdd(R.id.fragmentContainer, userStatisticFragment.uniqueTag, userStatisticFragment, fragmentManager, fragmentTransaction);
        }
        if (id == R.id.howToPlayItem) {
            if (howToPlayFragment == null) {
                howToPlayFragment = new HowToPlayFragment(this);
            }

            searchFragmentAndAdd(R.id.fragmentContainer, howToPlayFragment.uniqueTag, howToPlayFragment, fragmentManager, fragmentTransaction);
        }

        if (id == R.id.mainGameItem) {
            if (mainGameFragment == null) {
                mainGameFragment = new MainGameFragment(this);
            }
            searchFragmentAndReplace(R.id.fragmentContainer, mainGameFragment.uniqueTag, mainGameFragment, fragmentManager, fragmentTransaction);
        }

        if (id == R.id.settingsItem) {
            if (settingsFragment == null) {
                settingsFragment = new SettingsFragment(this);
            }
            searchFragmentAndAdd(R.id.fragmentContainer, settingsFragment.uniqueTag, settingsFragment, fragmentManager, fragmentTransaction);
        }

        r = 2;
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    void searchFragmentAndAdd(int containerId, String tag, MyCommonFragment fragment, @NonNull
    FragmentManager fragmentManager, FragmentTransaction fragmentTransaction) {
        MyCommonFragment previousThisTypeFragment = (MyCommonFragment) fragmentManager.findFragmentByTag(tag);
        if (previousThisTypeFragment == null) {
            fragmentTransaction.add(containerId, fragment, tag);
        } else {
            fragmentTransaction.show(previousThisTypeFragment);
            previousThisTypeFragment.onNavigationItemComeBack();
        }
        if (previousFragment != null && previousFragment != fragment) {
            previousFragment.onNavigationItemClicked();
            fragmentTransaction.hide(previousFragment).commit();
        }
        previousFragment = fragment;
        fragment.onEnter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }

    void searchFragmentAndReplace(int containerId, String tag, MyCommonFragment fragment, @NonNull
    FragmentManager fragmentManager, FragmentTransaction fragmentTransaction) {
        MyCommonFragment previousThisTypeFragment = (MyCommonFragment) fragmentManager.findFragmentByTag(tag);
        if (previousThisTypeFragment == null) {
            fragmentTransaction.replace(containerId, fragment, tag);
        } else {
            fragmentTransaction.show(previousThisTypeFragment);
            previousThisTypeFragment.onNavigationItemComeBack();
        }
        if (previousFragment != null && previousFragment != fragment) {
            previousFragment.onNavigationItemClicked();
            fragmentTransaction.hide(previousFragment).commit();
        }
        previousFragment = fragment;
        fragment.onEnter();
    }

    static MyCommonFragment previousActivityFragment;
    static AppCompatActivity previousActivity;

    void cleanLastActivityData() {
        previousFragment = null;
        previousActivity = this;
    }

    public static void cleanDataForIntent() {
        previousActivityFragment = previousFragment;
    }
}