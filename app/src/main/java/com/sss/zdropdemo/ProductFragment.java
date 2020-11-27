package com.sss.zdropdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Insets;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sss.zdropdemo.utility.CartConverter;


public class ProductFragment extends Fragment {


    LinearLayout linearLayout;
    ImageView productImg, cartIV;
    Button cartBtn;
    static int cart_count = 0;
    Context context;
    TextView menuTv;
    View mainView;
    BottomSheetDialog dialog;

    String TAG="ProductFragment";

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView= inflater.inflate(R.layout.fragment_product, container, false);

        linearLayout=mainView.findViewById(R.id.imgLayout);
        cartBtn=mainView.findViewById(R.id.btnCart);
        cartIV=mainView.findViewById(R.id.cartIV);
        productImg=mainView.findViewById(R.id.productIV);
        menuTv=mainView.findViewById(R.id.searchMenu);

        LayoutInflater itemInflater= LayoutInflater.from(context);

        for (int i=0; i<12; i++){
            View horizontalItemView= itemInflater.inflate(R.layout.horizontal_item,linearLayout,false);
            ImageView itemImage = horizontalItemView.findViewById(R.id.hr_item_iv);
            itemImage.setImageResource(R.drawable.keds);

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    buildDialog(R.style.DialogAnimation, "dialog");
                    showDialog();
                }
            });

            linearLayout.addView(horizontalItemView);
        }

        return mainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart_count++;
                cartIV.setImageDrawable(CartConverter.convertLayoutToImage(context, cart_count, R.drawable.cart));
            }
        });

        productImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        menuTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, menuTv);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.option_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId()==R.id.productOp){

                    }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }


    private void showDialog() {
        dialog = new BottomSheetDialog(context, R.style.DialogTheme);
        View dialogView = LayoutInflater.from(context).
                inflate(R.layout.dialog_layout, (ConstraintLayout) mainView.findViewById(R.id.dialog_container));

        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        setDialogView(dialogView);

        dialog.setContentView(dialogView);

        setupFullHeight(dialog);

        dialog.show();


    }

    private void setDialogView(View dialogView) {

        LinearLayout dialogHorizontalImgLL;
        TextView closeTV;
        Button closeBtn;

        dialogHorizontalImgLL=dialogView.findViewById(R.id.horizontalImgLayout);
        closeBtn=dialogView.findViewById(R.id.closeBtn);

        LayoutInflater itemInflater= LayoutInflater.from(context);

        for (int i=0; i<12; i++){
            View horizontalItemView= itemInflater.inflate(R.layout.horizontal_item,dialogHorizontalImgLL,false);
            ImageView itemImage = horizontalItemView.findViewById(R.id.hr_item_iv);
            itemImage.setImageResource(R.drawable.jacket_small);

            dialogHorizontalImgLL.addView(horizontalItemView);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        int height=0;
        int width=0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity)context).getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();

//            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            context.getDisplay().getRealMetrics(displayMetrics);
            height= displayMetrics.heightPixels;
            width=displayMetrics.widthPixels;
            Log.e(TAG, "getWindowHeight: h: "+ height + " w: " + width);
            return height-440;
        }
    }

}