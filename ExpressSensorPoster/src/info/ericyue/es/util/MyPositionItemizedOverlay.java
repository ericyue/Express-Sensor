/**
 * Copyright:   Moonlight(Eric yue)
 * Email:       hi.moonlight@gmail.com
 * Website:     www.ericyue.info
 * File name:   MyPositionItemizedOverlay.java 
 * Project Name:ExpressSensor
 * Create Date: 2011-7-19
 */
package info.ericyue.es.util;
import java.util.ArrayList;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
/*
 *图标标示类
 * */
@SuppressWarnings("rawtypes")
public class MyPositionItemizedOverlay extends ItemizedOverlay {
	@SuppressWarnings("unchecked")
	private ArrayList<OverlayItem> mOverlays=new ArrayList();
	public MyPositionItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
	public void addOverlay(OverlayItem overlay){
		mOverlays.add(overlay);
		populate();
	}
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	@Override
	public int size() {
		return mOverlays.size();
	}
}
