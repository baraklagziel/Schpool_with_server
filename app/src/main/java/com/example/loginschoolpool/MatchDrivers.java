package com.example.loginschoolpool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.junit.internal.runners.statements.Fail;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MatchDrivers extends AppCompatActivity {
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String value;
    private ImageView imageView;
    private ArrayList<StudentMember> matchStudents = new ArrayList<>();
    private List<StudentMember> allStudentsFromDatabase = new ArrayList<>();
    private Intent routeActivityIntent;
    private TextView title;
    private TextView subtitle;
    private TextView km;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.match_driver_activity);
        imageView = (ImageView) findViewById(R.id.imageView_user);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        routeActivityIntent = getIntent();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("StudentMembers");
        title = (TextView) findViewById(R.id.textView_title);
        subtitle = (TextView)findViewById(R.id.textView_subTitle);
        km = (TextView)findViewById(R.id.textView_km);

        subtitle.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        readStudentMemberFromDatabase(routeActivityIntent);



    }

    private void readStudentMemberFromDatabase(Intent routeActivityIntent) {
        final String startLocation = routeActivityIntent.getStringExtra("SET_HOME_LOCATION");
        final String targetLocation = routeActivityIntent.getStringExtra("SET_SCHOOL_LOCATION");
        int year = routeActivityIntent.getIntExtra("SET_YEAR", 5);
        int month = routeActivityIntent.getIntExtra("SET_MONTH", 5);
        int dayOfMonth = routeActivityIntent.getIntExtra("SET_DAY", 5);
        int hour = routeActivityIntent.getIntExtra("SET_HOUR", 5);
        int minute = routeActivityIntent.getIntExtra("SET_MINUTE", 5);
        final Calendar calenderAvailabilityInput = new GregorianCalendar(year, month, dayOfMonth, hour, minute);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<StudentMember> allStudentsMembers = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        allStudentsMembers.add(snapshot.getValue(StudentMember.class));
                    } catch (DatabaseException e) {
                        Log.d("problem key", dataSnapshot.getKey());
                    }
                }
                allStudentsFromDatabase = allStudentsMembers;
                Log.d("mytag", "Insert all students members");
                try {
                    findMatchDrivers(allStudentsMembers, calenderAvailabilityInput, targetLocation);
                    //getDataFromFirebase(matchStudents);
                    Log.d("myTag", "successes");
                } catch (Exception e) {
                    String errorMessage = "Cant make find match Drivers";
                    Log.d("myTag", errorMessage);

                }
            }


            private void getDataFromFirebase(ArrayList<StudentMember> matchStudents) {
                //allStudentsFromDatabase = allStudentsMembers;
                RecyclerView recyclerViewOfStudentsMembers = (RecyclerView) findViewById(R.id.recyclerView_result);
                RecycleViewAdapter myAdapter = new RecycleViewAdapter(getApplicationContext(), matchStudents);
                recyclerViewOfStudentsMembers.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                recyclerViewOfStudentsMembers.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("myTag", "Fail to insert students");
            }

        });
    }


    private void findMatchDrivers(ArrayList<StudentMember> allStudentsMembers, Calendar calenderAvailability, String targetLocation) throws IOException {
        Log.d("print students Members", Arrays.toString(allStudentsFromDatabase.toArray()));
        this.allStudentsFromDatabase = allStudentsMembers;
        for (StudentMember member : allStudentsFromDatabase) {
            if (isPotentialDriver(member, calenderAvailability, targetLocation)) {
                matchStudents.add(member);
            }

        }
        progressBar.setVisibility(View.INVISIBLE);
        title.setText(String.valueOf(matchStudents.size()).concat(" students on your way"));
        Typeface typeface1 = ResourcesCompat.getFont(this,R.font.quicksand_medium);
        title.setTypeface(typeface1);
        title.setVisibility(View.VISIBLE);
        subtitle.setVisibility(View.VISIBLE);
        RecyclerView recyclerViewOfStudentsMembers = (RecyclerView) findViewById(R.id.recyclerView_result);
        RecycleViewAdapter myAdapter = new RecycleViewAdapter(getApplicationContext(), matchStudents);
        recyclerViewOfStudentsMembers.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewOfStudentsMembers.setAdapter(myAdapter);

    }


    private boolean isPotentialDriver(StudentMember member, Calendar calenderAvailability, String targetLocation) throws IOException {
        String dateFormatOfCalender = calenderAvailability.get(Calendar.DAY_OF_MONTH) + "." + calenderAvailability.get(Calendar.MONTH)
                + "." + calenderAvailability.get(Calendar.YEAR);

        HashMap availabilityMap = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            availabilityMap = mapper.readValue(member.getObjectMapperObservabilityDateAndTime(), HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (availabilityMap.containsKey(dateFormatOfCalender)) {
            String possibleTime = (String) availabilityMap.get(dateFormatOfCalender);
            if (checkTimeAvailability(possibleTime, calenderAvailability) &&
                 ( checkDistanceLocations(member, targetLocation))) {
                {
                    return true;
                }
            }

            return false;
        }

        return false;
    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }


    private boolean checkDistanceLocations(StudentMember member, String targetLocation) throws IOException {
        LatLng latLngOfTargetAddressOfMember = getLatLngFromAddress(member.getHomeAddress());
        LatLng latLngOfEndAddressOfInputUser = getLatLngFromAddress(targetLocation);

        // Check if the distance between 2 users is less then 50 KM
        double distance = CalculationByDistance(latLngOfEndAddressOfInputUser, latLngOfTargetAddressOfMember);
        if (distance <= 16 && Double.compare(distance,0) != 0) {
            String distanceInString = new DecimalFormat("##.##").format(distance);
            member.setKmFromMember(distanceInString);
            return true;
        }

return false;
   }

    

    private LatLng getLatLngFromAddress(String addressStr) throws IOException {

        Geocoder geocoder = new Geocoder(MatchDrivers.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(addressStr, 1);
        } catch (IOException e) {
            Log.e("my tag", "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            return new LatLng(address.getLatitude(),address.getLongitude());
        }
        return null;

    }

    private boolean checkTimeAvailability(String member, Calendar calenderAvailabilityInput) {
        String[] hourSplit = member.split(":");
        int hourAvailabilityOfMember = Integer.parseInt(hourSplit[0]);
        int minuteAvailabilityOfMember = Integer.parseInt(hourSplit[1]);
        int hourAvailabilityOfInputUser = (Calendar.AM == 1) ? Calendar.HOUR : Calendar.HOUR + 11;
        int minuteAvailabilityOfInputUser = calenderAvailabilityInput.get(Calendar.MINUTE);
        int possibleTimeOfMember = hourAvailabilityOfMember * 100 + minuteAvailabilityOfMember;
        int possibleTimeOfInpurtUser = hourAvailabilityOfInputUser * 100 + minuteAvailabilityOfInputUser;

        return Math.abs(possibleTimeOfInpurtUser - possibleTimeOfMember) <= 500;
    }
}

//https://www.google.com/maps/place/Yerushalayim+St+142,+Azor/@32.0296361,34.7995957,17z/data=!4m13!1m7!3m6!1s0x151d4b31542cd91f:0xc597ce9c9132953d!2sYerushalayim+St+142,+Azor!3b1!8m2!3d32.0296361!4d34.8017897!3m4!1s0x151d4b31542cd91f:0xc597ce9c9132953d!8m2!3d32.0296361!4d34.8017897