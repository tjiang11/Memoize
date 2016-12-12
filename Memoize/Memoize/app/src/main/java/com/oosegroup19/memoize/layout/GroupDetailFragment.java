package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oosegroup19.memoize.R;

import com.oosegroup19.memoize.structures.GroupItem;

/**
 * Created by Tony on 12/11/2016.
 */

public class GroupDetailFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "GroupDetailFragment";
    private final String fragmentName = FRAGMENTNAME;

    private static GroupItem groupItem;

    public GroupDetailFragment() {
        // Required empty public constructor
    }

    public static GroupDetailFragment newInstance(GroupItem loc) {
        GroupDetailFragment fragment = new GroupDetailFragment();
        groupItem = loc;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_detail, container, false);

        // TODO: Figure out why doesn't this work?
        // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        TextView groupName = (TextView) view.findViewById(R.id.group_name_detail);
        TextView groupDescription = (TextView) view.findViewById(R.id.group_description_detail);

        groupName.setText(groupItem.getName());
        groupDescription.setText(groupItem.getDescription());

        GroupItem temp = (GroupItem) groupItem;
        groupName.setText("Latitude: " + String.valueOf(temp.getName()));
        groupDescription.setText("Longitude: " + String.valueOf(temp.getDescription()));


        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupFragment fragment = new GroupFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public String getFragmentName() {
        return this.fragmentName;
    }
}
