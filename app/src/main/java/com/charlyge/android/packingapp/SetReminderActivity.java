package com.charlyge.android.packingapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.charlyge.android.packingapp.Adapters.SetReminderListAdapter;
import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.Repository.AppRepository;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class SetReminderActivity extends AppCompatActivity implements SetReminderListAdapter.ItemClickedListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private EditText travelPurpose,destinationEdit,itemsEdit,travelModeEdit;
    private Button activatePacking_bt;
    private ImageView addItems;
    private FirebaseFirestore firebaseFirestore;
    private EditText travelDate,travelTime;
    private ImageView pickDes;
    AppRepository appRepository;
    ImageView bus,airplane,bike,car;
    public static final String TAG = SetReminderActivity.class.getSimpleName();
    private List<Items> itemsList = new ArrayList<>();
    private SetReminderListAdapter setReminderListAdapter;
    private String date,time;
    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyyyhh:mm";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private int ADD_PLACE_REQUEST_CODE = 89;
    private int PLACE_PICKER_REQUEST = 987;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private Date dateNew;
    private String travelMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        appRepository = new AppRepository(this.getApplication());
        RecyclerView recyclerView = findViewById(R.id.items_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        setReminderListAdapter = new SetReminderListAdapter(this,this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(setReminderListAdapter);
        travelTime =  findViewById(R.id.travel_time_edit);
        travelPurpose =findViewById(R.id.travel_purpose_edit);
        destinationEdit = findViewById(R.id.edit_destination);
        travelDate = findViewById(R.id.travel_date_edit);
        itemsEdit = findViewById(R.id.add_travel_items_edit);
        addItems =findViewById(R.id.add_item_bt);
        activatePacking_bt = findViewById(R.id.set_reminder_bt);
        pickDes = findViewById(R.id.place_picker);
        bus = findViewById(R.id.public_bus);
        car = findViewById(R.id.private_car);
        airplane = findViewById(R.id.airplane);
        bike = findViewById(R.id.bike);
    travelDate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(SetReminderActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            travelDate.setText(dayOfMonth + "/"
                                    + (month + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
    });
        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String purpose = travelPurpose.getText().toString();
               String items =  itemsEdit.getText().toString();
                String date = travelDate.getText().toString();
                String time = travelTime.getText().toString();
                String dateTime = date+time;
                if(TextUtils.isEmpty(items)){
                    Toast.makeText(SetReminderActivity.this, "Items cannot be Empty ", Toast.LENGTH_SHORT).show();
                  return;
                }
                Timber.i(dateTime);
                try {
                    dateNew = dateFormat.parse(dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final Items items1 = new Items(purpose,dateNew,items,false);
                itemsList.add(items1);
                setReminderListAdapter.setItemsList(itemsList);
                itemsEdit.setText("");



            }
        });
          pickDes.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(ActivityCompat.checkSelfPermission(SetReminderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                          PackageManager.PERMISSION_GRANTED){
                      //Request Permsssion
                      ActivityCompat.requestPermissions(SetReminderActivity.this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},ADD_PLACE_REQUEST_CODE);
                  }
                  else {
                      getPlaceInfo();
                  }
              }
          });
        activatePacking_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String destination = destinationEdit.getText().toString();
                String purpose = travelPurpose.getText().toString();
                String date = travelDate.getText().toString();
                String time = travelTime.getText().toString();
                String dateTime = date+time;
                Timber.i(dateTime);
                 if(itemsList.size()==0 || TextUtils.isEmpty(purpose) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(destination)){
                    Toast.makeText(SetReminderActivity.this, "Please Enter all Fields ", Toast.LENGTH_SHORT).show();

                 }else if(TextUtils.isEmpty(travelMode)){
                     Toast.makeText(SetReminderActivity.this, " select Travel Mode ", Toast.LENGTH_SHORT).show();


                 }
                 else {
                     try {
                         dateNew = dateFormat.parse(dateTime);

                     } catch (ParseException e) {
                         e.printStackTrace();
                     }
                     Timber.i(dateFormat.format(dateNew));

                     final PackingReminder packingReminder = new PackingReminder(destination,travelMode,purpose,dateNew,4);
                     AppExecutors.getInstance().diskIO().execute(new Runnable() {
                         @Override
                         public void run() {

                             for (int i = 0 ; i<itemsList.size();i++){
                                 Items items = itemsList.get(i);
                              appRepository.insertItems(items);
                             }

                              appRepository.insertPackingRm(packingReminder);
                         }
                     });

                     firebaseFirestore.collection("UsersTrips").document(mFirebaseUser.getUid())
                    .collection("Trips").add(packingReminder).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Timber.tag("FirebaseUpload").d("Online sync susesscesful");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Timber.d(e.getLocalizedMessage());
                }
            });
//
//                             firebaseFirestore.collection("UsersTrips").document("UserId")
//                                     .collection("TripsItems").add(itemsList);

                 }

                finish();
            }

        });

        travelTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(SetReminderActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        travelTime.setText(hourOfDay + ":" + minutes);
                    }
                }, currentHour, currentMinute, false);
                timePickerDialog.show();

            }
        });

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this,this)
                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Timber.i(TAG, "API Client Connection Successful!");

    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.i(TAG, "API Client Connection Suspended!");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e(TAG, "API Client Connection Failed!");

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode ==ADD_PLACE_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                getPlaceInfo();
            }
            else {
                boolean showRationale = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                }
                if (!showRationale) {
                    //When the user has checked on do not ask me again
                    Intent lauchPermissionScreen = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", getPackageName(), null));
                    lauchPermissionScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(lauchPermissionScreen);
                    finish();
                } else {
                    Toast.makeText(this, "Accept permission to Open Map", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ADD_PLACE_REQUEST_CODE);
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data ==null){
            return;
        }
        if(requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK){
            Place place = PlacePicker.getPlace(this, data);
            if (place == null) {
                Timber.i("No place selected");
                return;
            }
            destinationEdit.setText(place.getName());
        }
    }


    private void getPlaceInfo() {

        PlacePicker.IntentBuilder builder =
                new PlacePicker.IntentBuilder();
        try {
            // Launch the PlacePicker.
            startActivityForResult(builder.build(SetReminderActivity.this)
                    , PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnItemClicked(int Id) {
      itemsList.remove(Id);
      setReminderListAdapter.setItemsList(itemsList);
    }


    public void selectBike(View view) {
        travelMode = "Bike";
        bike.setBackgroundColor(getResources().getColor(R.color.darkGreen));

        airplane.setBackgroundColor(getResources().getColor(R.color.white));
        bus.setBackgroundColor(getResources().getColor(R.color.white));
        car.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void selectCar(View view) {
        travelMode = "Car";
        car.setBackgroundColor(getResources().getColor(R.color.darkGreen));

        airplane.setBackgroundColor(getResources().getColor(R.color.white));
        bus.setBackgroundColor(getResources().getColor(R.color.white));
        bike.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void selectBus(View view) {
        travelMode = "Bus";
        bus.setBackgroundColor(getResources().getColor(R.color.darkGreen));

        airplane.setBackgroundColor(getResources().getColor(R.color.white));
        bike.setBackgroundColor(getResources().getColor(R.color.white));
        car.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void selectAirplane(View view) {
        travelMode = "Airplane";
        airplane.setBackgroundColor(getResources().getColor(R.color.darkGreen));

        bike.setBackgroundColor(getResources().getColor(R.color.white));
        bus.setBackgroundColor(getResources().getColor(R.color.white));
        car.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
