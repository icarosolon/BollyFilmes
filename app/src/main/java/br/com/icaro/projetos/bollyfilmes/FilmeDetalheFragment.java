package br.com.icaro.projetos.bollyfilmes;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import br.com.icaro.projetos.bollyfilmes.data.FilmesContract;

import static java.security.AccessController.getContext;


public class FilmeDetalheFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri filmeUri;

    private TextView tituloView;

    private TextView dataLancamentoView;

    private TextView descricaoView;

    private RatingBar avaliacaoView;

    private ImageView capaView;

    private ImageView posterView;

    private static final int FILME_DETALHE_LOADER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            filmeUri = (Uri) getArguments().getParcelable(MainActivity.FILME_DETALHE_URI);
        }

        getLoaderManager().initLoader(FILME_DETALHE_LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filme_detalhe, container, false);

        tituloView = (TextView) view.findViewById(R.id.item_titulo);

        dataLancamentoView = (TextView) view.findViewById(R.id.item_data);

        descricaoView = (TextView) view.findViewById(R.id.item_desc);

        avaliacaoView = (RatingBar) view.findViewById(R.id.item_avaliacao);

        capaView = (ImageView) view.findViewById(R.id.item_capa);

        posterView = (ImageView) view.findViewById(R.id.item_poster);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                FilmesContract.FilmesEntry._ID,
                FilmesContract.FilmesEntry.COLUMN_TITULO,
                FilmesContract.FilmesEntry.COLUMN_DESCRICAO,
                FilmesContract.FilmesEntry.COLUMN_POSTER_PATH,
                FilmesContract.FilmesEntry.COLUMN_CAPA_PATH,
                FilmesContract.FilmesEntry.COLUMN_AVALIACAO,
                FilmesContract.FilmesEntry.COLUMN_DATA_LANCAMENTO
        };

        return new CursorLoader(getContext(), filmeUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {

            int tituloIndex = data.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_TITULO);
            int descricaoIndex = data.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_DESCRICAO);
            int posterIndex = data.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_POSTER_PATH);
            int capaIndex = data.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_CAPA_PATH);
            int avaliacaoIndex = data.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_AVALIACAO);
            int dataLancamentoIndex = data.getColumnIndex(FilmesContract.FilmesEntry.COLUMN_DATA_LANCAMENTO);

            String titulo = data.getString(tituloIndex);
            String descricao = data.getString(descricaoIndex);
            String poster = data.getString(posterIndex);
            String capa = data.getString(capaIndex);
            float avaliacao = data.getFloat(avaliacaoIndex);
            String dataLancamento = data.getString(dataLancamentoIndex);

            tituloView.setText(titulo);
            descricaoView.setText(descricao);
            avaliacaoView.setRating(avaliacao);
            dataLancamentoView.setText(dataLancamento);

            new DownloadImageTask(capaView).execute(capa);

            if (posterView != null) {
                new DownloadImageTask(posterView).execute(poster);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}