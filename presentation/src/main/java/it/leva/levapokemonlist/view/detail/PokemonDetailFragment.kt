package it.leva.levapokemonlist.view.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import it.leva.domain.model.Pokemon
import it.leva.domain.state.DataState
import it.leva.levapokemonlist.R
import it.leva.levapokemonlist.extension.loadImage
import it.leva.levapokemonlist.view.detail.adapter.ImageAdapter
import it.leva.levapokemonlist.view.detail.adapter.StatsAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class PokemonDetailFragment : Fragment() {

    var mView: View? = null
    val viewModel: DetailViewModel by viewModel()
    var url = ""
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_pokemon_detail, container, false)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            with(PokemonDetailFragmentArgs.fromBundle(it)) {
                url = urlArg
                name = nameArg
            }
        }
        viewModel.fetchDetail(name, url)
        startObserver()
    }

    private fun startObserver() {
        viewModel.getPokeDetail().observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.SUCCESS -> {
                    dataState.data?.let { viewState ->
                        viewState.pokemon?.let {
                            initView(it)
                        }
                    }

                }
                is DataState.ERROR -> {

                }
                is DataState.LOADING -> {

                }
            }
        })
    }

    fun initView(pokemon: Pokemon) {
        mView?.let {
            with((it)) {
                findViewById<AppCompatImageView>(R.id.img_main_picture).loadImage(
                    context,
                    pokemon.officialArtWorkDefault
                )
                findViewById<TextView>(R.id.txt_type).text = pokemon.getTypeText()
                findViewById<TextView>(R.id.txt_pokemon_name).text = pokemon.name
                val imageAdapter = ImageAdapter(pokemon.getImageList(), context)
                val statAdapter = StatsAdapter(pokemon.stats)
                findViewById<RecyclerView>(R.id.rv_stats).adapter = statAdapter
                findViewById<RecyclerView>(R.id.rv_images).adapter = imageAdapter
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            PokemonDetailFragment()
    }
}