package com.app.backpackr.ui.sections.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.app.backpackr.R
import com.app.backpackr.api.models.Place
import com.app.backpackr.dagger.modules.AppModule
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import javax.inject.Inject

/**
 * Created by kmikhailovskiy on 25.11.2016.
 */

class LocationsListAdapter(var context: Context, var places: OrderedRealmCollection<Place>) : RealmRecyclerViewAdapter<Place, LocationsListAdapter.LocationHolder>(context, places, true) {
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
        // todo: provide graphic header loading implementation
    }

    open class LocationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.place_header_image_view) open lateinit var placeHeaderImageView: ImageView
        @BindView(R.id.place_title_text_view) open lateinit var locationTitleTextView:  TextView
        @BindView(R.id.place_description_text_view) open lateinit var locationDescriptionTextView: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}