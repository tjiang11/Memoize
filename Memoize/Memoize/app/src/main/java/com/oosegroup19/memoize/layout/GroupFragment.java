package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.activity.HomePageActivity;
import com.oosegroup19.memoize.layout.dummy.DummyContent;
import com.oosegroup19.memoize.layout.dummy.DummyContent.DummyItem;

import com.oosegroup19.memoize.structures.GroupItem;
import com.oosegroup19.memoize.structures.GroupItemAdapter;
import com.oosegroup19.memoize.structures.User;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class GroupFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "GroupFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance Fields
    private ListView groupList;
    protected static ArrayList<GroupItem> groupItems;
    protected static GroupItemAdapter aa;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GroupFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GroupFragment newInstance(User user) {
        GroupFragment fragment = new GroupFragment();
        //Bundle args = new Bundle();
        //args.putInt(ARG_COLUMN_COUNT, columnCount);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        getActivity().setTitle("Reminder Log");

        // dbAdapt = new ListDatabaseAdapter(getActivity());
        // dbAdapt.open();

        groupList = (ListView) view.findViewById(R.id.groupItemList);
        // create ArrayList of courses from database
        groupItems = new ArrayList<GroupItem>();

        // make array adapter to bind arraylist to listview with new custom item layout
        aa = new GroupItemAdapter(this.getActivity(), R.layout.group_list_contents, groupItems);
        groupList.setAdapter(aa);

        //updateArray();

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupItem groupItem = (GroupItem) parent.getItemAtPosition(position);

                //TODO: Send item to ReminderDetailFragment
                GroupDetailFragment fragment = GroupDetailFragment.newInstance(groupItem);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        Log.i("ReminderLogFrag", "attempting networking request...");

        //now, listview is bound to user's array data
        AndroidNetworking.get(HomePageActivity.baseURL + "/users/1/subgroups/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        GroupItem[] myGroupItems = gson.fromJson(response, GroupItem[].class);

                        for (GroupItem groupItem : myGroupItems) {
                            Log.i("GroupFrag", groupItem.toString());
                            aa.add(groupItem);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "noooooo");
                        Log.e("tag", anError.getMessage());
                    }
                });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    public String getFragmentName() {
        return this.fragmentName;
    }
}
