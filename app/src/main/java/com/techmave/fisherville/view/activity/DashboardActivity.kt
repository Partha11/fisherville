package com.techmave.fisherville.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techmave.fisherville.R
import com.techmave.fisherville.databinding.ActivityDashboardBinding
import com.techmave.fisherville.listener.FragmentListener
import com.techmave.fisherville.model.User
import com.techmave.fisherville.util.SharedPrefs
import com.techmave.fisherville.util.Utility
import com.techmave.fisherville.view.fragment.NewsFragment
import com.techmave.fisherville.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity(), (Int) -> Unit, FragmentListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    private var prefs: SharedPrefs? = null

    private var fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initialize()
    }

    override fun onResume() {

        super.onResume()
        updateBasicInfo()
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)

        fragments.add(NewsFragment.getInstance())
        fragments.add(NewsFragment.getInstance())
        fragments.add(NewsFragment.getInstance())

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        prefs = SharedPrefs(this)

        if (prefs?.userNumber != null) {

            viewModel.setLiveData(prefs?.userNumber!!)
        }

        binding.bottomNavigation.onItemSelected = this
        loadFragment(getFragment(0))
    }

    private fun updateBasicInfo() {

        viewModel.getUserProfile()?.observe(this, {

            if (it != null) {

                val user = it.getValue(User::class.java)

                if (user != null) {

                    prefs?.userName = user.name
                    prefs?.userType = user.type
                }
            }
        })
    }

    private fun getFragment(position: Int): Fragment {

        return fragments[position]
    }

    private fun loadFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    override fun invoke(position: Int) {

        loadFragment(getFragment(position))
    }

    override fun getGreetingsMessage(): String {

        return "${Utility.getGreetingMessage()}, ${prefs?.userName}"
    }
}
