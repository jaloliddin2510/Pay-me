package dot.devops.payme.base

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dot.devops.payme.R

abstract class BaseFragment<VB: ViewBinding>(@LayoutRes private val layoutResId: Int) : Fragment(layoutResId) {

    private var _binding: VB?=null
    val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackgroundColor(view)
        setup()
        clicks()
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun setup()
    open fun clicks() {}
    open fun observe() {}
    @SuppressLint("ResourceType")
    open fun setBackgroundColor(view: View, color: Int= R.color.payMeBackground){
        view.setBackgroundColor(Color.parseColor(getString(color)))
    }


}