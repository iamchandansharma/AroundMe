package me.chandansharma.aroundme.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by iamcs on 2017-05-04.
 */

public class PlaceDetailWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PlaceDetailWidgetAdapter(this);
    }
}
