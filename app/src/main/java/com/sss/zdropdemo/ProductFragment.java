package com.sss.zdropdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sss.zdropdemo.utility.CartConverter;


public class ProductFragment extends Fragment {


    LinearLayout linearLayout, menuLayout;
    ImageView productImg, cartIV;
    Button cartBtn;
    int cart_count = 0;
    int counter = 0;
    Context context;
    TextView menuTv;
    View mainView;
    AppBarLayout appBarLayout;
    BottomSheetDialog dialog;
    ImageButton backBtn;
    Drawable backBtnBg, menuLayoutBg;
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
        appBarLayout=mainView.findViewById(R.id.appBarLayout);
        backBtn=mainView.findViewById(R.id.backBtn);
        menuLayout=mainView.findViewById(R.id.menuLL);

        backBtnBg=backBtn.getBackground();
        menuLayoutBg=menuLayout.getBackground();


        LayoutInflater itemInflater= LayoutInflater.from(context);

        for (int i=0; i<12; i++){
            View horizontalItemView= itemInflater.inflate(R.layout.horizontal_item,linearLayout,false);
            final ImageView itemImage = horizontalItemView.findViewById(R.id.hr_item_iv);
            itemImage.setImageResource(R.drawable.keds);

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    buildDialog(R.style.DialogAnimation, "dialog");
                    showDialog();
                    itemImage.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_item_green_border));
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

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                boolean isShow = true;
                int scrollRange = -1;
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {    //collapsed
                    backBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_shape_white));
                    menuLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.capsule_shape_white));
                    isShow=true;
                } else if (isShow) {
                    backBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_shape_grey_tr));
                    menuLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.capsule_shape_grey_tr));
                    isShow = false;

                }
            }

        });
    }


    private void showDialog() {
        dialog = new BottomSheetDialog(context, R.style.DialogTheme);
        View dialogView = LayoutInflater.from(context).
                inflate(R.layout.dialog_layout, (ScrollView) mainView.findViewById(R.id.dialog_container));

        dialog.setCancelable(true);
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        setDialogView(dialogView);

        dialog.setContentView(dialogView);

        setupFullHeight(dialog);

        dialog.show();


    }

    private void setDialogView(View dialogView) {

        LinearLayout dialogHorizontalImgLL;
        final TextView itemCounter;
        Button closeBtn, incBtn, decBtn;

        dialogHorizontalImgLL=dialogView.findViewById(R.id.horizontalImgLayout);
        closeBtn=dialogView.findViewById(R.id.closeBtn);
        decBtn=dialogView.findViewById(R.id.decrementBtn);
        incBtn=dialogView.findViewById(R.id.incrementBtn);
        itemCounter=dialogView.findViewById(R.id.quantity_counterTV);

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

        incBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                itemCounter.setText(String.valueOf(counter));
            }
        });

        decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter >= 1){
                    counter--;
                    itemCounter.setText(String.valueOf(counter));
                }
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
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity)context).getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getDisplay().getRealMetrics(displayMetrics);
            height= displayMetrics.heightPixels;
            width=displayMetrics.widthPixels;
            Log.e(TAG, "getWindowHeight: h: "+ height + " w: " + width);
            return height-440;

        }else {
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height= displayMetrics.heightPixels;
            return height-240;

        }
    }

}