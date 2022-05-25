package com.example.turismyandexmaps.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.turismyandexmaps.R;
import com.example.turismyandexmaps.databinding.FragmentMapsBinding;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSection;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.geometry.SubpolylineHelper;
import com.yandex.mapkit.location.FilteringMode;
import com.yandex.mapkit.location.Location;
import com.yandex.mapkit.location.LocationListener;
import com.yandex.mapkit.location.LocationManager;
import com.yandex.mapkit.location.LocationStatus;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment{
    private FragmentMapsBinding binding;
    protected Map map;
    protected MapView mapview;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Point location;

    private MapObjectCollection mapObjects;

    private final InputListener inputListener = new InputListener() {
        @Override
        public void onMapTap(@NonNull Map map, @NonNull Point point) {
            if(location != null) {
                binding.toInfoBtn.setVisibility(View.GONE);
                binding.cityName.setText("");
                requestRoutes(point);
            } else {
                Toast.makeText(getContext(), "Подождите загрузки вашей локации", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

        }
    };

    private final MapObjectTapListener sochi_listener = (mapObject, point) -> {
        setInfoActivity(R.layout.sochi, point, "Сочи");
        return true;
    };
    private final MapObjectTapListener khv_listener = (mapObject, point) -> {
        setInfoActivity(R.layout.khabarovsk, point, "Хабаровск");
        return true;
    };
    private final MapObjectTapListener pkc_listener = (mapObject, point) -> {
        setInfoActivity(R.layout.kamchatka, point, "Камчатка");
        return true;
    };
    private final MapObjectTapListener spb_listener = (mapObject, point) -> {
        setInfoActivity(R.layout.saint_petersburg, point, "Санкт-Петербург");
        return true;
    };
    private final MapObjectTapListener mmk_listener = (mapObject, point) -> {
        setInfoActivity(R.layout.murmansk, point, "Мурманск");
        return true;
    };

    private final MapObjectTapListener irkutsk_listener = (mapObject, point) -> {
        setInfoActivity(R.layout.irkutsk, point, "Иркутск");
        return true;
    };

    private final MapObjectTapListener samara_listener = (mapObject, point) -> {
        setInfoActivity(R.layout.samara, point, "Самара");
        return true;
    };

    private Intent i;

    private CameraPosition lastCameraPos;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        mapview = binding.mapview;

        binding.getRoot().setOnTouchListener((v, event) -> {
            binding.toInfoBtn.setVisibility(View.GONE);
            return false;
        });

        map = mapview.getMap();
        map.addInputListener(inputListener);
        mapObjects = map.getMapObjects();
        map.move(lastCameraPos == null ? map.getCameraPosition():lastCameraPos);
        if(location != null){
            mapObjects.addPlacemark(location, ImageProvider.fromBitmap(BitmapFactory.decodeResource(
                    getResources(), R.drawable.person_mark)));
        }

        //СОЧИ
        addCityObject(new Point(43.6, 39.74), sochi_listener);
        //SPB
        addCityObject(new Point(60, 30.4), spb_listener);
        //MURMANSK
        addCityObject(new Point(68.97, 33), mmk_listener);
        //KHABAROVSK
        addCityObject(new Point(48.5, 135), khv_listener);
        //KAMCHATKA
        addCityObject(new Point(57, 160), pkc_listener);
        //IRKUTSK
        addCityObject(new Point(52.28, 104.28), irkutsk_listener);
        //SAMARA
        addCityObject(new Point(53.21, 50.19), samara_listener);

        binding.toInfoBtn.setOnClickListener(v -> startActivity(i));

        return binding.getRoot();
    }

    private void addCityObject(Point point, MapObjectTapListener listener){
        mapObjects.addPlacemark(point, ImageProvider.fromBitmap(BitmapFactory.decodeResource(
                getResources(), com.yandex.maps.mobile.R.drawable.
                        search_layer_advert_pin_icon_default))).addTapListener(listener);
    }

    private void setInfoActivity(int layout, Point point, String city_name){
        if(location != null) {
            requestRoutes(point);
        }
        binding.toInfoBtn.setVisibility(View.VISIBLE);
        i = new Intent(requireContext(), InfoActivity.class);
        i.putExtra("Layout", layout);
        binding.cityName.setText("Сейчас выбран:\n" + city_name);
    }

    private void requestRoutes(Point target) {
        clearMapWithoutPoints();

        ArrayList<RequestPoint> route_points = new ArrayList<>();
        DrivingOptions options = new DrivingOptions();
        VehicleOptions veh_options = new VehicleOptions();
        route_points.add(new RequestPoint(location, RequestPointType.WAYPOINT, null));
        route_points.add(new RequestPoint(target, RequestPointType.WAYPOINT, null));
        DrivingRouter router = DirectionsFactory.getInstance().createDrivingRouter();
        router.requestRoutes(route_points, options, veh_options, new DrivingSession.DrivingRouteListener() {
            @Override
            public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
                binding.routeLength.setText("Строим маршрут...");
                try {
                    drawRoute(list.get(0));
                }catch (IndexOutOfBoundsException e){
                    binding.routeLength.setText("Маршрутов нет!");
                    return;
                }
                Toast.makeText(getContext(), "Маршрут построен успешно!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrivingRoutesError(@NonNull Error error) {
                binding.routeLength.setText("Ошибка в получении маршрутов!");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void drawRoute(DrivingRoute route){
        double routeLength = 0;
        int sectionsCount = route.getSections().size();
        int count = 0;
        for(DrivingSection section : route.getSections()){
            Polyline polyline = SubpolylineHelper.subpolyline(route.getGeometry(), section.getGeometry());

            routeLength += fromDegreesToMetres(polyline.getPoints().get(0).getLatitude(),
                    polyline.getPoints().get(0).getLongitude(), polyline.getPoints().get(
                            polyline.getPoints().size() - 1).getLatitude(),
                    polyline.getPoints().get(polyline.getPoints().size() - 1).getLongitude());

            PolylineMapObject polyline_obj = mapObjects.addPolyline(polyline);
            polyline_obj.setStrokeColor(Color.GREEN);
            ++count;
            binding.routeLength.setText("Маршрут просчитан на " + (count / sectionsCount * 100) + "%");
        }
        setRouteInfo(routeLength >= 10 ? routeLength : routeLength * 1000, routeLength >= 10 ? "км" : "м");
    }

    //Haversine formulae
    private double fromDegreesToMetres(double latitudeFrom, double longitudeFrom, double
            latitudeTo, double longitudeTo){
        double dLat = Math.toRadians(Math.abs(latitudeTo - latitudeFrom));
        double dLon = Math.toRadians(Math.abs(longitudeTo - longitudeFrom));

        latitudeFrom = Math.toRadians(latitudeFrom);
        latitudeTo = Math.toRadians(latitudeTo);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2)
                * Math.cos(latitudeFrom) * Math.cos(latitudeTo);
        double Earth_R = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return Earth_R * c;

    }

    @SuppressLint("SetTextI18n")
    private void setRouteInfo(double routeLength, String units){
        String message = String.format("Длина построенного пути: %s %s.", routeLength, units);
        binding.routeLength.setText(message);
    }

    private void clearMapWithoutPoints(){
        map.getMapObjects().clear();
        //СОЧИ
        addCityObject(new Point(43.6, 39.74), sochi_listener);
        //SPB
        addCityObject(new Point(60, 30.4), spb_listener);
        //MURMANSK
        addCityObject(new Point(68.97, 33), mmk_listener);
        //KHABAROVSK
        addCityObject(new Point(48.5, 135), khv_listener);
        //KAMCHATKA
        addCityObject(new Point(57, 160), pkc_listener);
        //IRKUTSK
        addCityObject(new Point(52.28, 104.28), irkutsk_listener);
        //SAMARA
        addCityObject(new Point(53.21, 50.19), samara_listener);

        map.getMapObjects().addPlacemark(location, ImageProvider.fromBitmap(BitmapFactory.
                decodeResource(getResources(), R.drawable.person_mark)));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String MAP_API_KEY = "cc948696-fc77-419e-8bba-01fbc2e31141";

        MapKitFactory.setApiKey(MAP_API_KEY);
        MapKitFactory.initialize(this.requireContext());
        DirectionsFactory.initialize(requireContext());

        locationManager = MapKitFactory.getInstance().createLocationManager();
        locationListener = new LocationListener() {
            @Override
            public void onLocationUpdated(@NonNull Location location) {
                if (MapsFragment.this.location == null) {
                    Toast.makeText(requireContext(), "Ваша позиция загружается...",
                            Toast.LENGTH_LONG).show();
                    mapview.getMap().move(new CameraPosition(location.getPosition(),
                            15f, 0f, 0f), new Animation(Animation.Type.SMOOTH,
                            2), null);
                    Point point = location.getPosition();
                    ImageProvider image = ImageProvider.fromBitmap(BitmapFactory.decodeResource(
                            getResources(), R.drawable.person_mark));
                    mapObjects.addPlacemark(point, image);
                    Toast.makeText(requireContext(), "Ваша позиция загружена!",
                            Toast.LENGTH_LONG).show();
                }
                MapsFragment.this.location = location.getPosition();
            }

            @Override
            public void onLocationStatusUpdated(@NonNull LocationStatus locationStatus) {}
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        if(location == null){

            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.
                    ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.subscribeForLocationUpdates(0, 0, 50, false,
                        FilteringMode.OFF, locationListener);
            }
        }
        mapview.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lastCameraPos = mapview.getMap().getCameraPosition();
        MapKitFactory.getInstance().onStop();
        mapview.onStop();
    }
}