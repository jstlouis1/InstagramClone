package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import java.io.File;
import com.parse.SaveCallback;


public class Compose extends Fragment {

    public static final String TAG = "Compose";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File file;
    public String photoFileName = "photo.jpg";
    private EditText etDescription;
    private Button TakePhoto, btnpost;
    private ImageView ivPhoto;
    private ProgressBar progressBar;
    private TextInputLayout description_text_input;


    public Compose(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescription = view.findViewById(R.id.etDescription);
        description_text_input = view.findViewById(R.id.description_text_input);
        TakePhoto = view.findViewById(R.id.btnTakePicture);
        btnpost = view.findViewById(R.id.btnSubmit);
        ivPhoto = view.findViewById(R.id.ivPicture);
        progressBar = view.findViewById(R.id.pbLoading);

        // click to launch the camera
        TakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
                Toast.makeText(getContext(), "Take", Toast.LENGTH_SHORT).show();
            }
        });

        //click on submit button
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                SavePost(description, currentUser, file);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                btnpost.setVisibility(View.VISIBLE);
                TakePhoto.setVisibility(View.INVISIBLE);
                description_text_input.setVisibility(View.VISIBLE);
                ivPhoto.setVisibility(View.VISIBLE);
                ivPhoto.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Error taking picture", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // method to launch the camera
    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        file = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    // method to save a post
    private void SavePost(String description, ParseUser currentUser, File photoFile) {
        progressBar.setVisibility(ProgressBar.VISIBLE); // show the progressBar
        PostObject postObject = new PostObject();
        postObject.setDescription(description);
        postObject.setImage(new ParseFile(photoFile));
        postObject.setUser(currentUser);

        postObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE); // hide the progressBar
                    return;
                }
                if (photoFile == null || ivPhoto.getDrawable() == null){
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE); // hide the progressBar
                    return;
                }
                Toast.makeText(getContext(), "Post save as successful!!", Toast.LENGTH_SHORT).show();
                etDescription.setText("");
                ivPhoto.setImageResource(0);
                progressBar.setVisibility(ProgressBar.INVISIBLE); // hide the progressBar
                btnpost.setVisibility(View.INVISIBLE);
                TakePhoto.setVisibility(View.VISIBLE);
                description_text_input.setVisibility(View.INVISIBLE);
                ivPhoto.setVisibility(View.INVISIBLE);
            }
        });
    }

}