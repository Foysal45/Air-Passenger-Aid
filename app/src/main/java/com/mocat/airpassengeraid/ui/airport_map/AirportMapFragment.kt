package com.mocat.airpassengeraid.ui.airport_map

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mocat.airpassengeraid.R
import com.mocat.airpassengeraid.databinding.FragmentAirportMapBinding
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.lang.Float.max
import java.lang.Float.min

class AirportMapFragment : Fragment(),View.OnTouchListener {

    private var binding: FragmentAirportMapBinding? = null
    private val viewModel: AirportMapViewModel by inject()

    private val options = RequestOptions()
        .placeholder(R.drawable.civil_aviation_logo)
        .error(R.drawable.civil_aviation_logo)
    private val imageUrl = "http://15.206.81.173:3008/"

    private var scaleFactor = 1f
    private var deltaX = 0f
    private var deltaY = 0f
    private var initialX = 0f
    private var initialY = 0f
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentAirportMapBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        getAirportMap()
        //mapImageZoomInZoomOut()
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
        binding?.airportMap?.setOnTouchListener(this)
        binding?.progressBar?.visibility = View.VISIBLE
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun mapImageZoomInZoomOut(){
        var scaleFactor = 1f
        val scaleGestureDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    scaleFactor = scaleFactor.coerceIn(0.1f, 5.0f)

                    binding?.airportMap?.scaleX = scaleFactor
                    binding?.airportMap?.scaleY = scaleFactor

                    return super.onScale(detector)
                }
            }
        )

        binding?.airportMap?.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)

                //bellow part is for movable zoom in out Avobe part only  for Zoom In Out
           /* when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = event.rawX
                    initialY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    deltaX = event.rawX - initialX
                    deltaY = event.rawY - initialY

                    view?.translationX = view?.translationX?.plus(deltaX)!!
                    view?.translationY = view?.translationY?.plus(deltaY)!!


                    initialX = event.rawX
                    initialY = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                }
            }
            return@setOnTouchListener true*/
        }
    }


    //here fetch the Airport Image from API
    private fun getAirportMap() {

        viewModel.getAirportMap().observe(viewLifecycleOwner) { list ->
            val dataList = list.size
            Timber.d("mapCheck: $dataList")

            if (list.isNotEmpty()) {

                binding?.progressBar?.visibility = View.GONE

                if (list.first().mapMediaLinkInfo.isNotEmpty()) {
                    Glide.with(binding!!.airportMap)
                        .load(imageUrl+list.first().mapMediaLinkInfo.first().mediaInfo.source)
                        .apply(options)
                        .into(binding!!.airportMap)
                }
            } else {
                binding?.emptyView?.visibility = View.VISIBLE
            }

        }
    }

    //Image dynamically MoveAble ZoomInOut control here
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean{
        scaleGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.rawX
                initialY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                if (!scaleGestureDetector.isInProgress) {
                    deltaX = event.rawX - initialX
                    deltaY = event.rawY - initialY

                    binding?.airportMap?.translationX = binding?.airportMap?.translationX?.plus(deltaX)!!
                    binding?.airportMap?.translationY = binding?.airportMap?.translationY?.plus(deltaY)!!

                    initialX = event.rawX
                    initialY = event.rawY
                }
            }
            MotionEvent.ACTION_UP -> {
                initialX = event.rawX
                initialY = event.rawY
            }
      }
      return true

    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = max(0.1f, min(scaleFactor, 3f))
            binding?.airportMap?.scaleX = scaleFactor
            binding?.airportMap?.scaleY = scaleFactor

            return super.onScale(detector)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}