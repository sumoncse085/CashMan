package com.cashman.physio.v1.android.alarm.activity.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ZoomButtonsController;

import com.cashman.physio.v1.android.alarm.R;
import com.cashman.physio.v1.android.alarm.data.Constant;
import com.cashman.physio.v1.android.alarm.util.LocalLog;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ViewMapActivity extends MapActivity
{
  private static final String TAG = "ViewMapActivity";
  private String mLatitudeE6 ;
  private String mLongitudeE6 ;
  private String mSnippetPoint = "";
  private String mTitlePoint = "";
  private MapView mapView;

  private void initViews()
  {
    this.mapView = ((MapView)findViewById(R.id.mapview));
    this.mapView.setBuiltInZoomControls(true);
    this.mapView.setClickable(true);
    Intent localIntent = getIntent();
    this.mLatitudeE6 = localIntent.getStringExtra(Constant.Contact.KEY_LOCATION_LATITUDE);
    this.mLongitudeE6 = localIntent.getStringExtra(Constant.Contact.KEY_LOCATION_LONGITUDE);
    this.mTitlePoint = localIntent.getStringExtra(Constant.Contact.KEY_LOCATION_TITLE);
    this.mSnippetPoint = localIntent.getStringExtra(Constant.Contact.KEY_LOCATION_SNIPPET);
    LocalLog.i(TAG, "mLatitudeE6 = " + this.mLatitudeE6 + ",mLongitudeE6 = " + this.mLongitudeE6 + ", mTitlePoint = " + this.mTitlePoint + ", mSnippetPoint = " + this.mSnippetPoint);
    List<Overlay> localList = this.mapView.getOverlays();
    CustomItemizedOverlay localCustomItemizedOverlay = new CustomItemizedOverlay(getResources().getDrawable(R.drawable.location_point), this);
    GeoPoint localGeoPoint = new GeoPoint(Integer.parseInt(this.mLatitudeE6), Integer.parseInt(this.mLongitudeE6));
    localCustomItemizedOverlay.addOverlay(new OverlayItem(localGeoPoint, this.mTitlePoint, this.mSnippetPoint));
    localList.add(localCustomItemizedOverlay);
    MapController localMapController = this.mapView.getController();
    
    localMapController.animateTo(localGeoPoint);
    localMapController.setZoom(18);
  }

  protected boolean isRouteDisplayed()
  {
    return false;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.view_map_layout);
    initViews();
  }

  protected void onDestroy()
  {
    super.onDestroy();
  }

  protected void onStart()
  {
    super.onStart();
  }

  public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem>
  {
    private Context context;
    private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();

    public CustomItemizedOverlay(Drawable arg2)
    {
      super(boundCenterBottom(arg2));
    }

    public CustomItemizedOverlay(Drawable paramDrawbale, Context arg3)
    {
      this(paramDrawbale);
      this.context = arg3;
    }

    public void addOverlay(OverlayItem paramOverlayItem)
    {
      this.mapOverlays.add(paramOverlayItem);
      populate();
    }

    protected OverlayItem createItem(int paramInt)
    {
      return (OverlayItem)this.mapOverlays.get(paramInt);
    }

    protected boolean onTap(int paramInt)
    {
      OverlayItem localOverlayItem = (OverlayItem)this.mapOverlays.get(paramInt);
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.context);
      localBuilder.setTitle(localOverlayItem.getTitle());
      localBuilder.setMessage(localOverlayItem.getSnippet());
      localBuilder.show();
      return true;
    }

    public int size()
    {
      return this.mapOverlays.size();
    }
  }
}