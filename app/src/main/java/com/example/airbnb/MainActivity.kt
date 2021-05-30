package com.example.airbnb

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val mapView: MapView by lazy {
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
        naverMap.minZoom = 11.0

        // 초기 위치 설정
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(35.566610, 129.249384))
        naverMap.moveCamera(cameraUpdate)

        // 현 위치 버튼
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled =
            true // 사용권한을 manifest에 정의 , API 2.6이후부턴 팝업을 통해 권한 동의 필요
        locationSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSTION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        getHouseListFromAPI()
    }

    private fun getHouseListFromAPI() {
        // api에 맞게 마커 작업
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(HouseService::class.java).also {
            it.getHouseList()
                .enqueue(object : Callback<HouseDto> {
                    override fun onResponse(call: Call<HouseDto>, response: Response<HouseDto>) {
                        if (response.isSuccessful.not()) {
                            // 실패에 대한 처리
                            return
                        }
                        response.body()?.let { dto ->
                            dto.items.forEach { house ->
                                val marker = Marker()
                                marker.position = LatLng(house.lat, house.lng)
                                //todo 마커 클릭 리스너
                                marker.map = naverMap
                                marker.tag = house.id
                                marker.icon = MarkerIcons.BLACK
                                marker.iconTintColor = Color.RED

                            }
                        }
                    }

                    override fun onFailure(call: Call<HouseDto>, t: Throwable) {

                    }

                })
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSTION_REQUEST_CODE) {
            return
        }
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
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


    companion object {
        private const val LOCATION_PERMISSTION_REQUEST_CODE = 1000
    }
}