package com.example.explorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class GameActivity extends AppCompatActivity {
    private JSONObject data;
    private int location = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        String fileName = getIntent().getStringExtra("fileName");
        String fileNameWithoutExtension = fileName.replaceAll("\\.json$", "");
        Resources res = this.getResources();
        int sourcefile = res.getIdentifier(fileNameWithoutExtension, "raw", this.getPackageName());
        String worldtitle="";
        int startLocation = 0;
        InputStream inputStream = getResources().openRawResource(sourcefile);
        Scanner scanner = new Scanner(inputStream);
        String jsonString = scanner.useDelimiter("\\A").next();
        try {
            data = new JSONObject(jsonString);
            worldtitle = data.getString("title");
            startLocation = data.getInt("start");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String newtitle = String.format(getString(R.string.app_title_name),getString(R.string.app_name),worldtitle);
        setTitle(newtitle);
        setLocation(startLocation);
    }
    protected void setLocation(int newLoc) {
        location = newLoc;
        try {
            JSONObject locationObject = data.getJSONArray("places").getJSONObject(location);
            TextView locationTitleTextView = findViewById(R.id.locationName);
            locationTitleTextView.setText(locationObject.getString("name"));
            TextView locationDescTextView = findViewById(R.id.locationDesc);
            locationDescTextView.setText(locationObject.getString("desc"));
            LinearLayout buttonsContainer = findViewById(R.id.buttons_container);
            ImageView locationImage = findViewById(R.id.locationImage);
            if (locationObject.has("image")) {
                locationImage.setVisibility(View.VISIBLE);
                int resourceId = getResources().getIdentifier(locationObject.getString("image"), "drawable", getPackageName());
                Log.d(this.getResources().getString(R.string.app_name),String.format("Image %s",locationObject.getString("image")));
                Log.d(this.getResources().getString(R.string.app_name),String.format("ID %d",resourceId));
                locationImage.setImageResource(resourceId);
            } else{
                locationImage.setVisibility(View.GONE);
            }
            locationImage.invalidate();
            JSONArray actions = locationObject.getJSONArray("actions");
            buttonsContainer.removeAllViews();
            for (int i = 0; i < actions.length(); i++) {
                JSONObject action = actions.getJSONObject(i);
                String actionName = action.getString("action_name");
                Button button = new Button(this);
                button.setText(actionName);
                int next = action.getInt("next");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setLocation(next);
                    }
                });;
                buttonsContainer.addView(button);
            }
            Boolean isFinal = locationObject.getBoolean("final");
            if (isFinal) {
                Button button = new Button(this);
                button.setText(getString(R.string.won_game));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });;
                buttonsContainer.addView(button);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}