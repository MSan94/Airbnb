package com.example.airbnb

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap : NaverMap
    private lateinit var locationSource : FusedLocationSource
    private val mapView : MapView by lazy {
        findViewById<MapView>(R.id.mapView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView.onCreate(savedInstanceState)


        mapView.getMapAsync(this)

    }


    override fun onMapReady(map: NaverMap) {
        naverMap = map
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 15.0
        
        // 초기 위치 설정
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(35.566610,129.249384))
        naverMap.moveCamera(cameraUpdate)

        // 현 위치 버튼
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = true // 사용권한을 manifest에 정의 , API 2.6이후부턴 팝업을 통해 권한 동의 필요
        locationSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSTION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        // 마커
        val marker = Marker()
        marker.position = LatLng(35.566955,129.249759)
        marker.map = naverMap
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode != LOCATION_PERMISSTION_REQUEST_CODE){
            return
        }
        if(locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults)){
            if(!locationSource.isActivated){
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    companion object{
        private const val LOCATION_PERMISSTION_REQUEST_CODE = 1000
    }
}