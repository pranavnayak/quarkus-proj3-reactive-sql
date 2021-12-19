package org.popins.dev.model;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class Movie {
	private Long id;
	private String title;
	
	public Movie() {
		
	}
	
	public Movie(String title) {
		this.title = title;
	}
	public Movie(Long id, String title) {
		this(title);
		this.id = id;
	}

	public static Multi<Movie> findMovies(PgPool client ){
		return client.query("SELECT id, title FROM movies ORDER BY title DESC")
			         .execute()
			         .onItem()
			         .transformToMulti(set -> Multi.createFrom().iterable(set))
			         .onItem()
			         .transform(row -> Movie.rowToMovie(row));
	}
	
	public static Uni<Movie> findMovieByID(PgPool client , Long id){
		return client.preparedQuery("SELECT id, title FROM movies WHERE id = $1")
			  .execute(Tuple.of(id))
			  .onItem()
			  .transform(mapper-> mapper.iterator().hasNext() ? rowToMovie(mapper.iterator().next()): null);
	}
	
	public static Uni<Long> saveMovie(PgPool client, String title){
		return client.preparedQuery("INSERT INTO movies (title) VALUES ($1) RETURNING id")
				.execute(Tuple.of(title))
				.onItem()
				.transform(row -> row.iterator().next().getLong("id"));
		
	}
	
	public static Uni<Boolean> deleteMovie(PgPool client, Long id){
		return client.preparedQuery("DELETE FROM movies WHERE id = $1")
				.execute(Tuple.of(id))
				.onItem()
				.transform(row -> row.rowCount() == 1);
		
	}
	
	private static Movie rowToMovie(Row row) {
		return new Movie(row.getLong("id"), row.getString("title"));
		
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
