package org.ze.smartc2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener ,LocationListener {
    final static String TAG = "getdataTAG";
    String param, result;
    ProgressDialog pDialog;
    private GoogleMap mMap;
    final static String u = "http://192.168.137.178/smart-city/getdata_android.php";
    ArrayList<HashMap<String, String>> points = new ArrayList<HashMap<String, String>>();
    LatLng currentLatLng,latlng;
    Double LAT, LNG;
    Double tai, nan;
    private final static String CALL = "android.intent.action.CALL";

    Double nlat,nlng;
    Double glat,glng;
    MarkerOptions markerOpt;
    HashMap hash;
    ArrayList<LatLng> markerPoints;
    Bundle bundle;
    LocationManager lm,glocManager;
    android.location.LocationListener nlocListener,glocListener;
    private final int second = 0;
    private final int meters = 0;
    private LatLng origin;
    Marker Marker_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        nlocListener = new MyLocationListenerNetWork();
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000 * 1,  // 1 Sec
                0,         // 0 meter
                nlocListener);




        // Add a marker in Sydney and move the camera

        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        criteria.setAccuracy(Criteria.ACCURACY_FINE);//設置為最大精度
        Location location = lm.getLastKnownLocation(lm.getBestProvider(criteria, true));
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, second,meters,LocationListener);
        Log.e("GG or nn",lm.getBestProvider(criteria, true));
        LAT = location.getLatitude();
        LNG = location.getLongitude();
        currentLatLng = new LatLng(LAT, LNG);
