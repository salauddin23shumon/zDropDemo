package com.sss.zdropdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Insets;
import android.graphics.drawable.Drawable;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sss.zdropdemo.adapter.HorizontalItemAdapter;
import com.sss.zdropdemo.utility.CartConverter;
import com.sss.zdropdemo.utility.ImageList;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.sss.zdropdemo.utility.ImageHelper.getRoundedCornerBitmap;


public class ProductFragment extends Fragment implements HorizontalItemAdapter.ShowProductDialog {


    LinearLayout  menuLayout;
    ImageView productImg, cartIV;
    Button cartBtn;
    int cart_count = 0, counter = 0, h = 0, w = 0;
    Context context;
    TextView menuTv;
    View mainView;
    RecyclerView horizontalRV;
    AppBarLayout appBarLayout;
    BottomSheetDialog dialog;
    ImageButton backBtn;
    Drawable backBtnBg, menuLayoutBg;
    ViewFlipper viewFlipper;
    ImageView itemImage;
    ImageButton btnNxt, btnPrev;
    HorizontalItemAdapter adapter;
    String TAG = "ProductFragment";
    List<ImageList> lists = new ArrayList<>();
    List<ImageView> imageViewList;
    int selected_position;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_product, container, false);
        dialog = new BottomSheetDialog(context, R.style.DialogTheme);
        horizontalRV = mainView.findViewById(R.id.horizontalRV);
        cartBtn = mainView.findViewById(R.id.btnCart);
        cartIV = mainView.findViewById(R.id.cartIV);
        productImg = mainView.findViewById(R.id.productIV);
        menuTv = mainView.findViewById(R.id.searchMenu);
        appBarLayout = mainView.findViewById(R.id.appBarLayout);
        backBtn = mainView.findViewById(R.id.backBtn);
        menuLayout = mainView.findViewById(R.id.menuLL);

        backBtnBg = backBtn.getBackground();
        menuLayoutBg = menuLayout.getBackground();

        createImageList();

        adapter = new HorizontalItemAdapter(context, lists, this);
        horizontalRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        horizontalRV.setAdapter(adapter);


        return mainView;
    }

    private void createImageList() {
        for (int i = 0; i < 12; i++) {
            lists.add(new ImageList(i, R.drawable.keds_small));
        }
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
                        if (item.getItemId() == R.id.productOp) {

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
                    isShow = true;
                } else if (isShow) {
                    backBtn.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_shape_grey_tr));
                    menuLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.capsule_shape_grey_tr));
                    isShow = false;

                }
            }

        });


    }


    private void showDialog() {

        View dialogView = LayoutInflater.from(context).
                inflate(R.layout.dialog_layout, (ScrollView) mainView.findViewById(R.id.dialog_container));

        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        setDialogView(dialogView);

        dialog.setContentView(dialogView);

        setupFullHeight(dialog);

        dialog.show();


    }

    private void setDialogView(View dialogView) {

        LinearLayout dialogHorizontalImgLL;
        final TextView itemCounter;
        Button closeBtn, incBtn, decBtn;
        selected_position = -1;
        imageViewList = new ArrayList<>();

        List<ImageList> images = new ArrayList<>();
        images.add(new ImageList(0, R.drawable.jacket));
        images.add(new ImageList(1, R.drawable.keds_small));
        images.add(new ImageList(2, R.drawable.product_main));
        images.add(new ImageList(3, R.drawable.jacket_small));
        images.add(new ImageList(4, R.drawable.keds_small));
        images.add(new ImageList(5, R.drawable.jacket_small));
        images.add(new ImageList(6, R.drawable.keds_small));
        images.add(new ImageList(7, R.drawable.baggy_small));
        images.add(new ImageList(8, R.drawable.jacket_small));


        dialogHorizontalImgLL = dialogView.findViewById(R.id.horizontalImgLayout);
        closeBtn = dialogView.findViewById(R.id.closeBtn);
        decBtn = dialogView.findViewById(R.id.decrementBtn);
        btnNxt = dialogView.findViewById(R.id.btnNxt);
        btnPrev = dialogView.findViewById(R.id.btnPrev);

        viewFlipper = dialogView.findViewById(R.id.flipper);
        incBtn = dialogView.findViewById(R.id.incrementBtn);
        itemCounter = dialogView.findViewById(R.id.quantity_counterTV);

        LayoutInflater itemInflater = LayoutInflater.from(context);


        for (final ImageList img : images) {
            View horizontalItemView = itemInflater.inflate(R.layout.horizontal_item, dialogHorizontalImgLL, false);
            itemImage = horizontalItemView.findViewById(R.id.hr_item_iv);
            itemImage.setTag(img.getIndex());
            itemImage.setImageResource(img.getDrawableId());
            itemImage.setClickable(true);
            itemImage.setFocusable(true);
            itemImage.setBackground(ContextCompat.getDrawable(context, R.drawable.grey_border));
            setImageInFlipper(img);
            dialogHorizontalImgLL.addView(horizontalItemView);


            imageViewList.add(itemImage);

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewFlipper.setDisplayedChild(img.getIndex());
                    setButton();
                    itemImage.setSelected(true);
                    Log.d(TAG, "onclick: child" + viewFlipper.getDisplayedChild());
                    setMarker(viewFlipper.getDisplayedChild());
                }
            });
        }

        Log.e(TAG, "setDialogView: size" + imageViewList.size());

