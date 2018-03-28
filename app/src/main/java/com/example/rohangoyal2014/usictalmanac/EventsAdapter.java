package com.example.rohangoyal2014.usictalmanac;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder>{

    private ArrayList<EventsModel> eventsModelArrayList;

    EventsAdapter(ArrayList<EventsModel> arrayList)
    {
        eventsModelArrayList=new ArrayList<>(arrayList);
    }
    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return eventsModelArrayList.size();
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder{
        private TextView eventNameView,eventDetailView,eventDateTime;
        private ImageView eventImage;
        EventsViewHolder(View view){
            super(view);
            eventNameView=view.findViewById(R.id.event_title);
            eventDetailView=view.findViewById(R.id.event_summary);
            eventDateTime=view.findViewById(R.id.date_time);
            eventImage=view.findViewById(R.id.event_image);
        }
        private void bind(int position){
            EventsModel eventsModel=eventsModelArrayList.get(position);
            eventNameView.setText(eventsModel.getEventName());
            String eventDetails=eventsModel.getEventDetail();
            if(eventDetails.length()>50)
            {
                eventDetails=eventDetails.substring(0,47);
                eventDetails=eventDetails.concat("...");
            }
            eventDetailView.setText(eventDetails);
        }
    }
}
