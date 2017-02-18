package com.app.backpackr.ui.sections.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.network.models.Place
import com.app.backpackr.dagger.modules.AppModule
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import javax.inject.Inject

/**
 * Created by kmikhailovskiy on 25.11.2016.
 */

class LocationsListAdapter(var context: Context, var places: OrderedRealmCollection<Place>, var itemClickListener: LocationItemClickListener) : RealmRecyclerViewAdapter<Place, LocationsListAdapter.LocationHolder>(context, places, true) {
    @Inject
    lateinit var systemParamsModule: AppModule

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LocationHolder {
        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_location, parent, false)
        return LocationHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationHolder?, position: Int) {
        val place = places[position]
        holder?.locationTitleTextView?.text = place.title
        holder?.locationDescriptionTextView?.text = place.description
        holder?.buttonRemove?.setOnClickListener {
            onRemoveButtonClicked(it as Button, position)
        }
        holder?.buttonShowOnMap?.setOnClickListener {
            onShowOnMapButtonClicked(it as Button, position)
        }

        // todo: provide graphic header loading implementation
    }

    override fun getItem(index: Int): Place? {
        return places[index]
    }

    private fun onRemoveButtonClicked(buttonRemove: Button, position: Int) {
        buttonRemove.setOnClickListener {
            places.deleteFromRealm(position)
            notifyDataSetChanged()
        }
    }

    private fun onShowOnMapButtonClicked(buttonShowOnMap: Button, position: Int) {
        buttonShowOnMap.setOnClickListener {
            val currentPlace = getItem(position)
            if (currentPlace != null) {
                itemClickListener.onShowGoogleMapsClicked(currentPlace)
            }
        }
    }

    class LocationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.place_header_image_view) lateinit var placeHeaderImageView: ImageView
        @BindView(R.id.place_title_text_view) lateinit var locationTitleTextView:  TextView
        @BindView(R.id.place_description_text_view) lateinit var locationDescriptionTextView: TextView
        @BindView(R.id.btn_remove_location) lateinit var buttonRemove: Button
        @BindView(R.id.btn_show_location) lateinit var buttonShowOnMap: Button
        @BindView(R.id.btn_fav) lateinit var buttonAddToFavorites: Button

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}

interface LocationItemClickListener {
    fun onShowGoogleMapsClicked(currentPlace: Place)
}