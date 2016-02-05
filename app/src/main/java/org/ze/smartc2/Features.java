package org.ze.smartc2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Features extends Fragment {
    GridView gridview;
    View view;
    final String[] imgText= {"民宿", "景點", "停車場", "警局", "最近的警局", "醫院"};
    public Features() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_features, container, false);
        gridview = (GridView)view.findViewById(R.id.gridview);

        int[] image = {
                R.drawable.bab, R.drawable.travel, R.drawable.parking,
                R.drawable.police, R.drawable.policecar, R.drawable.hospital
        };


        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("image", image[i]);
            item.put("text", imgText[i]);
            items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(getContext(),
                items, R.layout.grid_item, new String[]{"image", "text"},
                new int[]{R.id.image, R.id.text});


        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                whatdoufind(position);

            }



        });



        return view;

    }

    private void whatdoufind(int position) {
        Intent intent;
        Bundle bundle;
        switch (position){
            case 0:
                new AlertDialog.Builder(getActivity())
                    .setTitle("選擇距離")
                    .setPositiveButton("1公里", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), MapsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("table", "bab");
                            bundle.putInt("dis",1000);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("5公里", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), MapsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("table", "bab");
                            bundle.putInt("dis",5000);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    })
                    .setNeutralButton("10公里", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), MapsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("table", "bab");
                            bundle.putInt("dis",10000);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    })
                    .show();
            break;

            case 1:

                    new AlertDialog.Builder(getActivity())
                            .setTitle("選擇距離")
                            .setPositiveButton("1公里", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), MapsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("table", "view");
                                    bundle.putInt("dis",1000);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("5公里", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), MapsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("table", "view");
                                    bundle.putInt("dis",5000);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            })
                            .setNeutralButton("10公里", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), MapsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("table", "view");
                                    bundle.putInt("dis",10000);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            })
                            .show();


            break;
            case 2:
                    intent = new Intent();
                    intent.setClass(getActivity(), MapsActivity.class);
                    bundle = new Bundle();
                    bundle.putString("table", "parking");
                    intent.putExtras(bundle);
                    startActivity(intent);
            break;
            case 3:


                    intent = new Intent();
                    intent.setClass(getActivity(), MapsActivity.class);
                    bundle = new Bundle();
                    bundle.putString("table", "police");
                    intent.putExtras(bundle);
                    //                chechpermission();
                    startActivity(intent);
            break;
            case 4:
                    intent = new Intent();
                    intent.setClass(getActivity(), MapsActivity.class);
                    bundle = new Bundle();
                    bundle.putString("table", "police_close");
                    intent.putExtras(bundle);
                    startActivity(intent);

            break;
            case 5:
                    intent = new Intent();
                    intent.setClass(getActivity(), MapsActivity.class);
                    bundle = new Bundle();
                    bundle.putString("table", "hospital_close");
                    intent.putExtras(bundle);
                    startActivity(intent);
            break;

        }




    }


}
