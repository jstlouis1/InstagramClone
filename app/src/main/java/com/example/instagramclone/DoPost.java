package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class DoPost extends Fragment {

    public static final String TAG = "DoPost";
    protected RecyclerView rvPost;
    protected List<PostObject> postObjectList;
    protected AdapPost adapter;
    private Context context;

    public DoPost(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.do_post, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPost = view.findViewById(R.id.rvPost);
        postObjectList = new ArrayList<>();

        // adapter created
        adapter = new AdapPost(getContext(), postObjectList);

        //put the adapter on the recyclerView
        rvPost.setAdapter(adapter);
        rvPost.setLayoutManager(new LinearLayoutManager(context));

        queryPost();
    }

    protected void queryPost() {
        ParseQuery<PostObject> query = ParseQuery.getQuery(PostObject.class);
        query.include(PostObject.USER);
        query.findInBackground(new FindCallback<PostObject>() {
            @Override
            public void done(List<PostObject> postObjects, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting Posts", e);
                    Toast.makeText(getContext(), "Issue with getting Posts", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (PostObject postObject : postObjects){
                    Log.i(TAG, "Post: " + postObject.getDescription() + ", username: " + postObject.getUser().getUsername());
                }

                postObjectList.addAll(postObjects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}