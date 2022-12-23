@file:Suppress("DEPRECATION")

package com.example.photoViewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumsFragment : Fragment() {

    private lateinit var albumTitles : ArrayList<String>
    private lateinit var albumDesks : ArrayList<String>
    private lateinit var photoURLs : ArrayList<ArrayList<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        ((activity as MainActivity).bundle.get("album_titles") as ArrayList<String>).also { this.albumTitles = it }
        ((activity as MainActivity).bundle.get("album_desc") as ArrayList<String>).also { this.albumDesks = it }
        ((activity as MainActivity).bundle.get("photo_urls") as ArrayList<ArrayList<String>>).also { this.photoURLs = it }
        return inflater.inflate(R.layout.fragment_albums, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView : RecyclerView = requireView().findViewById(R.id.albumRecyclerView)
        LinearLayoutManager(context).also { recyclerView.layoutManager = it }
        AlbumAdapter(
            albumTitles = this.albumTitles,
            albumDesks = this.albumDesks,
            photoURLs = this.photoURLs
        ).also { recyclerView.adapter = it }

    }

}