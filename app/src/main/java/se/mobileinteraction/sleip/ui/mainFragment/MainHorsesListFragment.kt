package se.mobileinteraction.sleip.ui.mainFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_horses_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.ui.details.DetailsPageFragmentArgs

class MainHorsesListFragment : Fragment(R.layout.main_horses_list_fragment) {

    private val viewModel by viewModel<MainHorsesListViewModel>()



    private val horseAdapter: HorseAdapter = HorseAdapter { horse -> itemClicked(horse) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerData()
        main_horses_list.apply {
            adapter = horseAdapter
            layoutManager = LinearLayoutManager(context)
        }

        sign_out_btn.setOnClickListener {
            viewModel.repo.remove()
        }

        add_horse_btn.setOnClickListener {
            findNavController().navigate(MainHorsesListFragmentDirections.actionNavHostFragmentToCreateHorseFragment())
        }
    }

    private fun observerData() {
        viewModel.horseData.observe(viewLifecycleOwner, Observer {
            horseAdapter.setData(it)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getHorsesList()
    }

     private fun itemClicked(horseDetails: Horse) {
        findNavController().navigate(MainHorsesListFragmentDirections.actionNavHostFragmentToDetailsPageFragment(horseDetails))
    }
}
<<<<<<< HEAD
}
=======
>>>>>>> 970a605a10c175ef397eb85268be5af3315894fd
