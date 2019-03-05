package com.charlyge.android.packingapp;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.charlyge.android.packingapp.Adapters.PackingRemindersAdapter;
import com.charlyge.android.packingapp.AppWidget.WidgetService;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.Repository.AppRepository;
import com.charlyge.android.packingapp.ViewModel.PackingReminderViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements PackingRemindersAdapter.OnShareIconClickListener, PackingRemindersAdapter.OnLocationIconClickListener {
    private RecyclerView recyclerView;
    private AppRepository appRepository;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private int RC_SIGN_IN = 898;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mFirebaseAuth = FirebaseAuth.getInstance();
         mFirebaseUser = mFirebaseAuth.getCurrentUser();
        final LinearLayout noItem = findViewById(R.id.no_item);
        if (Build.VERSION.SDK_INT > 25) {
            //Start the widget service to update the widget
            WidgetService.StartWidgetServiceO(this);
        } else {

            WidgetService.StartWidgetService(this);
        }
        FloatingActionButton packingBt = findViewById(R.id.activate_packing_reminder);

        packingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFirebaseUser==null){
                    startSignIn();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, SetReminderActivity.class);
                startActivity(intent);
            }
        });
        appRepository = new AppRepository(this.getApplication());
        recyclerView = findViewById(R.id.recycler_view_reminder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        PackingReminderViewModel packingReminderViewModel = ViewModelProviders.of(MainActivity.this).get(PackingReminderViewModel.class);

        packingReminderViewModel.getPackingReminderList().observe(MainActivity.this, new Observer<List<PackingReminder>>() {
            @Override
            public void onChanged(@Nullable List<PackingReminder> packingReminders) {
                if (packingReminders == null) {
                    noItem.setVisibility(View.VISIBLE);
                    ImageView imageViewAnimate = findViewById(R.id.car_rotate);
                    AnimatorSet mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator
                            (MainActivity.this, R.animator.rotate);
                    mRotateAnim.setTarget(imageViewAnimate);
                    mRotateAnim.start();
                    return;
                }
                if (packingReminders.size() == 0) {
                    noItem.setVisibility(View.VISIBLE);
                    ImageView imageViewAnimate = findViewById(R.id.car_rotate);
                    AnimatorSet mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator
                            (MainActivity.this, R.animator.rotate);
                    mRotateAnim.setTarget(imageViewAnimate);
                    mRotateAnim.start();
                    return;
                }
                noItem.setVisibility(View.GONE);
                final PackingRemindersAdapter adapter = new PackingRemindersAdapter(packingReminders, MainActivity.this, MainActivity.this, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });


        checkDatabase(this);
    }

    private void startSignIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, SetReminderActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
        }
    }
    public void checkDatabase(final Context context) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<PackingReminder> packingReminderList = appRepository.getAllPackingNoObserveRm();
                if (packingReminderList.size() == 0) {
                    // Stop job Scheduler
                    return;
                }
                ReminderUtilities.schedulePackingReminder(context);
            }
        });


    }

    private void openLocationInMap(String addressString) {
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Timber.d("Couldn't call " + geoLocation.toString()
                    + ", no receiving apps installed!");
        }
    }


    @Override
    public void onSharedIconClicked(final String shareMsg, final String Destination) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Hello Friends I will be travelling to " + Destination + " Check out My PackList:- " + "\n" + shareMsg);
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });

    }

    @Override
    public void onLocationIconClicked(String Destination) {
        openLocationInMap(Destination);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.packlist_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.packlist_history) {
            if(mFirebaseUser==null){
                startSignIn();
            }
            else {
                Intent intent = new Intent(this,PackingHistoryActivity.class);
                startActivity(intent);

            }




            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
