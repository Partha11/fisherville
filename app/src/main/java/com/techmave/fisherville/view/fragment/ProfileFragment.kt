package com.techmave.fisherville.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.techmave.fisherville.R
import com.techmave.fisherville.adapter.TransactionAdapter
import com.techmave.fisherville.databinding.FragmentProfileBinding
import com.techmave.fisherville.listener.FragmentListener
import com.techmave.fisherville.model.Transaction
import com.techmave.fisherville.util.Utility


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding

    private var listener: FragmentListener? = null
    private var adapter: TransactionAdapter? = null

    private var fishBoughtInKg = 0f
    private var fishBoughtInTk = 0f
    private var fishSoldInKg = 0f
    private var fishSoldInTk = 0f

    companion object {

        @JvmStatic
        fun getInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)

        if (context is FragmentListener) {

            listener = context
        }

        (activity as AppCompatActivity).supportActionBar?.hide()
        (activity as AppCompatActivity).title = "Profile"
    }

    private fun initialize() {

        adapter = TransactionAdapter(context)

        binding.transactionFab.setOnClickListener(this)
        binding.signOutText.setOnClickListener(this)

        updateUi()
        setupRecyclerView()
        setupTransactionListener()
    }

    private fun setupRecyclerView() {

        binding.transactionsRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.transactionsRecycler.itemAnimator = DefaultItemAnimator()
        binding.transactionsRecycler.adapter = adapter
    }

    private fun setupTransactionListener() {

        listener?.getDailyTransactions()?.observe(viewLifecycleOwner, {

            if (it != null) {

                fishBoughtInKg = 0f
                fishBoughtInTk = 0f
                fishSoldInKg = 0f
                fishSoldInTk = 0f

                adapter?.items?.clear()
                adapter?.notifyDataSetChanged()

                for (data in it.children) {

                    val transaction = data.getValue(Transaction::class.java)

                    if (transaction != null) {

                        adapter?.items?.add(0, transaction)
                        adapter?.notifyItemInserted(0)

                        if (transaction.transactionType == 0L) {

                            fishBoughtInKg += transaction.fishAmount
                            fishBoughtInTk += transaction.transactionAmount

                        } else {

                            fishSoldInKg += transaction.fishAmount
                            fishSoldInTk += transaction.transactionAmount
                        }
                    }
                }

                updateUi()
            }
        })
    }

    private fun updateUi() {

        val buyText = context?.getString(R.string.transaction_text, Utility.convertToOneDecimalPoints(fishBoughtInKg), Utility.convertToOneDecimalPoints(fishBoughtInTk))
        val sellText = context?.getString(R.string.transaction_text, Utility.convertToOneDecimalPoints(fishSoldInKg), Utility.convertToOneDecimalPoints(fishSoldInTk))

        binding.dailySellText.text = sellText
        binding.dailyBuyText.text = buyText
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.transaction_fab) {

            listener?.onTransactionAddClicked()

        } else if (v?.id == R.id.sign_out_text) {

            listener?.onSignOutClicked()
        }
    }
}