//        Log.d(TAG, "setview: " + viewFlipper.getDisplayedChild());

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
                if (counter >= 1) {
                    counter--;
                    itemCounter.setText(String.valueOf(counter));
                }
            }
        });

        btnNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.setInAnimation(context, R.anim.slide_in_right);
                viewFlipper.setOutAnimation(context, R.anim.slide_out_left);
                viewFlipper.showNext();
                setButton();
                setMarker(viewFlipper.getDisplayedChild());
//                Log.d(TAG, "setview: " + viewFlipper.getDisplayedChild());
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.setInAnimation(context, android.R.anim.slide_in_left);
                viewFlipper.setOutAnimation(context, android.R.anim.slide_out_right);
                viewFlipper.showPrevious();
                setButton();
                setMarker(viewFlipper.getDisplayedChild());
            }
        });
    }

    private void setImageInFlipper(ImageList img) {
        setButton();
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        ImageView imageView = new ImageView(context);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), img.getDrawableId());
        imageView.setLayoutParams(params);
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(getRoundedCornerBitmap(icon, 30));
        viewFlipper.addView(imageView);

        Log.d(TAG, "setImageInFlipper: " + viewFlipper.getDisplayedChild());

    }


    private void setMarker(int pos) {

        Log.e(TAG, "setMarker: " + pos + " " + selected_position);

        if (selected_position == -1) {
            selected_position = pos;
            imageViewList.get(pos).setBackground(ContextCompat.getDrawable(context, R.drawable.selected_item_green_border));
            Log.e(TAG, "selected_position: if " + selected_position);
        } else if (selected_position > -1) {

            Log.d(TAG, "elseif pos : " + pos);
            imageViewList.get(pos).setBackground(ContextCompat.getDrawable(context, R.drawable.selected_item_green_border));

            Log.e(TAG, "selected_position: elseif " + selected_position);
            imageViewList.get(selected_position).setBackground(ContextCompat.getDrawable(context, R.drawable.grey_border));

            selected_position = pos;
        } else {
            Log.e(TAG, "setMarker: else");
        }
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
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
        int height = 0;
        int width = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = ((Activity) context).getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getDisplay().getRealMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            Log.e(TAG, "getWindowHeight: h: " + height + " w: " + width);
            return (int) (height - (height * .15));

        } else {
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            height = displayMetrics.heightPixels;
            return (int) (height - (height * .10));

        }
    }

    private void setButton() {
        Log.d(TAG, "setImageInFlipper: " + viewFlipper.getDisplayedChild());
        if (viewFlipper.getDisplayedChild() == 0) {
            btnPrev.setVisibility(View.INVISIBLE);
            btnNxt.setVisibility(View.VISIBLE);
        } else if (viewFlipper.getDisplayedChild() == 8) {
            btnPrev.setVisibility(View.VISIBLE);
            btnNxt.setVisibility(View.INVISIBLE);
        } else {
            btnPrev.setVisibility(View.VISIBLE);
            btnNxt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHorizontalItemClick(int pos) {
        showDialog();
        Log.e(TAG, "onHorizontalItemClick: " + pos);
    }
}