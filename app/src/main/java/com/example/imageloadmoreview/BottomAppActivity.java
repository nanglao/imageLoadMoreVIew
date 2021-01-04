package com.example.imageloadmoreview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class BottomAppActivity extends AppCompatActivity {
    protected BottomAppBar bar;
    protected CoordinatorLayout coordinatorLayout;
    protected FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_app_test);

        Button centerButton = findViewById(R.id.center);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        fab = findViewById(R.id.fab);
        bar = findViewById(R.id.bar);
        Button endButton = findViewById(R.id.end);

        centerButton.setOnClickListener(
                v -> {
                    bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                });
        endButton.setOnClickListener(
                v -> {
                    bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                });
        fab.setOnClickListener(v -> showSnackbar(fab.getContentDescription()));
    }
    private void showSnackbar(CharSequence text) {
        Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT)
                .setAnchorView(fab.getVisibility() == View.VISIBLE ? fab : bar)
                .show();
    }
}