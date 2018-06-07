package com.acadview.www.aq;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class CategoryFragment extends Fragment{

    View myFragment;
    RecyclerView listCategory ;
    RecyclerView.LayoutManager layoutManager;

    public static CategoryFragment newInstance(){
        CategoryFragment categoryFragment =new CategoryFragment();
        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       myFragment =inflater.inflate(R.layout.fragment_category,container,false);

        listCategory = (RecyclerView)myFragment.findViewById(R.id.categorylist);
        ItemData itemdatas[]= {new ItemData("Java",R.drawable.javao1)
                ,new ItemData("Python",R.drawable.python)
                ,new ItemData("ML",R.drawable.machinelearning)
                ,new ItemData("PHP",R.drawable.php)
                ,new ItemData("Android",R.drawable.android)};

        listCategory.setLayoutManager(new GridLayoutManager(getContext(),2));
        MyAdapter myAdapter =new MyAdapter(itemdatas);
        listCategory.setNestedScrollingEnabled(true);
        listCategory.setAdapter(myAdapter);
        listCategory.setHasFixedSize(true);
        listCategory.setItemAnimator(new DefaultItemAnimator());

        listCategory.setItemAnimator(new DefaultItemAnimator());

        return myFragment ;
    }


}
