package com.applications.duckle.danz;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String SONG_NAME = "com.example.duckle.danz.MESSAGE";
    String songs[] = {"Chicken Dance", "Levels"};
    int songImages[] = {R.drawable.chickendance, R.drawable.levels};
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new HorizontalListViewFragment();
            fragments.add(fragment);
            ;
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    public void play(View v){
        Intent intent = new Intent(this, PreTutorial.class);
        String songName = null;
        for(Fragment f : fragments){
            HorizontalListViewFragment hlf = (HorizontalListViewFragment) f;
            if (hlf.selected_song != null){
                songName = hlf.selected_song;
            }
        }
        if (songName != null) {
            intent.putExtra(SONG_NAME, songName);
            startActivity(intent);
        }
    }
}
