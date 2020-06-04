package se.mobileinteraction.sleip.ui.mainFragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_horses_list_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.entities.Horse


class MainHorsesListFragment : Fragment(R.layout.main_horses_list_fragment) {

    private val viewModel by viewModel<MainHorsesListViewModel>()

    private val horseAdapter: HorseAdapter = HorseAdapter { horse -> itemClicked(horse) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main_horses_list.apply {
            adapter = horseAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

            val divider =
                ContextCompat.getDrawable(requireContext(), R.drawable.divider)
            val itemDecoration = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
            itemDecoration.setDrawable(divider!!).apply {
                    setPadding(2,0,2,0)
                }
        }

        sign_out_btn.setOnClickListener {
            viewModel.repo.remove()
            findNavController().navigate(MainHorsesListFragmentDirections.actionNavHostFragmentToLoginFragment())
        }

        add_horse_float_btn.setOnClickListener {
            findNavController().navigate(MainHorsesListFragmentDirections.actionNavHostFragmentToCreateHorseFragment())
        }

        /** Swipe left to delete **/

        val itemSwipeHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                if (direction == ItemTouchHelper.LEFT) {
                    val builder = AlertDialog.Builder(requireContext())
                    val horseName = horseAdapter.list[viewHolder.adapterPosition].name
                    builder.setMessage("Are you sure you want to Delete $horseName?")
                        .setCancelable(false)
                        .setPositiveButton("Yes") { _, _ ->
                            onDelete(horseAdapter.removeWithSwap(viewHolder))
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            viewModel.getHorsesList()
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()

                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val swipedBackground = ColorDrawable(Color.parseColor("#E691031F"))
                val itemView = viewHolder.itemView
                val deleteIcon = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_delete_24
                )!!
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                if (dX < 0) {
                    swipedBackground.setBounds(
                        itemView.right + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )
                    deleteIcon.setBounds(
                        itemView.right - iconMargin - deleteIcon.intrinsicWidth,
                        itemView.top + iconMargin,
                        itemView.right - iconMargin,
                        itemView.bottom - iconMargin
                    )

                }
                swipedBackground.draw(c)
                deleteIcon.draw(c)
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

            }
        }
        val itemTouchHelper = ItemTouchHelper(itemSwipeHelper)
        itemTouchHelper.attachToRecyclerView(main_horses_list)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observerData()
    }

    private fun itemClicked(horseDetails: Horse) {
        findNavController().navigate(
            MainHorsesListFragmentDirections.actionNavHostFragmentToDetailsPageFragment(
                horseDetails
            )
        )
    }

    private fun onDelete(horseId: Int) {
        viewModel.deleteHorse(horseId)
        viewModel.getHorsesList()
    }

}
