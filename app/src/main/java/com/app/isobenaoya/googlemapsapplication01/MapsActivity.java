package com.app.isobenaoya.googlemapsapplication01;

import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            //. Try to obtain the map from the SupportMapFragment
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        Intent intent = getIntent();

        if(intent != null) {
            String addr = intent.getStringExtra("addr");
            String shop = intent.getStringExtra("shop");
            int MAXSHOPSIZE = Integer.parseInt(intent.getStringExtra("shopnum"));
            Address[] address;
            if(!(addr==null||addr=="")) {
                double latitude[] = new double[MAXSHOPSIZE];
                double longitude[] = new double[MAXSHOPSIZE];
                //Geocoder
                try {

                   address = getLatLongFromLocationName(addr + " " + shop);

                    //�������ʂ̃A�h���X�l�ɂl�`�w�T�C�Y���Z�b�g
                    MAXSHOPSIZE = address.length;

                    //�A�h���X�̒l�Ɠ������̃_�u���z����C���X�^���X��
                    latitude = new double[MAXSHOPSIZE];
                    longitude = new double[MAXSHOPSIZE];
                    for(int i =0;MAXSHOPSIZE>i;i++) {
                        latitude[i] = address[i].getLatitude();
                        longitude[i] = address[i].getLongitude();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //�q�b�g�����ꌏ�ڂɂ����J�������ړ�����
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude[0], longitude[0]), 8);
                mMap.moveCamera(cu);
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);

                //���ݒn�̃}�[�J�[�ݒ�
                //Location loc = mMap.getMyLocation();

                //LocationManager�̎擾
                LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
                //GPS���猻�ݒn�̏����擾
                Location myLocate = locationManager.getLastKnownLocation("gps");
                double myLati=0.00;
                double myLongi=0.00;
                if(myLocate != null) {
                    //���ݒn���擾����
                    //�ܓx�̎擾
                    myLati = myLocate.getLatitude();
                    //�o�x�̎擾
                    myLongi = myLocate.getLongitude();
                }
                //myLati = loc.getLatitude();
                //myLongi = loc.getLongitude();
                BitmapDescriptor nowIcon = BitmapDescriptorFactory.fromResource(R.drawable.cursor);
                mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(myLati,myLongi))
                                .title("NOW")
                                .alpha(50)
                                .icon(nowIcon)
                );


                //�~��\������
                mMap.addCircle(new CircleOptions()
                                .center(new LatLng(myLati, myLongi))
                                .radius(2000)
                                .fillColor(Color.argb(70, 255, 255, 0))
                                .strokeColor(Color.argb(70, 0, 255, 0))
                                .strokeWidth(3f)
                );

                //�}�[�J�[���������ʂԂ�\��
                if(MAXSHOPSIZE>0) {
                    //�ŏ��̌������ʂ̃}�[�J�[�ݒ�
                    BitmapDescriptor firstIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                    String title = "SELECTED:";
                    mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(latitude[0], longitude[0]))
                                    .title(title + shop)
                                    .snippet("�ܓx�F" + latitude[0] + "�@�o�x�F" + longitude[0])
                                    .icon(firstIcon)
                    );
                    //���̑��̃}�[�J�[�ݒ�
                    BitmapDescriptor otherIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                    for (int i = 1; MAXSHOPSIZE > i; i++) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude[i], longitude[i]))
                                .title(shop)
                                .snippet(" Y : "+latitude[i]+" /  X : "+longitude[i]+" ")
                                .icon(otherIcon)
                        );
                    }

                    //���ݒn�ƑS���̃f�[�^�Ɛ����q��
                    for(int i = 0; MAXSHOPSIZE > i; i++) {
                        mMap.addPolyline(new PolylineOptions()
                                        .add(new LatLng(myLati, myLongi))
                                        .add(new LatLng(latitude[i], longitude[i]))
                                        .color(Color.CYAN)
                                        .width(1f)
                        );
                    }
                }
            }else{
                Intent mapIntent = new Intent(MapsActivity.this,AddListActivity.class);
                startActivity(mapIntent);
            }
        }else{
            Intent mapIntent = new Intent(MapsActivity.this,AddListActivity.class);
            startActivity(mapIntent);
        }
    }

    private Address[] getLatLongFromLocationName(String locationName) throws IOException{
        Geocoder gcoder = new Geocoder(this, Locale.getDefault());

        Intent intent = getIntent();
        final int MAXSHOPSIZE = Integer.parseInt(intent.getStringExtra("shopnum"));

        // MAXSHOPSIZE ���܂ŏZ�����擾
        List<Address> addressList = gcoder.getFromLocationName(locationName,MAXSHOPSIZE);
            Address[] address = new Address[addressList.size()];
            for (int i = 0; addressList.size()>i && i < MAXSHOPSIZE; i++) {
                address[i] = addressList.get(i);
            }

        return address;
    }
}