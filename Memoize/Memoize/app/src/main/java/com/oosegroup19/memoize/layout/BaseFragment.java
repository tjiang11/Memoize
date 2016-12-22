package com.oosegroup19.memoize.layout;

import android.app.Fragment;

/**
 * Created by smsukardi on 11/12/16.
 */

/** Class that extends baseFragment which ensures that there
 * is a getFragmentName method for tabbing.
 */
public abstract class BaseFragment extends Fragment {
    public abstract String getFragmentName();
}

