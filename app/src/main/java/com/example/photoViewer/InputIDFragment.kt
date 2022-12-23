package com.example.photoViewer

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.photoViewer.databinding.FragmentInputIdBinding
import org.json.JSONArray
import org.json.JSONObject

class InputIDFragment : Fragment() {

    private var _binding: FragmentInputIdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInputIdBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val TOKEN = (activity as MainActivity).TOKEN

        binding.buttonFirst.setOnClickListener {

            val vkId = binding.editTextVKID.text.toString()

            if (vkId.isEmpty()) {
                binding.editTextVKID.error = "ID отсутствует"
                binding.editTextVKID.requestFocus()
                return@setOnClickListener
            }

            val k : String = (activity as MainActivity).asyncRun("https://api.vk.com/method/photos.getAlbums?v=5.131&access_token=$TOKEN&owner_id=$vkId&need_system=1").get()

            if (k.substring(2, 7) == "error") {
                binding.editTextVKID.error = "Неверный ID пользователя"
                binding.editTextVKID.requestFocus()
                return@setOnClickListener
            }


            //val jsonObj = JSONObject(k)
            val albumList = (JSONObject(k).toMap()["response"] as LinkedHashMap<*, *>)["items"] as ArrayList<Map<String, *>>

            val albumTitles = arrayListOf<String>()
            val albumDesc = arrayListOf<String>()
            val photoURLs = arrayListOf<ArrayList<String>>()


            for (curAlbum in albumList){
                val albumId = curAlbum["id"]
                val albumPhotos = (activity as MainActivity).asyncRun("https://api.vk.com/method/photos.get?v=5.131&access_token=$TOKEN&owner_id=$vkId&album_id=$albumId").get()

                if (albumPhotos.substring(2, 7) == "error") continue

                val jsonPhotos = JSONObject(albumPhotos)
                val photoList = (jsonPhotos.toMap()["response"] as Map<*, *>)["items"] as ArrayList<Map<String, *>>
                val albumPhotoURLs = arrayListOf<String>()

                for (curPhoto in photoList){
                    albumPhotoURLs.add(
                        (
                            (curPhoto["sizes"] as ArrayList<*>)[2] as Map<*, *>)["url"] as String
                    )
                }

                photoURLs.add(albumPhotoURLs)
                albumTitles.add(curAlbum["title"] as String)

                if (curAlbum["description"] != null) {
                    albumDesc.add(curAlbum["description"] as String)
                } else {
                    albumDesc.add("Описание отсутствует")
                }

            }

            (activity as MainActivity).bundle.putStringArrayList("album_titles", albumTitles)
            (activity as MainActivity).bundle.putStringArrayList("album_desc", albumDesc)
            (activity as MainActivity).bundle.putSerializable("photo_urls", photoURLs)

            findNavController().navigate(R.id.action_InputIDFragment_to_AlbumsFragment)
        }
    }

    private fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith { it ->
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { it.toString() to value[it] }
                JSONObject(map).toMap().values.toMutableList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}