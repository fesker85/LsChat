package cc.lzsou.lschat.core.handler;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewOutlineProvider;

import cc.lzsou.lschat.views.dialog.LoadingDialog;


public abstract class AsynHandler {

    private LoadingDialog dialog;
    private Context mContext;
    private boolean isShowDialog;
    private String mDialogmsg;
    public AsynHandler(){
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                return onProcess();
            }
            @Override
            protected void onPostExecute(Object o) {
                onResult(o);
                super.onPostExecute(o);
            }
        }.execute();

    }
    public AsynHandler( Context context,  boolean showDialog,  String dialogmsg){
        this.mContext=context;
        this.isShowDialog=showDialog;
        this.mDialogmsg=dialogmsg;
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(isShowDialog){
                    dialog = new LoadingDialog.Builder(mContext).setCancelable(true).setMessage(mDialogmsg).create();
                    dialog.show();
                }
            }
            @Override
            protected Object doInBackground(Void... voids) {
                return onProcess();
            }

            @Override
            protected void onPostExecute(Object o) {
                if(dialog!=null)dialog.dismiss();
                onResult(o);
                super.onPostExecute(o);
            }
        }.execute();
    }

    protected abstract Object onProcess();
    protected abstract void  onResult(Object object);



}
