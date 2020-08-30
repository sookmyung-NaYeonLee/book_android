package com.example.book_android.ui.bookshelf;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.R;
import com.example.book_android.RecordActivity;
import com.example.book_android.dataclass.BookshelfItem;

import java.util.ArrayList;

public class BookshelfVerticalAdapter extends RecyclerView.Adapter<BookshelfVerticalAdapter.ViewHolder> {

    private ArrayList<ArrayList<BookshelfItem>> mDataList;
    private Context context;

    private OnRecordBtnClickListener2 mListener = null ;

    public interface OnRecordBtnClickListener2 {
        void onRecordBtnClick2(BookshelfItem item) ;
    }

    public void setOnRecordBtnClickListener2(OnRecordBtnClickListener2 listener) {
        this.mListener = listener ;
    }

    public BookshelfVerticalAdapter(Context context, ArrayList<ArrayList<BookshelfItem>> data){
        mDataList = data;
        this.context = context;
    }

    public void setmDataList(ArrayList<ArrayList<BookshelfItem>> list){
        mDataList = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerView recyclerView;
        ImageView background;

        public ViewHolder(final View itemView){
            super(itemView);
            recyclerView = itemView.findViewById(R.id.bookshelf_horizontal_recycler);
            background = itemView.findViewById(R.id.bookshelf_horizontal_img);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bookshelf_vertical_item, parent, false);
        BookshelfVerticalAdapter.ViewHolder vh = new BookshelfVerticalAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<BookshelfItem> list = mDataList.get(position);
        if(list.size() == 0){
            holder.background.setVisibility(View.VISIBLE);
        }
        BookshelfHorizontalAdapter adapter = new BookshelfHorizontalAdapter(list);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
        adapter.setOnRecordBtnClickListener(new BookshelfHorizontalAdapter.OnRecordBtnClickListener() {
            @Override
            public void onRecordBtnClick(View v, int position, BookshelfItem item) {
                mListener.onRecordBtnClick2(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
