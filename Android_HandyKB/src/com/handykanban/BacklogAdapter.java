/**
 * 
 */
package com.handykanban;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.handykanban.Task.Priority;
import com.handykanban.Task.Status;

/**
 * @author esijuma
 *
 */
public class BacklogAdapter extends BaseAdapter {

	private Context mContext;
	//contain the data
	private ArrayList<Task> list;
	//contain the check state
	private static HashMap isSelected;
	//import layout
	private LayoutInflater inflater = null;
	BacklogAdapter(ArrayList list, Context context)
	{
		mContext = context;
		this.list = list;
		
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap();
        // init data
        initData();		
	}
	
    private void initData(){
        for(int i=0; i< getCount(); i++){
        	getIsSelected().put(i, list.get(i).getStatus()==Status.TODO);
        }
    }

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    ViewHolder holder = null;
        if (convertView == null) {
           // get view holder 
           holder = new ViewHolder();
           // import the layout and assign it to convertview
           convertView = inflater.inflate(R.layout.pickup_todo_list_item, null);
           holder.tv_title = (TextView) convertView.findViewById(R.id.Item_TaskTitle);
           holder.cb = (CheckBox) convertView.findViewById(R.id.item_selectToDo);
           holder.tv_priority = (TextView) convertView.findViewById(R.id.Item_TaskPrio);
           holder.btn = (Button) convertView.findViewById(R.id.item_clickbtn);
           // set tag for view
           convertView.setTag(holder);
       } else {
           // get the holder
           holder = (ViewHolder) convertView.getTag();
       }

       // set the task title
       holder.tv_title.setText(list.get(position).getTitle());
       // set the task priority
       holder.tv_priority.setText(list.get(position).getPriority().toString());
       if(list.get(position).getPriority() == Priority.P1)
       {
    	   holder.tv_priority.setBackgroundColor(Color.RED);
       }
       else if(list.get(position).getPriority() == Priority.P2)
       {
    	   holder.tv_priority.setBackgroundColor(Color.YELLOW);
       }
       else if(list.get(position).getPriority() == Priority.P3)
       {
    	   holder.tv_priority.setBackgroundColor(Color.GREEN);
       }       
       // set checkbox according to value in isSelected
       holder.cb.setChecked(getIsSelected().get(position).equals(true));
       
       holder.btn.setOnClickListener(new OnClickListener() {
           
           @Override
           public void onClick(View v) {
              Log.v("xyz", "Click update button:" + position);  
              Intent intent = new Intent();
              Bundle b=new Bundle();
              b.putInt("TASK_MODE", TaskDetalInfoActivity.EDIT_MODE);
              b.putInt("TASK_ID", list.get(position).getTaskID());
              intent.setClass(v.getContext(), TaskDetalInfoActivity.class);
              intent.putExtras(b);
              v.getContext().startActivity(intent);    
           }
       });       
       return convertView;
	}

	public int todoTaskCount() {
		int count=0;
        for(int i=0; i< getCount(); i++){
        	if(isSelected.get(i).equals(true)){
        	  count ++;
        	}
        };
        return count;
	}
	
	// check whether the specified item was checked
	public boolean isItemChecked(int pos) {
		
        return isSelected.get(pos).equals(true);

	}
	
	public static HashMap getIsSelected() {
	   return isSelected;
	}

	public static void setIsSelected(HashMap isSelected) {
		BacklogAdapter.isSelected = isSelected;
	}
	

    public final class ViewHolder {        
        public TextView tv_title;
        public TextView tv_priority;
        public CheckBox cb;
        public Button btn;
    }
}
