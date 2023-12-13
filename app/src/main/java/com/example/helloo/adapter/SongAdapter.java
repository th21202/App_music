package com.example.helloo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.helloo.R;
import com.example.helloo.model.Song;

import java.util.List;

public class SongAdapter extends ArrayAdapter<Song> {

    public SongAdapter(Context context, List<Song> songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // Mục chưa được vẽ, tạo ViewHolder mới
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.song_list_item, parent, false);

            // Ánh xạ các thành phần từ layout
            viewHolder.titleTextView = convertView.findViewById(R.id.songTitleTextView);
            viewHolder.artistTextView = convertView.findViewById(R.id.artistNameTextView);
            viewHolder.songIconImageView = convertView.findViewById(R.id.songIconImageView);

            // Lưu ViewHolder vào convertView để tái sử dụng
            convertView.setTag(viewHolder);
        } else {
            // Mục đã được vẽ, lấy ViewHolder từ convertView
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Lấy dữ liệu từ danh sách và điền vào các thành phần của ViewHolder
        Song currentSong = getItem(position);
        if (currentSong != null) {
            viewHolder.titleTextView.setText(currentSong.getTitle());
            viewHolder.artistTextView.setText(currentSong.getArtist());
        }

        return convertView;
    }

    @Override
    public Song getItem(int position) {
        return super.getItem(position);
    }
    private static class ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        ImageView songIconImageView;
        // Các thành phần khác nếu có
    }
}
