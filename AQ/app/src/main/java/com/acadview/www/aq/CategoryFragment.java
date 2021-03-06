package com.acadview.www.aq;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

        listCategory = myFragment.findViewById(R.id.categorylist);
        ItemData itemdatas[]= {new ItemData("Java",R.drawable.ic_java_icon)
                ,new ItemData("Python",R.drawable.ic_python_icon)
                ,new ItemData("PHP",R.drawable.ic_php_icon)
                ,new ItemData("Android",R.drawable.ic_android_icon)
                ,new ItemData("Coming Soon",R.drawable.ic_objectivec_logo)
                ,new ItemData("Coming Soon",R.drawable.ic_machine_learning)
                ,new ItemData("Coming Soon",R.drawable.ic_java_script)};
        listCategory.setLayoutManager(new GridLayoutManager(getContext(),2));
        MyAdapter myAdapter =new MyAdapter(itemdatas);
        listCategory.setAdapter(myAdapter);
        listCategory.setItemAnimator(new DefaultItemAnimator());

        return myFragment ;
    }


}
