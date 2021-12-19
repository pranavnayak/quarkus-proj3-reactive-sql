package org.popins.dev;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

import org.popins.dev.model.Movie;


@Path("movies")
public class MovieResource {
	
	@Inject
    PgPool client;
	
	@PostConstruct
	void config() {
		intiDB();
	}

	private void intiDB() {
		client.query("DROP TABLE IF EXISTS movies")
			  .execute()
			  .flatMap(mapper -> client.query("CREATE TABLE movies (id SERIAL PRIMARY KEY, title TEXT NOT NULL)").execute())
			  .flatMap(mapper -> client.query("INSERT INTO movies (title) VALUES ('THE MATRIX')").execute())
			  .flatMap(mapper -> client.query("INSERT INTO movies (title) VALUES ('THE MATRIX RELOADED')").execute())
			  .await()
			  .indefinitely();
	}
	
	@GET
    public Multi<Movie> getAll() {
        return Movie.findMovies(client);
    }
	
	@GET
	@Path("{id}")
	public Uni<Response> getMovieByID(@PathParam("id") Long id){
		return Movie.findMovieByID(client, id)
			   .onItem()
			   .transform(movie -> movie != null? Response.ok(movie) : Response.status(Response.Status.NOT_FOUND))
			   .onItem()
			   .transform(response -> response.build());
	}
	
	@POST
	public Uni<Response> createMovie(Movie movie){
		return Movie.saveMovie(client, movie.getTitle())
				    .onItem()
				    .transform(id -> URI.create("/movie"+ id)) 
				    .onItem()
				    .transform(uri -> Response.created(uri).build());
	}
	
	@DELETE
	@Path("{id}")
	public Uni<Response> removeMovie(@PathParam("id") Long id){
		return Movie.deleteMovie(client, id)
				    .onItem()
				    .transform(mapper -> mapper ? Response.Status.NO_CONTENT : Response.Status.NOT_FOUND )
				    .onItem()
				    .transform(status -> Response.status(status).build());
				    
	}

}
