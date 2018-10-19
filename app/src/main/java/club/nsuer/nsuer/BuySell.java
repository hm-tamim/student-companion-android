package club.nsuer.nsuer;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class BuySell extends Fragment {


    private View view;

    private MainActivity main;
    private Context context;
    private ArrayList<BuySellItem> itemList;
    private BuySellAdapter itemAdapter;
    private RecyclerView recyclerView;

    public BuySell() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        main = (MainActivity) getActivity();
        context =getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.buy_sell_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        main.setActionBarTitle("Buy-Sell Shop");

        itemList = new ArrayList<BuySellItem>();
        itemAdapter = new BuySellAdapter(R.layout.buy_sell_recycler, itemList, context, this);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        itemList.add(new BuySellItem(0,"HM Tamim",20,"ECO101 Book By Andro 7th Edition","120 TK","20.jpg",1534356000,2));



        itemList.add(new BuySellItem(0,"HM Tamim",20,"ECO101 Book","120 TK","20.jpg",1534356000,2));


        itemList.add(new BuySellItem(0,"HM Tamim",20,"Introduction to Java Programming","120 TK","20.jpg",1534356000,2));

        itemList.add(new BuySellItem(0,"HM Tamim",20,"ECO101 Book","120 TK","20.jpg",1534356000,2));


        recyclerView.setAdapter(itemAdapter);





    }
}