//        LatLng tainan = new LatLng(tai, nan);
        LatLng tainan = new LatLng(LAT ,LNG);
        mMap.addMarker(new MarkerOptions().position(tainan).title("You are here!!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tainan, 16));
        mMap.setMyLocationEnabled(true);
        bundle = this.getIntent().getExtras();


        String table = bundle.getString("table");
        new get(table).execute();
        mMap.setOnMarkerClickListener(this);
    }







    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e("marker click", "success");


        Log.e("marker.gettitle", marker.getTitle());
        Log.e("marker.getId", marker.getId());
        Log.e("marker.getposition",marker.getPosition().toString());
        markerPoints = new ArrayList<LatLng>();
        LatLng mysite = new LatLng(LAT,LNG);
        LatLng close = marker.getPosition();
        markerPoints.add(mysite);
        markerPoints.add(close);
        String Na ="";
        try{
            Na = hash.get("Name").toString();
        }catch(Exception e){
            Na = "info";
        }

        if(marker.getTitle().equals("最近的警察局："+Na)){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS},1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }

            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("報警")
                    .setMessage("是否要報警?")
                    .setPositiveButton("立刻報警", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent call = new Intent(CALL, Uri.parse("tel:" + "0975381155"));

                            startActivity(call);
                        }
                    })
                    .setNegativeButton("先等等", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("幫我帶路", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (markerPoints.size() >= 2) {
                                LatLng origin = markerPoints.get(0);
                                LatLng dest = markerPoints.get(1);

                                // Getting URL to the Google Directions API
                                String url = getDirectionsUrl(origin, dest);

                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions
                                // API
                                downloadTask.execute(url);
                            }
                        }
                    })
                    .show();
        }
        else if(marker.getTitle().equals("最近的醫院："+Na)){
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_CONTACTS)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_CONTACTS}, 1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }

            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle("送醫")
                    .setPositiveButton("立刻打電話", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent call = new Intent(CALL, Uri.parse("tel:" + "0975381155"));

                            startActivity(call);
                        }
                    })
                    .setNegativeButton("先等等", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNeutralButton("幫我帶路", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (markerPoints.size() >= 2) {
                                LatLng origin = markerPoints.get(0);
                                LatLng dest = markerPoints.get(1);

                                // Getting URL to the Google Directions API
                                String url = getDirectionsUrl(origin, dest);

                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions
                                // API
                                downloadTask.execute(url);
                            }
                        }
                    })
                    .show();
        }
        else if(marker.getTitle().equals("You are here!!")){

        }
        else{


            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle(Na)
                    .setMessage("幫我帶路")
                    .setPositiveButton("路線規劃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (markerPoints.size() >= 2) {
                                LatLng origin = markerPoints.get(0);
                                LatLng dest = markerPoints.get(1);

                                // Getting URL to the Google Directions API
                                String url = getDirectionsUrl(origin, dest);

                                DownloadTask downloadTask = new DownloadTask();

                                // Start downloading json data from Google Directions
                                // API
                                downloadTask.execute(url);
                            }
                        }
                    })
                    .setNegativeButton("先等等", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float accuracy = location.getAccuracy();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /** 當服務提供商可提供服務時，會透過這個 method 告知 */

    public void onProviderEnabled(String provider) {

    }

    /** 當服務提供商失效時，會透過這個 method 告知 */

    public void onProviderDisabled(String provider) {

    }
    public class get extends AsyncTask<String, Void, String>  {
        String table;


        public get(String table) {
            this.table = table;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if(table.equals("police_close")){
                    param = "data_table=" + URLEncoder.encode("police", "UTF-8");
                }
                else if(table.equals("hospital_close")){
                    param = "data_table=" + URLEncoder.encode("hospital", "UTF-8");
                }
                else{
                    param = "data_table=" + URLEncoder.encode(table, "UTF-8");
                }

                URL url = new URL(u);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla / 5.0");

                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(param.getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                System.out.println("POST Response Code :: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    // print result
                    System.out.println(response.toString());
                    result = response.toString();
                }
                else{
                    System.out.println("POST request not worked");
                    result = "failed";
                }

            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.e(TAG, "abcc" + result);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            if (pDialog.isShowing())
//                pDialog.dismiss();
            Log.e(TAG, "aa" + result);
            if (result!=null) {
                switch (table){
                    case "bab":
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            points.clear();
                            int tol =0;
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Name = jsonObject.getString("Name");
                                String Addr = jsonObject.getString("Addr");
                                String Tel = jsonObject.getString("Tel");
                                Double Lat = Double.parseDouble(jsonObject.getString("Lat"));
                                Double Lng = Double.parseDouble(jsonObject.getString("Lng"));
                                String site = jsonObject.getString("site");

                                float results[]=new float[1];
                                //現在緯度,現在經度,目標緯度,目標經度,
                                Location.distanceBetween(LAT, LNG, Lat, Lng, results);
                                String distance = NumberFormat.getInstance().format(results[0]);
                                Double dis = Double.parseDouble(distance.replace(",", ""));
                                Log.e("distance",dis+Name);

                                if(dis<bundle.getInt("dis")/1000){
                                    LatLng bab = new LatLng(Lat,Lng);
                                    mMap.addMarker(new MarkerOptions().position(bab).title(Name));
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    hashMap.put(TABLE_DISNAME, disName);
//                                    hashMap.put(TABLE_INFO, info);
//                                    hashMap.put(TABLE_SYMPTOMID, symptomID);
                                    tol++;
                                }


//                                points.add(hashMap);

                            }
                            if(tol==0){
                                Toast.makeText(MapsActivity.this, bundle.getInt("dis")/1000+"公里內沒有民宿", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "view":
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            points.clear();
                            int tol =0;
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Name = jsonObject.getString("Name");
                                String Told = jsonObject.getString("Told");
                                String Addr = jsonObject.getString("Addr");
                                String Tel = jsonObject.getString("Tel");
                                String Open = jsonObject.getString("Open");
                                Double Px = Double.parseDouble(jsonObject.getString("Px"));
                                Double Py = Double.parseDouble(jsonObject.getString("Py"));
                                String site = jsonObject.getString("site");
                                String Ticket = jsonObject.getString("Ticket");
                                String PS = jsonObject.getString("PS");

                                float results[]=new float[1];
                                //現在緯度,現在經度,目標緯度,目標經度,
                                Location.distanceBetween(LAT, LNG, Px, Py, results);
                                String distance = NumberFormat.getInstance().format(results[0]);
                                Double dis = Double.parseDouble(distance.replace(",", ""));
                                Log.e("distance", dis + "");

                                if(dis<bundle.getInt("dis")){
                                    LatLng bab = new LatLng(Px,Py);
                                    mMap.addMarker(new MarkerOptions().position(bab).title(Name));
                                    tol++;
                                }


                                //                                points.add(hashMap);

                            }
                            if(tol==0){
                                Toast.makeText(MapsActivity.this, bundle.getInt("dis")/1000+"公里內沒有其他景點", Toast.LENGTH_LONG).show();
                            }
                            //                            Toast.makeText(getApplicationContext(),tol,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case "parking":
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            points.clear();
                            int tol =0;
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Name = jsonObject.getString("name");
                                String payinfo = jsonObject.getString("payinfo");
                                String worktime = jsonObject.getString("worktime");
                                Double Lat = Double.parseDouble(jsonObject.getString("px"));
                                Double Lng = Double.parseDouble(jsonObject.getString("py"));


                                float results[]=new float[1];
                                //現在緯度,現在經度,目標緯度,目標經度,
                                Location.distanceBetween(LAT, LNG, Lat, Lng, results);
                                String distance = NumberFormat.getInstance().format(results[0]);
                                Double dis = Double.parseDouble(distance.replace(",", ""));
                                Log.e("distance",dis+"");

                                if(dis<3000){
                                    Log.d("ddd",dis+"");
                                    LatLng bab = new LatLng(Lat,Lng);
                                    mMap.addMarker(new MarkerOptions().position(bab).title(Name));
                                    //                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    //                                    hashMap.put(TABLE_DISNAME, disName);
                                    //                                    hashMap.put(TABLE_INFO, info);
                                    //                                    hashMap.put(TABLE_SYMPTOMID, symptomID);
                                    tol++;
                                }


                                //                                points.add(hashMap);
                                Log.e("tol",tol+"");

                            }
                            if(tol==0){
                                Toast.makeText(getApplicationContext(),"3公里內沒有停車場",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case "police":
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            points.clear();
                            int tol =0;
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Name = jsonObject.getString("name");
                                String Addr = jsonObject.getString("addr");
                                String Tel = jsonObject.getString("tel");
                                Double Lat = Double.parseDouble(jsonObject.getString("lat"));
                                Double Lng = Double.parseDouble(jsonObject.getString("lng"));
                                String site = jsonObject.getString("site");

                                float results[]=new float[1];
                                //現在緯度,現在經度,目標緯度,目標經度,
                                Location.distanceBetween(LAT, LNG, Lat, Lng, results);
                                String distance = NumberFormat.getInstance().format(results[0]);
                                Double dis = Double.parseDouble(distance.replace(",", ""));
                                Log.e("distance",dis+"");

                                if(dis<3000){
                                    Log.d("ddd",dis+"");
                                    LatLng bab = new LatLng(Lat,Lng);
                                    mMap.addMarker(new MarkerOptions().position(bab).title(Name));
                                    //                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    //                                    hashMap.put(TABLE_DISNAME, disName);
                                    //                                    hashMap.put(TABLE_INFO, info);
                                    //                                    hashMap.put(TABLE_SYMPTOMID, symptomID);
                                    tol++;
                                }


                                //                                points.add(hashMap);

                            }
                            if(tol==0){
                                Toast.makeText(getApplicationContext(),"3公里內沒有警局",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;


                    case "police_close":
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            points.clear();
                            hash =  new HashMap();
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Name = jsonObject.getString("name");
                                String Addr = jsonObject.getString("addr");
                                String Tel = jsonObject.getString("tel");
                                Double Lat = Double.parseDouble(jsonObject.getString("lat"));
                                Double Lng = Double.parseDouble(jsonObject.getString("lng"));
                                String site = jsonObject.getString("site");

                                float results[]=new float[1];
                                //現在緯度,現在經度,目標緯度,目標經度,
                                Location.distanceBetween(LAT, LNG, Lat, Lng, results);
                                String distance = NumberFormat.getInstance().format(results[0]);
                                Double dis = Double.parseDouble(distance.replace(",", ""));
                                Log.e("distance",dis+"");

                                if(i==0){
                                    hash.put("Name",Name);
                                    hash.put("Addr",Addr);
                                    hash.put("Tel",Tel);
                                    hash.put("Lat",Lat);
                                    hash.put("Lng",Lng);
                                    hash.put("Dis",dis.toString());
                                }
                                else{
                                    if(Double.parseDouble((String) hash.get("Dis"))>dis){
                                        hash.put("Name",Name);
                                        hash.put("Addr",Addr);
                                        hash.put("Tel",Tel);
                                        hash.put("Lat",Lat);
                                        hash.put("Lng",Lng);
                                        hash.put("Dis", dis.toString());

                                        Log.e("close police",Name+" "+Addr+" "+Tel+" "+Lat+","+Lng);
                                    }
                                }

                                //                                points.add(hashMap);

                            }


                            Log.e("Name",hash.get("Name")+"");
                            Log.e("Tel",hash.get("Tel")+"");
                            Log.e("Lat",hash.get("Lat")+"");
                            Log.e("Lng",hash.get("Lng")+"");


                            markerOpt = new MarkerOptions();
                            LatLng police_close = new LatLng((Double)hash.get("Lat"), (Double) hash.get("Lng"));
                            markerOpt.position(police_close);
                            markerOpt.title("最近的警察局：" + hash.get("Name"));


                            mMap.addMarker(markerOpt);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;

                    case "hospital_close":
                        try {
                            JSONArray jsonArray = new JSONArray(result);
                            points.clear();
                            hash =  new HashMap();
                            for (int i = 0; i < jsonArray.length(); i++) {


                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String Name = jsonObject.getString("name");
                                String Addr = jsonObject.getString("addr");
                                String Tel = jsonObject.getString("tel");
                                Double Lat = Double.parseDouble(jsonObject.getString("lat"));
                                Double Lng = Double.parseDouble(jsonObject.getString("lng"));
                                String site = jsonObject.getString("site");

                                float results[]=new float[1];
                                //現在緯度,現在經度,目標緯度,目標經度,
                                Location.distanceBetween(LAT, LNG, Lat, Lng, results);
                                String distance = NumberFormat.getInstance().format(results[0]);
                                Double dis = Double.parseDouble(distance.replace(",", ""));
                                Log.e("distance",dis+"");

                                if(i==0){
                                    hash.put("Name",Name);
                                    hash.put("Addr",Addr);
                                    hash.put("Tel",Tel);
                                    hash.put("Lat",Lat);
                                    hash.put("Lng",Lng);
                                    hash.put("Dis",dis.toString());
                                }
                                else{
                                    if(Double.parseDouble((String) hash.get("Dis"))>dis){
                                        hash.put("Name",Name);
                                        hash.put("Addr",Addr);
                                        hash.put("Tel",Tel);
                                        hash.put("Lat",Lat);
                                        hash.put("Lng",Lng);
                                        hash.put("Dis", dis.toString());

                                        Log.e("close police",Name+" "+Addr+" "+Tel+" "+Lat+","+Lng);
                                    }
                                }

                                //                                points.add(hashMap);

                            }


                            Log.e("Name",hash.get("Name")+"");
                            Log.e("Tel",hash.get("Tel")+"");
                            Log.e("Lat",hash.get("Lat")+"");
                            Log.e("Lng",hash.get("Lng")+"");


                            markerOpt = new MarkerOptions();
                            LatLng police_close = new LatLng((Double)hash.get("Lat"), (Double) hash.get("Lng"));
                            markerOpt.position(police_close);
                            markerOpt.title("最近的醫院：" + hash.get("Name"));


                            mMap.addMarker(markerOpt);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }



            }

        }


    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;

        return url;
    }

    /**從URL下載JSON資料的方法**/
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
//            Log.e("Exception in downloading url", e.toString());
            e.printStackTrace();
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /** 解析JSON格式 **/
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);  //導航路徑寬度
                lineOptions.color(Color.BLUE); //導航路徑顏色

            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }


    public class MyLocationListenerNetWork implements android.location.LocationListener
    {
        int start123 =0,i=0;
        @Override
        public void onLocationChanged(Location loc)
        {

            nlat = loc.getLatitude();
            nlng = loc.getLongitude();
            latlng=new LatLng(nlat,nlng);
            if(start123==0)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
            start123++;
            origin=latlng;
            String slat=Double.toString(nlat);
            String slng=Double.toString(nlng);
            if(i!=0)
                Marker_location.remove();
            Marker_location= mMap.addMarker(new MarkerOptions().position(latlng).title("目前位置"));

            i++;
            Log.d("LAT & LNG Network:", nlat + " " + nlng);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("LOG", "Network is OFF!");
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            Log.d("LOG", "Thanks for enabling Network !");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }
    }



}