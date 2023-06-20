package com.kurlic.labirints;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class SettingsData implements Serializable
{
    private String theme;
    private String language;
    private boolean firstLaunch = false;
    private int version = 2;

    String systemString = "sys";

    public SettingsData(@NonNull AppCompatActivity activity)
    {
        theme = systemString;
        language = systemString;
        wasFirstLaunch();
        initSettings(activity);
    }

    static Map<String, String> themeMap = new TreeMap<>();
    static Map<String, String> langMap = new TreeMap<>();
    void initSettings(@NonNull AppCompatActivity activity)
    {
        initThemeMap(activity);
        initLangMap(activity);
    }

    void initThemeMap(@NonNull AppCompatActivity activity)
    {
        getThemeMap().put("sys", activity.getString(R.string.optionSystemTheme)) ;
        getThemeMap().put("light", activity.getString(R.string.optionLightTheme)) ;
        getThemeMap().put("night", activity.getString(R.string.optionNightTheme)) ;
    }

    void initLangMap(@NonNull AppCompatActivity activity)
    {
        getLangMap().put("ru", activity.getString(R.string.russian));
        getLangMap().put("en", activity.getString(R.string.english));
        getLangMap().put("sys", activity.getString(R.string.systemic));
    }



    public Map<String, String> getLangMap()
    {
        return langMap;
    }

    public void setLangMap(Map<String, String> langMap)
    {
        this.langMap = langMap;
    }

    public Map<String, String> getThemeMap()
    {
        return themeMap;
    }

    public void setThemeMap(Map<String, String> themeMap)
    {
        this.themeMap = themeMap;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
    }

    public String getLanguage()
    {
        return langMap.get(language);
    }
    public String getLanguageCode()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        for (Map.Entry<String, String> entry : getLangMap().entrySet()) {
            if (entry.getValue().equals(language)) {
                this.language = entry.getKey();
                break;
            }
        }
    }

    public void changeLanguage(String theme)
    {
        this.theme = theme;
    }

    public boolean wasFirstLaunch()
    {
        return firstLaunch;
    }

    public void setFirstLaunch(boolean firstLaunch)
    {
        this.firstLaunch = firstLaunch;
    }

    public String getTheme()
    {
        return getThemeMap().get(theme);
    }

    public void setTheme(String theme)
    {
        for (Map.Entry<String, String> entry : getThemeMap().entrySet()) {
            if (entry.getValue().equals(theme)) {
                this.theme = entry.getKey();
                break;
            }
        }
    }

    public void changeTheme(String theme)
    {
        this.theme = theme;
    }
}
