package my.applicationforeground;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import my.applicationforeground.R;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppAdapter extends BaseAdapter{
	Context context;
	LayoutInflater infater = null;
	
	
	private List<MapAppLocItem> listAppIcon = null;
	
	public AppAdapter(Context context, List<MapAppLocItem> list){
		infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context=context;
		this.listAppIcon=list;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return  listAppIcon.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listAppIcon.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//nov21
//		Log.i("track","getview");
		View view = null;
		ViewHolder holder = null;
		if (convertView == null || convertView.getTag() == null) {
			//nov21
//			Log.i("view","converview==null or gettag==null  view = infater.inflate(R.layout.adapter_app_item..");
			view = infater.inflate(R.layout.adapter_app_item, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} 
		else{
			//nov21
//			Log.i("view","converview!=null and gettag1=null view = convertView");
			view = convertView ;
			holder = (ViewHolder) convertView.getTag() ;
		}
		MapAppLocItem mapAppLocItem = (MapAppLocItem) getItem(position);
		holder.appIcon.setImageDrawable(mapAppLocItem.getIcon());
		holder.tvAppLabel.setText(mapAppLocItem.getAppName());
		SimpleDateFormat sdfDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// dd/MM/yyyy
//		Date now = new Date();
		Date now=new Date(mapAppLocItem.getStartMiliSeconds());
		String date = sdfDate.format(now);
		holder.tvPkgName.setText(date);
		holder.tvAppLabel.setTextSize(18);
		holder.tvPkgName.setTextSize(13);
		return view;
	}
	class ViewHolder {
		ImageView appIcon;
		TextView tvAppLabel;
		TextView tvPkgName;

		public ViewHolder(View view) {
			this.appIcon = (ImageView) view.findViewById(R.id.imgIcon);
			this.tvAppLabel = (TextView) view.findViewById(R.id.txtApp);
			this.tvPkgName = (TextView) view.findViewById(R.id.txtPkg);
		}
	}
	

}
