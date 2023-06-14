package id.rashio.android.ui.main.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.rashio.android.ui.main.profile.bookmarksection.BookmarkFragment
import id.rashio.android.ui.main.profile.riwayatdeteksisection.RiwayatDeteksiFragment

class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = RiwayatDeteksiFragment()
            1 -> fragment = BookmarkFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}
