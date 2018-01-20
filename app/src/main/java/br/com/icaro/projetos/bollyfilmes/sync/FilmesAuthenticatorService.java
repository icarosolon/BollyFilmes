package br.com.icaro.projetos.bollyfilmes.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Solon on 20/01/2018.
 */

public class FilmesAuthenticatorService extends Service {

    private FilmesAuthenticator filmesAuthenticator;

    @Override
    public void onCreate(){
        this.filmesAuthenticator = new FilmesAuthenticator(this);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return filmesAuthenticator.getIBinder();
    }
}
