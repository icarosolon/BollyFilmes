package br.com.icaro.projetos.bollyfilmes.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Solon on 20/01/2018.
 */

public class FilmesSyncService extends Service {

    private static FilmesSyncAdapter filmesSyncAdapter = null;

    private static final Object lock = new Object();

    @Override
    public void onCreate() {
        synchronized (lock){
            if(filmesSyncAdapter == null){
                filmesSyncAdapter = new FilmesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return filmesSyncAdapter.getSyncAdapterBinder();
    }
}
