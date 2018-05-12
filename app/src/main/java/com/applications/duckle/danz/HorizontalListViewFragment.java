package com.applications.duckle.danz;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class HorizontalListViewFragment extends Fragment {

    ArrayList<SongMenuItem> listitems = new ArrayList<>();
    RecyclerView MyRecyclerView;
    String songs[] = {"Chicken Dance","Levels"};
    int  songImages[] = {R.drawable.chickendance,R.drawable.levels};
    int selected_position = 0;
    public String selected_song = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listitems.clear();
        for(int i =0;i<songs.length;i++){
            SongMenuItem item = new SongMenuItem();
            item.setCardName(songs[i]);
            item.setImageResourceId(songImages[i]);
            item.setIsturned(0);
            listitems.add(item);
        }
        getActivity().setTitle("Songs");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);
        MyRecyclerView = (RecyclerView) view.findViewById(R.id.cardView);
        MyRecyclerView.setHasFixedSize(true);
        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (listitems.size() > 0 & MyRecyclerView != null) {
            MyRecyclerView.setAdapter(new MyAdapter(listitems));
        }
        MyRecyclerView.setLayoutManager(MyLayoutManager);

        MyRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), MyRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SongMenuItem song = listitems.get(position);
                Toast.makeText(getActivity().getApplicationContext(), song.getCardName() + " is selected!", Toast.LENGTH_SHORT).show();
                selected_position = position;
                resetSelection();
                listitems.get(position).textView.setBackgroundColor(Color.parseColor("#fb01fd"));
                selected_song = song.getCardName();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }

    private void resetSelection(){
        selected_song = null;
        for(SongMenuItem s : listitems){
            s.textView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<SongMenuItem> list;

        public MyAdapter(ArrayList<SongMenuItem> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.titleTextView.setText(list.get(position).getCardName());
            holder.coverImageView.setImageResource(list.get(position).getImageResourceId());
            holder.coverImageView.setTag(list.get(position).getImageResourceId());
            list.get(position).textView = holder.titleTextView;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView titleTextView;
        public ImageView coverImageView;

        public MyViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.titleTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);
        }

    }
}