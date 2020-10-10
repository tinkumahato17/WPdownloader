package in.santiniketan.wpdownloader;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private Context context;
    private ArrayList<Object> filesList;

    public StoryAdapter(Context context, ArrayList<Object> filesList) {
        this.context = context;
        this.filesList = filesList;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_row,null,false);

        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, final int position) {

        final StoryModel files = (StoryModel) filesList.get(position);

        if (files.getUri().toString().endsWith(".mp4")) {
            holder.playIcon.setVisibility(View.VISIBLE);

        }
        else{
        holder.playIcon.setVisibility(View.INVISIBLE);
    }
        Glide.with(context)
                .load(files.getUri())
                .into(holder.saveImage);


        holder.downloadID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFolder();

                final String path = ((StoryModel) filesList.get(position)).getPath();
                final File file = new File(path);

                String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
                File destFile = new File(destPath);


                try {
                    FileUtils.copyFileToDirectory(file,destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MediaScannerConnection.scanFile(
                        context,
                        new String[]{destPath + files.getFilename()},
                        new String[]{"*/*"},
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {

                            }

                            @Override
                            public void onScanCompleted(String s, Uri uri) {

                            }
                        }


                );

                Toast.makeText(context,"Saved to:"+destPath+files.getFilename(),Toast.LENGTH_SHORT).show();
            }



        });

    }

    private void checkFolder() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;

        File dir = new File(path);

        boolean isDirectoryCreated = dir.exists();

        if (!isDirectoryCreated){
            isDirectoryCreated = dir.mkdir();

        }
        if (isDirectoryCreated){
            Log.d("Folder","Already Created");
        }
    }

    @Override
    public int getItemCount() {

        return filesList.size();
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder{

        ImageView playIcon,downloadID,saveImage;

        public StoryViewHolder(@NonNull View itemView){
            super(itemView);

            saveImage = itemView.findViewById(R.id.mainImageView);
            playIcon = itemView.findViewById(R.id.playButtonImage);
            downloadID = itemView.findViewById(R.id.downloadID);

        }

    }
}
