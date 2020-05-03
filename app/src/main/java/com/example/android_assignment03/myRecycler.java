package com.example.android_assignment03;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_03.R;
import com.squareup.picasso.Picasso;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class myRecycler extends RecyclerView.Adapter<myRecycler.ViewHolder> {

    Context con;
    String bu = "https://raw.githubusercontent.com/revolunet/PythonBooks/master/";
    private ArrayList<String> head;
    private ArrayList<String> levels;

    private ArrayList<String> id;
    private ArrayList<String> url;
    private ArrayList<String> information;
    ProgressDialog p_dialog;
    private  String book_Url="";
    public myRecycler(Context con, ArrayList<String> head, ArrayList<String> levels, ArrayList<String> information, ArrayList<String> id, ArrayList<String> url) {

        this.con = con;
        this.id = id;
        this.head = head;

        this.information=information;
        this.levels = levels;
        this.url=url;
        p_dialog =new ProgressDialog(this.con);

        p_dialog.setProgressStyle(p_dialog.STYLE_HORIZONTAL);
        p_dialog.setMessage("Downloading....");
        p_dialog.setIndeterminate(true);
        p_dialog.setCancelable(true);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(con).inflate(R.layout.list_item, null);
        ViewHolder my_holder = new ViewHolder(row);
        return my_holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder my_holder, final int pos) {
        my_holder.txt_head.setText(head.get(pos));
        my_holder.txt_levels.setText(levels.get(pos));
        my_holder.txt_information.setText(information.get(pos));
        Picasso.get().load(book_Url + id.get(pos)).into(my_holder.img_icon);
        final String subs=url.get(pos);
        if(subs.contains("pdf")|| subs.contains("zip"))
        {
            my_holder.btn.setText("Download");
        }
        else
        {
            my_holder.btn.setText("Read Onlne");

        }
        my_holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(subs.contains("pdf")|| subs.contains("zip")) {
                    book_Url=head.get(pos);
                    new CustomDownload().execute(subs);
                }
                else
                {
                    Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(subs));
                    con.startActivity(intent);
                }
            }

        });


    }

    @Override
    public int getItemCount() {
        return head.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_head;
        public TextView txt_levels;
        public TextView txt_information;
        public ImageView img_icon;
        public Button btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_head = itemView.findViewById(R.id.txt_head);
            txt_levels=itemView.findViewById(R.id.txt_levels);
            txt_information=itemView.findViewById(R.id.txt_information);
            img_icon = itemView.findViewById(R.id.img_icon);
            btn=itemView.findViewById(R.id.btn);
        }
    }
class CustomDownload extends AsyncTask<String,Integer,String>
{
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        p_dialog.show();
    }
    @Override
    protected String doInBackground(String... fnUrl)
    {
        int c;
        try {



            URL url=new URL(fnUrl[0]);
            URLConnection conn=url.openConnection() ;
            conn.connect();
            int length=conn.getContentLength();
            InputStream ipS= new BufferedInputStream(url.openStream());
            final  int PERMISSION_REQUEST_CODE=12345;
            ActivityCompat.requestPermissions((Activity)con,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
            OutputStream opS=new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+book_Url+".pdf");
            byte[]buffer=new byte[1024];
            long total=0;
            while ((c=ipS.read(buffer))!=-1)
            {
                total+=c;
                publishProgress((int)((total*100)/length));


                opS.write(buffer,0,c);
            }
            opS.flush();;
            opS.close();;
            ipS.close();
            ipS.close();


        }
        catch (FileNotFoundException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        return  null;

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        p_dialog.setIndeterminate(false);
        p_dialog.setMax(100);
        p_dialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        p_dialog.dismiss();
            if(o==null)
            {
                Toast.makeText(con,"File downloaded", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(con,"Download Error"+o,Toast.LENGTH_SHORT).show();
            }
    }
}

}
