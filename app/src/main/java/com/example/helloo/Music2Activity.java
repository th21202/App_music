package com.example.helloo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.ImageView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.helloo.adapter.SongAdapter;
import com.example.helloo.model.Song;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Music2Activity extends AppCompatActivity {


    private ImageButton playPauseButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;
    private ImageView albumArtImageView;
    private Animation rotateAnimation;
    private TextView songTitleTextView;
    private TextView artistNameTextView;
    private List<Song> songList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music2);

        // Ánh xạ các thành phần từ layout
        TextView songTitleTextView = findViewById(R.id.songTitleTextView);
        TextView artistNameTextView = findViewById(R.id.artistNameTextView);
        albumArtImageView = findViewById(R.id.albumArtImageView);
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_animation);
        playPauseButton = findViewById(R.id.playPauseButton);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        seekBar = findViewById(R.id.seekBar);
        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        handler = new Handler();
        // Tạo danh sách bài hát từ thư mục raw
        songList = getSongList();
        // Call this method to start the rotation animation when the music starts playing
        startRotateAnimation();

        setupMediaPlayer();
        setupSeekBar();
        setupButtons();
        // Gán giá trị cho TextView
        songTitleTextView.setText("Default Song Title");
        artistNameTextView.setText("Default Artist Name");

        // Tạo danh sách bài hát từ thư mục raw
        List<Song> songList = getSongList();
        Log.d("Music2Activity", "Song List Size: " + songList.size());

        // Tìm kiếm Button và ListView theo ID
        Button showPlaylistButton = findViewById(R.id.showPlaylistButton);




        // Xử lý sự kiện khi nhấn vào Button
        showPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaylistDialog();
            }
        });
        // Xử lý sự kiện khi nhấn vào Button Next
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong1();
            }
        });
        // Xử lý sự kiện khi nhấn vào Button Previous
        previousButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               playPreviousSong1();
            }
        });

        // ... (your existing code)

    }

    // Method to show the playlist dialog
    private void showPlaylistDialog() {
        // Tạo Dialog
        Dialog dialog = new Dialog(Music2Activity.this);
        dialog.setContentView(R.layout.dialog_playlist);

        // Ánh xạ ListView trong Dialog
        ListView dialogListView = dialog.findViewById(R.id.dialogListView);


        // Tạo danh sách bài hát từ thư mục raw
        List<Song> songList = getSongList();

        // Tạo adapter và kết nối với ListView trong Dialog
        final PlaylistDialogAdapter playlistDialogAdapter = new PlaylistDialogAdapter(Music2Activity.this, songList);
        dialogListView.setAdapter(playlistDialogAdapter);

        // Xử lý sự kiện khi chọn một bài hát từ danh sách trong Dialog
        dialogListView.setOnItemClickListener((parent, view, position, id) -> {
            Song selectedSong = playlistDialogAdapter.getItem(position);



            // Phát nhạc và bắt đầu quay logo
            playSong(selectedSong);
            startRotateAnimation();
        });

        // Hiển thị Dialog
        dialog.show();

    }


    // Lớp PlaylistDialogAdapter mới
    public class PlaylistDialogAdapter extends ArrayAdapter<Song> {
        private  class ViewHolder {
            TextView titleTextView;
            TextView artistTextView;
            ImageView songIconImageView;
        }
        public PlaylistDialogAdapter(Context context, List<Song> songs) {
            super(context, 0, songs);
        }
        @NonNull

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.song_list_item, parent, false);

                holder = new ViewHolder();
                holder.titleTextView = convertView.findViewById(R.id.songTitleTextView);
                holder.artistTextView = convertView.findViewById(R.id.artistNameTextView);
                holder.songIconImageView = convertView.findViewById(R.id.songIconImageView);
                // Ánh xạ thêm các phần tử khác nếu có

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // Lấy dữ liệu từ Song tại vị trí position
            Song song = getItem(position);

            // Đặt dữ liệu vào các phần tử
            if (song != null) {
                holder.titleTextView.setText(song.getTitle());
                holder.artistTextView.setText(song.getArtist());
                // Đặt dữ liệu vào các phần tử khác nếu có
            }

            return convertView;
        }

    }

    private void playNextSong1() {
        try {
            // Add your existing code for playing the next song here

            // Example logging
            Log.d("Music2Activity", "playNextSong: Successfully played next song");
        } catch (Exception e) {
            // Log any exceptions for debugging
            Log.e("Music2Activity", "playNextSong: Error", e);
        }
        int currentSongIndex = getCurrentSongIndex();
        int nextSongIndex = (currentSongIndex + 1) % songList.size(); // Circular playlist

        Song nextSong = songList.get(nextSongIndex);
        playSong(nextSong);
    }

    private void playPreviousSong1() {
        int currentSongIndex = getCurrentSongIndex();


        int previousSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size(); // Circular playlist

        Song previousSong = songList.get(previousSongIndex);
        playSong(previousSong);
    }



    private int getCurrentSongIndex() {
        // Find the index of the currently playing song in the songList
        // You can implement this based on the current song's title or any other identifier
        // For simplicity, let's assume that the song titles are unique
        String currentSongTitle = songTitleTextView.getText().toString();
        for (int i = 0; i < songList.size(); i++) {
            if (songList.get(i).getTitle().equals(currentSongTitle)) {
                return i;
            }
        }
        return -1; // Not found
    }
    private List<Song> getSongList() {
        List<Song> songs = new ArrayList<>();

        // Adding song1.mp4
        int song1ResourceId = getResources().getIdentifier("song1", "raw", getPackageName());
        songs.add(new Song("song1.mp4", "Unknown Artist", getDuration(song1ResourceId)));

        // Adding song2.mp3
        int song2ResourceId = getResources().getIdentifier("song2", "raw", getPackageName());
        songs.add(new Song("song2.mp3", "Unknown Artist", getDuration(song2ResourceId)));
        return songs;
    }
    private void playSong(Song song) {
        // Stop and release the current MediaPlayer (if any)
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Get the resource ID for the selected song
        int resourceId = getResources().getIdentifier(song.getTitle(), "raw", getPackageName());

        // Check the file extension to determine the appropriate method
        if (song.getTitle().endsWith(".mp3")) {
            // For MP3 files, use MediaPlayer.create directly
            mediaPlayer = MediaPlayer.create(this, resourceId);
        } else if (song.getTitle().endsWith(".mp4")) {
            // For MP4 files, create a new MediaPlayer and set the data source
            mediaPlayer = new MediaPlayer();
            try {
                AssetFileDescriptor afd = getResources().openRawResourceFd(resourceId);
                if (afd != null) {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    afd.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Update the title and artist on the interface
        if (songTitleTextView != null && artistNameTextView != null) {
            songTitleTextView.setText(song.getTitle());
            artistNameTextView.setText(song.getArtist());
        }

        // Start playing the song and initiate the rotation animation
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> playNextSong());
            mediaPlayer.start();
            startRotateAnimation();
        }
        }


    private int getDuration(int resourceId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, resourceId);
        int duration = mediaPlayer.getDuration();
        mediaPlayer.release();
        return duration;
    }

    //router
    private void startRotateAnimation() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            albumArtImageView.startAnimation(rotateAnimation);
        }
    }

    private void stopRotateAnimation() {
        albumArtImageView.clearAnimation();
    }
    private void setupMediaPlayer() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Xử lý khi bài hát kết thúc
                // Ví dụ: Chuyển đến bài hát tiếp theo
                playNextSong();
            }
        });
    }

    private void setupSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());

        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1000); // Cập nhật SeekBar mỗi giây
            }
        };

        handler.postDelayed(runnable, 1000);

        // Xử lý sự kiện khi người dùng di chuyển SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Xử lý thay đổi vị trí của bài hát
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Tạm dừng bài hát khi người dùng bắt đầu di chuyển SeekBar
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Đặt lại vị trí của bài hát khi người dùng kết thúc di chuyển SeekBar
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                }
            }
        });
    }

    private void setupButtons() {
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
            }
        });
    }
    ///

    private void togglePlayPause() {
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
//        } else {
//            mediaPlayer.start();
//            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
//        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            stopRotateAnimation(); // Stop rotation when music is paused
        } else {
            mediaPlayer.start();
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            startRotateAnimation(); // Start rotation when music is playing
        }
    }

    private void playNextSong() {
        // Xử lý khi người dùng nhấn nút next
        // Ví dụ: Chuyển đến bài hát tiếp theo trong danh sách
    }

    private void playPreviousSong() {
        // Xử lý khi người dùng nhấn nút previous
        // Ví dụ: Chuyển đến bài hát trước đó trong danh sách
    }

    @Override
    protected void onDestroy() {
//
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(runnable);
        stopRotateAnimation(); // Stop rotation when activity is destroyed

//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//        handler.removeCallbacks(runnable);
    }
}