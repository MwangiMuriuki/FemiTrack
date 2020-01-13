package com.example.a001759.pregtrack.models;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class ModelNavigationDrawer {

    String item_name;
    int image_resource;
    String image_url;
    boolean activity;
    boolean fragment;
    Class activityName;
    Fragment fragmentName;
    List<ModelNavigationDrawer> subMenus = new ArrayList<>();

    public ModelNavigationDrawer() {
    }

    public ModelNavigationDrawer(String item_name, int image_resource, String image_url, boolean activity,
                                 boolean fragment, Class activityName, Fragment fragmentName, List<ModelNavigationDrawer> subMenus) {
        this.item_name = item_name;
        this.image_resource = image_resource;
        this.image_url = image_url;
        this.activity = activity;
        this.fragment = fragment;
        this.activityName = activityName;
        this.fragmentName = fragmentName;
        this.subMenus = subMenus;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public void setImage_resource(int image_resource) {
        this.image_resource = image_resource;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    public boolean isFragment() {
        return fragment;
    }

    public void setFragment(boolean fragment) {
        this.fragment = fragment;
    }

    public Class getActivityName() {
        return activityName;
    }

    public void setActivityName(Class activityName) {
        this.activityName = activityName;
    }

    public Fragment getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(Fragment fragmentName) {
        this.fragmentName = fragmentName;
    }

    public List<ModelNavigationDrawer> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<ModelNavigationDrawer> subMenus) {
        this.subMenus = subMenus;
    }
}
