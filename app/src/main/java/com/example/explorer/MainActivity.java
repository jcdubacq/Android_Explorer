package com.example.explorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mButtonStartGame = findViewById(R.id.button);
        mButtonStartGame.setOnClickListener(view -> startGameActivity());
    }
    private void startGameActivity() {
        Intent intent;
        intent = new Intent(this, GameActivity.class);
        intent.putExtra("fileName", "test.json"); // Ajoutez votre nom de fichier ici
        startActivity(intent);
    }
}