package com.example.androiddata.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.androiddata.R
import com.example.androiddata.model.Monster
import com.example.androiddata.shared.SharedViewModel
import com.example.androiddata.utilities.PrefsHelper

class MainFragment : Fragment() ,RecyclerAdapter.ItemClickListner{

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: SharedViewModel
    private lateinit var swipelayout: SwipeRefreshLayout
    private lateinit var navcontroler: NavController
    private lateinit  var adapter: RecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view= inflater.inflate(R.layout.main_fragment,
            container, false)

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        recyclerView=view.findViewById(R.id.recyclerView)
        val layoutStyle = PrefsHelper.getItemType(requireContext())
        recyclerView.layoutManager=if(layoutStyle =="grid"){
            GridLayoutManager(requireContext(),2)
        }else{
            LinearLayoutManager(requireContext())
        }


        navcontroler=Navigation.findNavController(
            requireActivity(),R.id.nav_host
        )

        swipelayout = view.findViewById(R.id.swiper)
        swipelayout.setOnRefreshListener {
            viewModel.refreshData()
        }


        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        viewModel.monsterData.observe(viewLifecycleOwner, Observer {


            adapter = RecyclerAdapter(requireContext(),it,this)
            recyclerView.adapter=adapter
            swipelayout.isRefreshing=false

        })

        viewModel.activityTitle.observe(viewLifecycleOwner, Observer {
                   requireActivity().title=it
        })

        return view
    }


    override fun onItemClick(monster: Monster) {
        viewModel.selectedMonster.value=monster
       navcontroler.navigate(R.id.action_nav_detail)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_view_grid -> {
              PrefsHelper.setItemType(requireContext(),"grid")
                recyclerView.layoutManager=
                    GridLayoutManager(requireContext(),2)
                recyclerView.adapter =adapter
            }
            R.id.action_view_list -> {
                PrefsHelper.setItemType(requireContext(),"list")
                recyclerView.layoutManager=
                 LinearLayoutManager(requireContext())
                recyclerView.adapter =adapter
            }
            R.id.action_settings -> {
             navcontroler.navigate(R.id.settingsActivity)
            }
        }
        return true
    }


    override fun onResume() {
        super.onResume()
        viewModel.updateActivityTitle()
    }
}
