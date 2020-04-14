package br.com.truckpad.waister.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.truckpad.waister.R
import br.com.truckpad.waister.domain.History
import br.com.truckpad.waister.ui.adapter.HistoryAdapter
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private var historyAdapter: HistoryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        renderData()
    }

    private fun initViews() {
        val layoutManager = LinearLayoutManager(this)
        recycler_phones.layoutManager = layoutManager

        historyAdapter = HistoryAdapter(this)
        recycler_phones.adapter = historyAdapter

        recycler_phones.addItemDecoration(
            DividerItemDecoration(
                recycler_phones.context,
                layoutManager.orientation
            )
        )
    }

    private fun renderData() {
        val data: ArrayList<History>? = Hawk.get(MainActivity.HAWK_HISTORY, null)
        val history = data ?: ArrayList()

        if (history.isNotEmpty()) {

            text_empty.visibility = View.GONE
            recycler_phones.visibility = View.VISIBLE

        } else {

            text_empty.visibility = View.VISIBLE
            recycler_phones.visibility = View.GONE

        }

        historyAdapter?.setData(history)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_clear_history -> {
                AlertDialog.Builder(this)
                    .setTitle(R.string.confirmation)
                    .setMessage(R.string.confirm_clear_history)
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        Hawk.delete(MainActivity.HAWK_HISTORY)

                        renderData()

                        Toast.makeText(this, R.string.history_deleted, Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .create()
                    .show()
            }
            else -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
