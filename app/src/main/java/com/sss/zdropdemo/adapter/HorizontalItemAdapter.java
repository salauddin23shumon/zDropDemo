package com.sss.zdropdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sss.zdropdemo.R;
import com.sss.zdropdemo.utility.ImageList;

import java.util.List;

public class HorizontalItemAdapter extends RecyclerView.Adapter<HorizontalItemAdapter.CustomVH> {

    private Context context;
    private List<ImageList> imageLists;
    private Fragment fragment;
    private ShowProductDialog productDialog;
    private int selected_position = -1;
    private String TAG="HorizontalItemAdapter";

    public HorizontalItemAdapter(Context context, List<ImageList> imageLists, Fragment fragment) {
        this.context = context;
        this.imageLists = imageLists;
        this.fragment = fragment;
        productDialog= (ShowProductDialog) fragment;
    }

    @NonNull
    @Override
    public CustomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.horizontal_item,parent,false);
        return new CustomVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomVH holder, final int position) {

        ImageList image=imageLists.get(position);
        holder.imageView.setImageResource(image.getDrawableId());


        if (selected_position == position) {
            holder.imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_item_green_border));

        } else {
            holder.imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_border));
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productDialog.onHorizontalItemClick(position);

                if(selected_position==position){
                    selected_position=-1;
                    notifyDataSetChanged();
                    return;
                }
                selected_position = position;
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+imageLists.size());
        return imageLists.size();
    }

    public class CustomVH extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public CustomVH(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.hr_item_iv);
        }
    }

    public interface ShowProductDialog{
        void onHorizontalItemClick(int pos);
    }
}
