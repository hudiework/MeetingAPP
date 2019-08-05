package com.example.dell.demo2.loginsucce_participantpart;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.demo2.R;
import com.example.dell.demo2.loginsucce_participantpart.PayAndBill.BitmapUtil;
import com.example.dell.demo2.loginsucce_participantpart.PayAndBill.PdfUtils;
import com.example.dell.demo2.loginsucce_participantpart.PayAndBill.pdfModel;

import java.util.ArrayList;
import java.util.List;

public class ParticipantMyFilesActivity extends AppCompatActivity {
    private ListView lv_myFiles;
    private String[] strings; //pdf文件数组
    private List<pdfModel> list;//pdf文件集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_my_files);
        init();
        lv_myFiles.setAdapter(new MyPdfAdapter(ParticipantMyFilesActivity.this, list));
        lv_myFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String path = PdfUtils.getString()[position];
                Intent intent = new Intent(ParticipantMyFilesActivity.this,PdfActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });
    }

    public void init(){
        lv_myFiles=findViewById(R.id.lv_myFiles);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        strings = PdfUtils.getString();
        list = new ArrayList<pdfModel>();
        if (strings != null)
            for (int i = 0; i < strings.length; i++) {
                pdfModel model = new pdfModel();
                String url = strings[i];
                model.setName(url.substring(url.lastIndexOf('/') + 1));
                model.setPath(url);
                list.add(model);
            }
    }



    /**
     * PDF文件适配器
     * @author can
     */
    class MyPdfAdapter extends BaseAdapter {

        private Context ct ;
        private List<pdfModel> list;

        public MyPdfAdapter(Context c,List<pdfModel> mList) {
            super();
            this.ct = c;
            this.list = mList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VH vh ;
            if(convertView==null){
                convertView = LayoutInflater.from(ct).inflate(R.layout.pdf_item_layout, null);
                vh = new VH();
                vh.tv_name = (TextView) convertView.findViewById(R.id.tv_name_pdf_list);
                vh.tv_time = (TextView) convertView
                        .findViewById(R.id.tv_time_pdf_list);
                vh.iv = (ImageView) convertView
                        .findViewById(R.id.iv_pdf_list);
                vh.tv_length = (TextView) convertView
                        .findViewById(R.id.tv_length_pdf_list);
                convertView.setTag(vh);
            }else{
                vh = (VH) convertView.getTag();
            }
            vh.tv_name.setText(list.get(position).getName());
            String path = list.get(position).getPath();
            vh.tv_time.setText(BitmapUtil.getFileCreatedTime(path));
            vh.tv_length.setText(BitmapUtil.getVideoLength(path));
            return convertView;
        }

        class VH {
            ImageView iv;
            TextView tv_name,tv_time,tv_length;
        }

    }


}
