package com.Bookey.book_android.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.Bookey.book_android.MainActivity;
import com.Bookey.book_android.R;
import com.bumptech.glide.Glide;
import com.Bookey.book_android.dataclass.HomeItem;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<HomeItem> mData = null;

    private MainActivity activity;
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

    HomeAdapter(ArrayList<HomeItem> list, MainActivity activity){
        mData = list;
        this.activity = activity;
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
        String imgUrl = mData.get(position).getBookImg();
        Glide.with(activity).load(imgUrl).into(holder.bookImg);
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

    public void setData(ArrayList<HomeItem> list){
        mData = list;
        notifyDataSetChanged();
    }
}
