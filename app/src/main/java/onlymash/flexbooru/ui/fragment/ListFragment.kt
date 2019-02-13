package onlymash.flexbooru.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.refreshable_list.*
import onlymash.flexbooru.R
import onlymash.flexbooru.ui.MainActivity
import onlymash.flexbooru.widget.SearchBar
import onlymash.flexbooru.widget.SearchBarMover

abstract class ListFragment : Fragment() {

    companion object {
        internal const val STATE_NORMAL = 0
        internal const val STATE_SEARCH = 1
    }

    internal var state = STATE_NORMAL

    internal lateinit var leftDrawable: DrawerArrowDrawable
    private lateinit var searchBarMover: SearchBarMover

    abstract val helper: SearchBar.Helper

    private val sbMoverHelper: SearchBarMover.Helper
        get() = object : SearchBarMover.Helper {
            override val validRecyclerView: RecyclerView
                get() = list

            override fun isValidView(recyclerView: RecyclerView): Boolean {
                return state == STATE_NORMAL && recyclerView == list
            }

            override fun forceShowSearchBar(): Boolean {
                return state == STATE_SEARCH
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeRefresh()
        initSearchBar()
    }

    private fun initSwipeRefresh() {
        val start = resources.getDimensionPixelSize(R.dimen.swipe_refresh_layout_offset_start)
        val end = resources.getDimensionPixelSize(R.dimen.swipe_refresh_layout_offset_end)
        swipe_refresh.apply {
            setProgressViewOffset(false, start, end)
            setColorSchemeResources(
                R.color.blue,
                R.color.purple,
                R.color.green,
                R.color.orange,
                R.color.red
            )
        }
    }
    private fun initSearchBar() {
        leftDrawable = DrawerArrowDrawable(requireContext())
        search_bar.setLeftDrawable(leftDrawable)
        search_bar.setHelper(helper)
        search_bar.setLeftButtonListener(object : SearchBar.LeftButtonListener {
            override fun onLeftButtonClick() {
                val activity = requireActivity()
                if (activity is MainActivity) {
                    activity.drawer.openDrawer()
                } else {
                    activity.onBackPressed()
                }
            }
        })
        searchBarMover = SearchBarMover(sbMoverHelper, search_bar, list)
    }
}