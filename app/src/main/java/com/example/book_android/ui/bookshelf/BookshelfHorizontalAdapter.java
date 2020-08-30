package com.example.book_android.ui.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_android.R;
import com.example.book_android.dataclass.BookshelfItem;

import java.util.ArrayList;


public class BookshelfHorizontalAdapter extends RecyclerView.Adapter<BookshelfHorizontalAdapter.ViewHolder> {

    private ArrayList<BookshelfItem> mData;

    BookshelfHorizontalAdapter(ArrayList<BookshelfItem> list){
        this.mData = list;
    }

    private OnRecordBtnClickListener mListener = null ;

    public interface OnRecordBtnClickListener {
        void onRecordBtnClick(View v, int position, BookshelfItem item) ;
    }

    public void setOnRecordBtnClickListener(OnRecordBtnClickListener listener) {
        this.mListener = listener ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImg;
        TextView recordBtn;

        ViewHolder(final View itemView){
            super(itemView);
            bookImg = itemView.findViewById(R.id.bookshelf_book_img);
            recordBtn = itemView.findViewById(R.id.bookshelf_record_btn);
            recordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        BookshelfItem item = mData.get(pos);
                        mListener.onRecordBtnClick(view, pos, item);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bookshelf_item, parent, false);
        BookshelfHorizontalAdapter.ViewHolder vh = new BookshelfHorizontalAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookshelfItem item = mData.get(position);
        holder.bookImg.setImageResource(item.getImg());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
