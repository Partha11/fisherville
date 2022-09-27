package com.techmave.fisherville.view.fragment.dashboard

import androidx.fragment.app.viewModels
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.techmave.fisherville.adapter.TransactionAdapter
import com.techmave.fisherville.databinding.FragmentProfileBinding
import com.techmave.fisherville.model.Transaction
import com.techmave.fisherville.util.extension.LifeCycleExtensions.observeOnce
import com.techmave.fisherville.view.fragment.BaseFragment
import com.techmave.fisherville.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment @Inject constructor(): BaseFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::inflate) {

    override val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var adapter: TransactionAdapter

    private var fishBoughtInKg = 0f
    private var fishBoughtInTk = 0f
    private var fishSoldInKg = 0f
    private var fishSoldInTk = 0f

    override fun initialize() {

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.transactionsRecycler.adapter = adapter

        viewModel.transactions?.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                snapshot.getValue(Transaction::class.java)?.let { trx ->

                    viewModel.trxList.observeOnce(viewLifecycleOwner) { list ->

                        if (!list.contains(trx)) {

                            viewModel.trxList.value = (viewModel.trxList.value?.plus(trx) ?: mutableListOf(trx)) as MutableList<Transaction>?

                            if (trx.transactionType == 0L) {

                                fishBoughtInKg += trx.fishAmount
                                fishBoughtInTk += trx.transactionAmount

                            } else {

                                fishSoldInKg += trx.fishAmount
                                fishSoldInTk += trx.transactionAmount
                            }
                        }

                        updateObserver()
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                viewModel.trxList.observeOnce(viewLifecycleOwner) { list ->

                    snapshot.getValue(Transaction::class.java)?.let { trx ->

                        val item = list.find { it.id == trx.id }
                        val index = list.indexOf(item)

                        viewModel.trxList.value?.set(index, trx).also { adapter.notifyItemChanged(index) }

                        if (trx.transactionType == 0L) {

                            fishBoughtInKg -= item?.fishAmount ?: 0f
                            fishBoughtInTk -= item?.transactionAmount ?: 0f

                            fishBoughtInKg += trx.fishAmount
                            fishBoughtInTk += trx.transactionAmount

                        } else {

                            fishSoldInKg -= item?.fishAmount ?: 0f
                            fishSoldInTk -= item?.transactionAmount ?: 0f

                            fishSoldInKg += trx.fishAmount
                            fishSoldInTk += trx.transactionAmount
                        }

                        updateObserver()
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

                viewModel.trxList.observeOnce(viewLifecycleOwner) { list ->

                    snapshot.getValue(Transaction::class.java)?.let { trx ->

                        val index = list.indexOf(trx)

                        viewModel.trxList.value?.remove(trx).also { adapter.notifyItemRemoved(index) }

                        if (trx.transactionType == 0L) {

                            fishBoughtInKg -= trx.fishAmount
                            fishBoughtInTk -= trx.transactionAmount

                        } else {

                            fishSoldInKg -= trx.fishAmount
                            fishSoldInTk -= trx.transactionAmount
                        }

                        updateObserver()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }
        })

//        updateUi()
//        setupRecyclerView()
//        setupTransactionListener()
    }

//    private fun setupRecyclerView() {
//
//        binding.transactionsRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        binding.transactionsRecycler.itemAnimator = DefaultItemAnimator()
//        binding.transactionsRecycler.adapter = adapter
//    }
//
//    private fun setupTransactionListener() {
//
//        listener?.getDailyTransactions()?.observe(viewLifecycleOwner, {
//
//            if (it != null) {
//
//                fishBoughtInKg = 0f
//                fishBoughtInTk = 0f
//                fishSoldInKg = 0f
//                fishSoldInTk = 0f
//
//                adapter?.items?.clear()
//                adapter?.notifyDataSetChanged()
//
//                for (data in it.children) {
//
//                    val transaction = data.getValue(Transaction::class.java)
//
//                    if (transaction != null) {
//
//                        adapter?.items?.add(0, transaction)
//                        adapter?.notifyItemInserted(0)
//
//                        if (transaction.transactionType == 0L) {
//
//                            fishBoughtInKg += transaction.fishAmount
//                            fishBoughtInTk += transaction.transactionAmount
//
//                        } else {
//
//                            fishSoldInKg += transaction.fishAmount
//                            fishSoldInTk += transaction.transactionAmount
//                        }
//                    }
//                }
//
//                updateUi()
//            }
//        })
//    }

    private fun updateObserver() {

        viewModel.fishBoughtInTk.postValue(fishBoughtInTk)
        viewModel.fishBoughtInKg.postValue(fishBoughtInKg)
        viewModel.fishSoldInTk.postValue(fishSoldInTk)
        viewModel.fishSoldInKg.postValue(fishSoldInKg)
    }

//    private fun updateUi() {
//
//        val buyText = context?.getString(R.string.transaction_text, Utility.convertToOneDecimalPoints(fishBoughtInKg), Utility.convertToOneDecimalPoints(fishBoughtInTk))
//        val sellText = context?.getString(R.string.transaction_text, Utility.convertToOneDecimalPoints(fishSoldInKg), Utility.convertToOneDecimalPoints(fishSoldInTk))
//
//        binding.dailySellText.text = sellText
//        binding.dailyBuyText.text = buyText
//    }
//
//    override fun onClick(v: View?) {
//
//        if (v?.id == R.id.transaction_fab) {
//
//            listener?.onTransactionAddClicked()
//
//        } else if (v?.id == R.id.sign_out_text) {
//
//            listener?.onSignOutClicked()
//        }
//    }
}