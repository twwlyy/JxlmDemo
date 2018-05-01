package com.svse.jxlmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listview;
    private ArrayList<String> list;
    public int clickPosition = -1;
    public Boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
        list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            list.add("我是第" + i + "个条目");
        }
        adapter = new MyAdapter();
        listview.setAdapter(adapter);
    }


    private MyAdapter adapter;

    class MyAdapter extends BaseAdapter implements View.OnClickListener {


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final MyViewHolder vh;
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_list, null);
                vh = new MyViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (MyViewHolder) convertView.getTag();
            }
            vh.tv_test.setText(list.get(position));
            //刷新adapter的时候，getview重新执行，此时对在点击中标记的position做处理
            if (clickPosition == position) {//当条目为刚才点击的条目时
                if (vh.selectorImg.isSelected()) {//当条目状态图标为选中时，说明该条目处于展开状态，此时让它隐藏，并切换状态图标的状态。
                    vh.selectorImg.setSelected(false);
                    vh.ll_hide.setVisibility(View.GONE);
                    Log.e("listview","if执行");
                    clickPosition=-1;//隐藏布局后需要把标记的position去除掉，否则，滑动listview让该条目划出屏幕范围，
                    // 当该条目重新进入屏幕后，会重新恢复原来的显示状态。经过打log可知每次else都执行一次 （条目第二次进入屏幕时会在getview中寻找他自己的状态，相当于重新执行一次getview）
                    //因为每次滑动的时候没标记得position填充会执行click
                } else {//当状态条目处于未选中时，说明条目处于未展开状态，此时让他展开。同时切换状态图标的状态。
                    vh.selectorImg.setSelected(true);
                    vh.ll_hide.setVisibility(View.VISIBLE);

                    Log.e("listview","else执行");
                }
            } else {//当填充的条目position不是刚才点击所标记的position时，让其隐藏，状态图标为false。

                //每次滑动的时候没标记得position填充会执行此处，把状态改变。所以如果在以上的if (vh.selectorImg.isSelected()) {}中不设置clickPosition=-1；则条目再次进入屏幕后，还是会进入clickposition==position的逻辑中
                //而之前的滑动（未标记条目的填充）时，执行此处逻辑，已经把状态图片的selected置为false。所以上面的else中的逻辑会在标记过的条目第二次进入屏幕时执行，如果之前的状态是显示，是没什么影响的，再显示一次而已，用户看不出来，但是如果是隐藏状态，就会被重新显示出来
                vh.ll_hide.setVisibility(View.GONE);
                vh.selectorImg.setSelected(false);

                Log.e("listview","状态改变");
            }
            vh.tv4.setOnClickListener(this);

            vh.selectorImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "被点了", Toast.LENGTH_SHORT).show();
                    clickPosition = position;//记录点击的position
                    notifyDataSetChanged();//刷新adapter重新填充条目。在重新填充的过程中，被记录的position会做展开或隐藏的动作，具体的判断看上面代码
                    //在此处需要明确的一点是，当adapter执行刷新操作时，整个getview方法会重新执行，也就是条目重新做一次初始化被填充数据。
                    //所以标记position，不会对条目产生影响，执行刷新后 ，条目重新填充当，填充至所标记的position时，我们对他处理，达到展开和隐藏的目的。
                    //明确这一点后，每次点击代码执行逻辑就是 onclick（）---》getview（）
                }
            });
            return convertView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv4:
                    Toast.makeText(MainActivity.this, "切换", Toast.LENGTH_SHORT).show();
                    break;

            }
        }


        class MyViewHolder {
            View itemView;
            TextView tv_test, tv1,tv2,tv3,tv4;
            ImageView selectorImg;
            LinearLayout ll_hide;

            public MyViewHolder(View itemView) {
                this.itemView = itemView;
                tv_test = (TextView) itemView.findViewById(R.id.tv_test);
                selectorImg = (ImageView) itemView.findViewById(R.id.checkbox);
               tv1 = (TextView) itemView.findViewById(R.id.tv1);
                tv2 = (TextView) itemView.findViewById(R.id.tv2);
                tv3 = (TextView) itemView.findViewById(R.id.tv3);
                tv4 = (TextView) itemView.findViewById(R.id.tv4);
                ll_hide = (LinearLayout) itemView.findViewById(R.id.ll_hide);
            }
        }
    }
}
