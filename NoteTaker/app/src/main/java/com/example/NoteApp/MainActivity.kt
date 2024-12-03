package com.example.fragmentgg1
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.NoteTaker.MyViewModel
import com.example.NoteTaker.PageAdapter
import com.example.fragmentgg1.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MyViewModel

    // Create the activity  layout and set up the ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel and Toolbar
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        // Set up the toolbar and tabs
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Set up the TabLayout and ViewPager with the PageAdapter to display the fragments
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.pager)

        // Set up the ViewPager with the PageAdapter to display the fragments to the user
        val adapter = PageAdapter(this, 3)
        viewPager.adapter = adapter

        // Save the current tab position
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.setCurrentTab(position)
            }
        })

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
//                    tab.text = "Time"
                    tab.setIcon(R.drawable.ic_time)
                }
                1 -> {
//                    tab.text = "Note"
                    tab.setIcon(R.drawable.ic_note)
                }
                2 -> {
//                    tab.text = "History"
                    tab.setIcon(R.drawable.ic_see_note)
                }
            }
        }.attach()

        // Restore the last selected tab
        viewModel.currentTab.value?.let { position ->
            viewPager.setCurrentItem(position, false)
        }
    }
}