package com.techmave.fisherville.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.techmave.fisherville.R
import com.techmave.fisherville.adapter.FishAdapter
import com.techmave.fisherville.databinding.DialogTransactionBinding
import com.techmave.fisherville.model.Market
import com.techmave.fisherville.model.Transaction

class TransactionDialog(context: Context): DialogFragment(), View.OnClickListener {

    private lateinit var binding: DialogTransactionBinding

    private var listener: TransactionListener? = null
    private var adapter: FishAdapter? = null
    private var market: Market? = null

    var items: List<Market> = mutableListOf()

    init {

        if (context is TransactionListener) {

            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DialogTransactionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {

        adapter = FishAdapter(requireContext(), R.layout.spinner_dropdown, items)

        binding.fishNameSpinner.adapter = adapter
        binding.radioGroup.check(R.id.radio_buy)

        if (items.isNotEmpty()) {

            market = items[0]
        }

        binding.transactionConfirm.setOnClickListener(this)
        binding.transactionCancel.setOnClickListener(this)
    }

    override fun onResume() {

        super.onResume()

        val window = dialog?.window
        val size = Point()

        if (window != null) {

            val display = window.windowManager.defaultDisplay
            display.getSize(size)

            val width = size.x

            window.setLayout((width * 0.95).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
            window.setGravity(Gravity.CENTER_HORIZONTAL)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.transaction_confirm -> addTransaction()
            R.id.transaction_cancel -> dismiss()
        }
    }

    private fun addTransaction() {

        when {

            binding.transactionAmount.text.isNullOrEmpty() -> binding.transactionAmount.error = "Required"
            binding.transactionFishAmount.text.isNullOrEmpty() -> binding.transactionFishAmount.error = "Required"

            else -> {

                try {

                    val transaction = Transaction()
                    val position = binding.fishNameSpinner.selectedItemPosition

                    transaction.fishName = items[position].name
                    transaction.fishId = items[position].id
                    transaction.fishAmount = binding.transactionFishAmount.text.toString().toFloat()
                    transaction.transactionAmount = binding.transactionAmount.text.toString().toFloat()
                    transaction.transactionTime = System.currentTimeMillis()
                    transaction.transactionType = if (binding.radioGroup.checkedRadioButtonId == R.id.radio_buy) 0L else 1L

                    listener?.onTransactionAdded(transaction)

                    binding.transactionFishAmount.setText("")
                    binding.transactionAmount.setText("")
                    binding.radioGroup.check(R.id.radio_buy)

                } catch (ex: Exception) {

                    Toast.makeText(context, ex.localizedMessage, Toast.LENGTH_SHORT).show()

                } finally {

                    dismiss()
                }
            }
        }
    }

    interface TransactionListener {

        fun onTransactionAdded(transaction: Transaction)
    }
}
