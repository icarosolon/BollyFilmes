package br.com.icaro.projetos.bollyfilmes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by icarosolon on 06/07/17.
 */

/*
* {"adult":false,
* "backdrop_path":"/87hTDiay2N2qWyX4Ds7ybXi9h8I.jpg",
* "belongs_to_collection":null,
* "budget":63000000,
* "genres":[{"id":18,"name":"Drama"}],
* "homepage":"http://www.foxmovies.com/movies/fight-club",
* "id":550,
* "imdb_id":"tt0137523",
* "original_language":"en",
* "original_title":"Fight Club",
* "overview":"A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
* "popularity":14.526815,
* "poster_path":"/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg",
* "production_companies":[{"name":"Regency Enterprises","id":508},{"name":"Fox 2000 Pictures","id":711},{"name":"Taurus Film","id":20555},{"name":"Linson Films","id":54050},{"name":"Atman Entertainment","id":54051},{"name":"Knickerbocker Films","id":54052}],
* "production_countries":[{"iso_3166_1":"DE","name":"Germany"},{"iso_3166_1":"US","name":"United States of America"}],
* "release_date":"1999-10-15",
* "revenue":100853753,
* "runtime":139,
* "spoken_languages":[{"iso_639_1":"en","name":"English"}],
* "status":"Released",
* "tagline":"Mischief. Mayhem. Soap.",
* "title":"Fight Club",
* "video":false,
* "vote_average":8.199999999999999,
* "vote_count":8699}
* */

public class ItemFilme implements Serializable {

    private long id;

    private String titulo;

    private String descricao;

    private String dataLancamento;

    // http://image.tmdb.org/t/p/w185/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg
    private String posterPath;

    // http://image.tmdb.org/t/p/w185/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg
    private String capaPath;

    private float avaliacao;

    private float popularidade;


    public ItemFilme(long id, String titulo, String descricao, String dataLancamento, String posterPath, String capaPath, float avaliacao, float popularidade) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataLancamento = dataLancamento;
        this.posterPath = posterPath;
        this.capaPath = capaPath;
        this.avaliacao = avaliacao;
        this.popularidade = popularidade;
    }

    public ItemFilme(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getLong("id");
        this.titulo = jsonObject.getString("title");
        this.descricao = jsonObject.getString("overview");
        this.dataLancamento = jsonObject.getString("release_date");
        this.posterPath = jsonObject.getString("poster_path");
        this.capaPath = jsonObject.getString("backdrop_path");
        this.avaliacao = (float) jsonObject.getDouble("vote_average");
        this.popularidade = (float) jsonObject.getDouble("popularity");
    }

    private String buildPath(String width, String path) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://image.tmdb.org/t/p/")
                .append(width)
                .append(path);
        return  builder.toString();
    }

    public float getPopularidade() {
        return popularidade;
    }

    public void setPopularidade(float popularidade) {
        this.popularidade = popularidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataLancamento() {

        Locale locale = new Locale("pt","BR");

        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", locale).parse(dataLancamento);
            return new SimpleDateFormat("dd/mm/aaaa", locale).format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getPosterPath() {
        return buildPath("w500", posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getCapaPath() {
        return buildPath("w780", capaPath);
    }

    public void setCapaPath(String capaPath) {
        this.capaPath = capaPath;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }
}