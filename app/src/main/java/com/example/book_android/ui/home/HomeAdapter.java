package com.example.book_android.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.R;
import com.example.book_android.dataclass.HomeItem;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<HomeItem> mData = null;

    private Bitmap bitmap;
    //private String img;
    private int img;
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImg;
        TextView bookTitle;
        TextView bookWriter;
        TextView bookNum;

        ViewHolder(final View itemView){
            super(itemView);
            bookImg = itemView.findViewById(R.id.book_img);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookWriter = itemView.findViewById(R.id.book_writer);
            bookNum = itemView.findViewById(R.id.book_num);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(mListener != null){
                            mListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }
    }

    HomeAdapter(ArrayList<HomeItem> list){
        mData = list;
        notifyDataSetChanged();
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.home_item, parent, false);
        HomeAdapter.ViewHolder vh = new HomeAdapter.ViewHolder(view);

        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(HomeAdapter.ViewHolder holder, int position) {
        img = mData.get(position).getBookImg();
        if(img == 0){
            holder.bookImg.setImageResource(R.drawable.bookimg_ex);
        }
        else{
            //downloadProfile();
            //holder.bookImg.setImageBitmap(bitmap);
            holder.bookImg.setImageResource(img);
        }
        String title = mData.get(position).getBookTitle();
        String writer = mData.get(position).getWriter();
        holder.bookTitle.setText(title);
        holder.bookWriter.setText(writer);
        holder.bookNum.setText(String.valueOf(position + 1));
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size();
    }

    private void downloadProfile(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("http://52.79.242.93:8000/media/userprofile/"+img);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        };
        thread.start();
        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void setData(ArrayList<HomeItem> list){
        mData = list;
        notifyDataSetChanged();
    }
}
