package com.example.loginschoolpool;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FirebaseDatabaseInsert {

    private DatabaseReference databaseUsersReference;
    private FirebaseDatabase firebaseReference;


    public static void main(String[] args) {
        FirebaseDatabaseInsert firebaseDatabaseInsert;
        firebaseDatabaseInsert = new FirebaseDatabaseInsert();
        firebaseDatabaseInsert.InitInsert();
    }

    public FirebaseDatabaseInsert() {
        firebaseReference = FirebaseDatabase.getInstance();
        databaseUsersReference = firebaseReference.getReference("StudentMembers");
    }

    public void InitInsert(){
        String [] studentNames = {"barak_lagziel","aviv_mund","dudi_chen","ido_hayun","noam_levy","oran_sherf","sahar_hagbi","tomer_shaked","ziv_halsband","dan_bar"};
        String [] city = {"Jerusalem", "Tel Aviv", "Rishon LeTsiyon", "Haifa", "Ashdod", "Rishon LeTsiyon", "Petah Tikva", "Beersheba","Kefar Sava","Bat yam"};
        StudentMember studentMember = null;

        for (int i = 1; i <= 10; i++) {
            studentMember = new StudentMember();
            studentMember.setDriveFromHome(true);
            if (i % 3 == 0) {
                studentMember.setDrive(true);
            }
            studentMember.setUsername(studentNames[i-1]);
            addThumbnailToStudent(studentMember,studentNames[i-1]);
            studentMember.setImageName(studentMember.getUsername().toLowerCase().concat("_").concat("pic"));
           // int isFound = geoLocate(city[10 - i]);
            studentMember.setHomeAddress(city[10 - i]);
            try {
                setCalenderOfStudent(studentMember);
            }
            catch (Exception e){
                String message = "cant convert calender student mamber to json";
                Log.d("Exception" , message);
            }
            String numberMember = "Member".concat(String.valueOf(i));
            if(studentMember.getUsername().equals("barak_lagziel")) studentMember.setHomeAddress("Haifa");
            if(studentMember.getUsername().equals("sahar_hagbi")) studentMember.setHomeAddress("Bat yam");
            databaseUsersReference.child(numberMember).setValue(studentMember);
        }


    }

//    private int geoLocate(String address) {
//       Geocoder geocoder = new Geocoder();
//        List<Address> list = new ArrayList<>();
//        try{
//            list = geocoder.getFromLocationName(address, 1);
//        }catch (IOException e){
//            Log.e("myTag", "geoLocate: IOException: " + e.getMessage() );
//        }
//
//        return list.size();
//
//    }


    private void addThumbnailToStudent(StudentMember studentMember, String item) {
                if(item.equals("barak_lagziel")) studentMember.setThumbnail(R.drawable.barak_pic);
                else if(item.equals("aviv_mund")) studentMember.setThumbnail(R.drawable.aviv_mund);
                else if(item.equals("dudi_chen")) studentMember.setThumbnail(R.drawable.dudi_chen);
                else if(item.equals("ido_hayun")) studentMember.setThumbnail(R.drawable.ido_hayun);
                else if(item.equals("noam_levy")) studentMember.setThumbnail(R.drawable.noam_levy);
                else if(item.equals("oran_sherf")) studentMember.setThumbnail(R.drawable.oran_sherf);
                else if(item.equals("sahar_hagbi")) studentMember.setThumbnail(R.drawable.sahar_hagbi);
                else if(item.equals("tomer_shaked")) studentMember.setThumbnail(R.drawable.tomer_shaked);
                else if(item.equals("ziv_halsband")) studentMember.setThumbnail(R.drawable.ziv_halsband);
                else if(item.equals("dan_bar")) studentMember.setThumbnail(R.drawable.dan_bar);
            }



    private void setCalenderOfStudent(StudentMember studentMember) throws JsonProcessingException {
        Random randomNumber = new Random();
        ArrayList<TimeCalendar> weekCalender = new ArrayList<>();
        HashMap<String, String> availabilityDateAndTime = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

      //  DecimalFormat formatter = new DecimalFormat("00");
        for (int j = 1; j <= 3; j++) {
            String year = "2020";
            String month = String.valueOf(randomNumber.nextInt(12));
            String day =  String.valueOf(randomNumber.nextInt(30));
            String hourStartTime  =  String.valueOf(randomNumber.nextInt(23));
            String minuteStartTime =  String.valueOf(randomNumber.nextInt(59));
            insertSpecificDate(availabilityDateAndTime,day,month,year,hourStartTime,minuteStartTime);
            //dd.mm.yyyy-hh:mm
        }
        // Example instance thar i could debug matchDriver algorithm
        insertSpecificDate(availabilityDateAndTime, "20", "4", "2020", "21", "10");
//        if(studentMember.getUsername().equals("barak_lagziel") ||studentMember.getUsername().equals("dan_bar") || studentMember.getUsername().equals("noam_levy")
//        || studentMember.getUsername().equals("sahar_hagbi")){
//            insertSpecificDate(availabilityDateAndTime, "18", "4", "2020", "21", "10");
//        }

        String json = mapper.writeValueAsString(availabilityDateAndTime);
        studentMember.setObjectMapperObservabilityDateAndTime(json);

    }


    private void insertSpecificDate(HashMap<String, String> availabilityDateAndTime, String day, String month, String year, String hour, String minute) {
        String dateAvailability = day + "." + month + "." + year;
        String timeAvailability = hour + ":" + minute;
        TimeCalendar time = new TimeCalendar();
        time.setAvailabilityTime(timeAvailability);
        availabilityDateAndTime.put(dateAvailability, timeAvailability);
    }


}



