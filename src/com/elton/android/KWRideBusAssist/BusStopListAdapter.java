package com.elton.android.KWRideBusAssist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BusStopListAdapter extends BaseAdapter {

	private int m_layout;
	private Cursor m_cursor;
	private Context m_ctx;
	private LayoutInflater m_layoutInflater;
	
	public BusStopListAdapter(Context ctx, int layout, Cursor dbCursor ) {
		m_ctx = ctx;
		m_cursor = dbCursor;
		m_layout = layout;
		m_layoutInflater = LayoutInflater.from(m_ctx);
	}

	@Override
	public int getCount() {
		return m_cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		Log.d("BusStopListAdapter", "getItem at position: " + Integer.toString(position));
		m_cursor.moveToPosition(position);
		return m_cursor;
	}

	@Override
	public long getItemId(int position) {
		Log.i("BusStopListAdapter itemID: ", Integer.toString(position));
		return position;
	}
	
	public final class ViewHolder {
		public TextView firstLine;
		public TextView secondLine;
		public Button delete;
		public Button edit;
	}
	
	private String m_busStopNum;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("current cursor position: ", Integer.toString(position));
		m_cursor.moveToPosition(position);
		ViewHolder holder = null;
		if( convertView == null) {
			convertView = m_layoutInflater.inflate(R.layout.my_stop_list, null);
			holder = new ViewHolder();
			holder.firstLine = (TextView)convertView.findViewById(android.R.id.text1);
			
			holder.secondLine = (TextView)convertView.findViewById(android.R.id.text2);
			
			holder.delete = (Button)convertView.findViewById(R.id.delete);
			
			holder.edit = (Button)convertView.findViewById(R.id.edit);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.firstLine.setText(m_cursor.getString(m_cursor.getColumnIndex(Constants.TABLE_DESCRIPTION)));
		m_busStopNum = m_cursor.getString(m_cursor.getColumnIndex(Constants.TABLE_ID));
		holder.secondLine.setText(m_busStopNum);
		
		holder.delete.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("ListView", "delete button clicked!");
				deleteBusStop();
			}
		});
		
		return convertView;
	}

	private AlertDialog confirmDialog;
    private void deleteBusStop() {
    	confirmDialog = new AlertDialog.Builder(m_ctx)
    							.setTitle(R.string.warning)
    							.setMessage(R.string.deleteWarningMessage)
    							.setCancelable(true)
    							.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										confirmDialog.dismiss();
									}
								})
    							.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
								    	SQLiteDatabase mSQLiteDatabase = m_ctx.openOrCreateDatabase(Constants.DATABASE_NAME, Context.MODE_PRIVATE, null);
								    	if( mSQLiteDatabase.delete(Constants.TABLE_NAME, Constants.TABLE_ID + "=?", new String[] {m_busStopNum} ) > 0) {
								    		Toast deleted = Toast.makeText(m_ctx, R.string.deleteSuccess, Toast.LENGTH_SHORT);
								    		deleted.show();
								    	}
								    	//mSQLiteDatabase.close();
								    	m_cursor.requery();
								    	BusStopListAdapter.this.notifyDataSetChanged();
									}
								}).create();
    	confirmDialog.show();
    }
}
