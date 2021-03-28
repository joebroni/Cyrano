package com.corgrimm.goodhusband.ui.notifications

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.corgrimm.goodhusband.GoodHusbandApplication
import com.corgrimm.goodhusband.R
import com.corgrimm.goodhusband.ReminderRecyclerAdapter

class NotificationsFragment : Fragment() {

//    private lateinit var notificationsViewModel: NotificationsViewModel
    private val notificationsViewModel: NotificationsViewModel by viewModels {
        ReminderViewModelFactory((requireActivity().application as GoodHusbandApplication).repository)
    }
    lateinit var fav: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notifications_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        val recyclerView = root.findViewById<RecyclerView>(R.id.reminder_recycler)
        val adapter = ReminderRecyclerAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        notificationsViewModel.allReminders.observe(viewLifecycleOwner, Observer { reminders ->
            // Update the cached copy of the words in the adapter.
            reminders?.let { adapter.submitList(it) }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}