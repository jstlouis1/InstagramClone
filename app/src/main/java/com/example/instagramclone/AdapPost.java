package com.example.instagramclone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import java.util.List;

public class AdapPost extends RecyclerView.Adapter<AdapPost.ViewHolder>{

    public static final String TAG = "PostAdapter";
    public static Context context;
    List<PostObject> postObjects;

    public AdapPost(Context context1, List<PostObject> postObjects) {
        this.context = context1;
        this.postObjects = postObjects;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostObject postObject = postObjects.get(position);
        holder.bind(postObject);
    }

    @Override
    public int getItemCount() {
        return postObjects.size();
    }

    // Method to clean all elements of the recycler
    public void clear(){
        postObjects.clear();
        notifyDataSetChanged();
    }

    // Method to add a list of Posts -- change to type used
    public void addAll(List<PostObject> postObjectList){
        postObjects.addAll(postObjectList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView ivPost;
        TextView txtvUsername, txtvDescription;
        ImageButton imgbtnheart, imgbtncomment, imgbtnsend, imgbtnsave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPost = itemView.findViewById(R.id.ivPost);
            txtvUsername = itemView.findViewById(R.id.txtvUsername);
            txtvDescription = itemView.findViewById(R.id.txtvDescription);
            imgbtnheart = itemView.findViewById(R.id.imgbtnheart);
            imgbtncomment = itemView.findViewById(R.id.imgbtncomment);
            imgbtnsend = itemView.findViewById(R.id.imgbtnsend);
            imgbtnsave = itemView.findViewById(R.id.imgbtnsave);
        }

        public void bind(PostObject postObject){
            txtvUsername.setText(postObject.getUser().getUsername());
            txtvDescription.setText(postObject.getDescription());

            ParseFile image = postObject.getImage();
            if(image != null){
                Glide.with(context).load(image.getUrl()).into(ivPost);
            }
        }






    }
}
