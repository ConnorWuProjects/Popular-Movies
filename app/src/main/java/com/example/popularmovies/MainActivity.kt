package com.example.popularmovies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.popularmovies.databinding.ActivityMainBinding
import com.example.popularmovies.model.Movie
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val movieAdapter by lazy {
        MovieAdapter(object : MovieAdapter.MovieClickListener{
            override fun onMovieClick(movie: Movie) {
                openMovieDetails(movie)
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.movieList.adapter = movieAdapter

        val movieRepository = (application as MovieApplication).movieRepository
        val movieViewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MovieViewModel(movieRepository) as T
            }
        } ).get(MovieViewModel::class.java)

        movieViewModel.popularMovies.observe(this, { popularMovies ->
            movieAdapter.addMovies(popularMovies)
        })
        movieViewModel.getError().observe(this,{ error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        })
    }

    private fun openMovieDetails(movie: Movie){
        val intent = Intent(this,DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.EXTRA_MOVIE,movie)
        }
        startActivity(intent)
    }
}