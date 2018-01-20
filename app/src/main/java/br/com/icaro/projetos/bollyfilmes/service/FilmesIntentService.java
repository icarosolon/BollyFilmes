package br.com.icaro.projetos.bollyfilmes.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import br.com.icaro.projetos.bollyfilmes.BuildConfig;
import br.com.icaro.projetos.bollyfilmes.ItemFilme;
import br.com.icaro.projetos.bollyfilmes.JsonUtil;
import br.com.icaro.projetos.bollyfilmes.R;
import br.com.icaro.projetos.bollyfilmes.data.FilmesContract;

/**
 * Created by Solon on 20/01/2018.
 */

public class FilmesIntentService extends IntentService {


    public FilmesIntentService() {
        super("FilmesIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // https://api.themoviedb.org/3/movie/popular?api_key=qwer08776&language=pt-BR

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ordem = preferences.getString(getString(R.string.prefs_ordem_key), "popular");
        String idioma = preferences.getString(getString(R.string.prefs_idioma_key), "pt-BR");

        try {
            String urlBase = "https://api.themoviedb.org/3/movie/" + ordem + "?";
            String apiKey = "api_key";
            String language = "language";

            Uri uriApi = Uri.parse(urlBase).buildUpon()
                    .appendQueryParameter(apiKey, BuildConfig.TMDB_API_KEY)
                    .appendQueryParameter(language, idioma)
                    .build();

            URL url = new URL(uriApi.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();


            reader = new BufferedReader(new InputStreamReader(inputStream));

            String linha;
            StringBuffer buffer = new StringBuffer();
            while ((linha = reader.readLine()) != null) {
                buffer.append(linha);
                buffer.append("\n");
            }

            List<ItemFilme> itemFilmes = JsonUtil.fromJsonToList(buffer.toString());


            for (ItemFilme itemFilme : itemFilmes) {
                ContentValues values = new ContentValues();
                values.put(FilmesContract.FilmesEntry._ID, itemFilme.getId());
                values.put(FilmesContract.FilmesEntry.COLUMN_TITULO, itemFilme.getTitulo());
                values.put(FilmesContract.FilmesEntry.COLUMN_DESCRICAO, itemFilme.getDescricao());
                values.put(FilmesContract.FilmesEntry.COLUMN_POSTER_PATH, itemFilme.getPosterPath());
                values.put(FilmesContract.FilmesEntry.COLUMN_CAPA_PATH, itemFilme.getCapaPath());
                values.put(FilmesContract.FilmesEntry.COLUMN_AVALIACAO, itemFilme.getAvaliacao());
                values.put(FilmesContract.FilmesEntry.COLUMN_DATA_LANCAMENTO, itemFilme.getDataLancamento());
                values.put(FilmesContract.FilmesEntry.COLUMN_POPULARIDADE, itemFilme.getPopularidade());

                int update = getContentResolver().update(FilmesContract.FilmesEntry.buildUriForFilmes(itemFilme.getId()), values, null, null);

                if (update == 0) {
                    getContentResolver().insert(FilmesContract.FilmesEntry.CONTENT_URI, values);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class FilmesReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intentService = new Intent(context, FilmesIntentService.class);
            context.startService(intent);
        }
    }
}
