package se.mobileinteraction.sleip.ui.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.details_page_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.util.afterMeasure
import se.mobileinteraction.sleip.util.load


class DetailsPageFragment : Fragment(R.layout.details_page_fragment) {


    private val viewModel: DetailsPageViewModel by viewModel {
        parametersOf(horse)
    }

    private val horse: Horse
        get() = DetailsPageFragmentArgs.fromBundle(requireArguments()).horseDetails

    private val detailsAdapter = DetailsHorseAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        recyclerView.afterMeasure {}
        recyclerView.apply {
            adapter = detailsAdapter
            layoutManager = LinearLayoutManager(context)
        }
        viewModel.getHorse(horse.id)
        horse.image?.let {
            detail_photo.load(it)
        }

        viewModel.getRecList(horse.id)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeData()
    }


    private fun observeData() {
        viewModel.horseProfile.observe(viewLifecycleOwner, Observer {
            it.image?.let { it1 -> detail_photo.load(it1) }
            detail_photo.aspectRatio =
            detail_photo.height.toDouble() / detail_photo.width.toDouble()
            h_birth.text = it.date_of_birth
            h_description.text = it.description
            horse_name.text = it.name
        })

        viewModel.horseRec.observe(viewLifecycleOwner, Observer {
            detailsAdapter.setDataRec(it)
        })

    }

}
