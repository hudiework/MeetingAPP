package com.example.dell.demo2.download;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.demo2.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dell on 2018-04-21.
 */

public class FileAdapter extends BaseAdapter {



    private List<String> filelist;
    private LayoutInflater inflater;
    ViewHolder holder = null;

    public FileAdapter(List<String> filelist,  Context context) {
        this.filelist = filelist;
        inflater = LayoutInflater.from(context);   //传入数据源
    }

    @Override
    public int getCount() {
        return filelist.size();
    }

    @Override
    public Object getItem(int i) {
        return filelist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) { // 缓冲池中没有可复用的view时，新建view
            //1.将列表项布局文件解析为一个view对象
            view = inflater.inflate(R.layout.file_item, null);
            holder = new ViewHolder();
            //2.使用ViewHolder类封装view中的UI组件
            holder.name = (TextView) view.findViewById(R.id.tv_filename);

            //3.为对象设置holder标记（将holder放入view中，将holder和view视图关联）
            view.setTag(holder);
        } else {
            //否则，复用缓冲池里的view对象
            holder = (ViewHolder) view.getTag();
        }
        //从数据源中获取指定位置处的数据项，并填充到子视图的显示组件中
        holder.name.setText(filelist.get(position));
        return view;
    }


    //内部类：封装子视图中的UI组件对象，优化应用
    public class ViewHolder {
        private TextView name,tv_showProgress;
        private Button btnDown;
        private ProgressBar progressBar;
    }



}
