package com.ssafy.groute.src.main.travel

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.groute.R
import com.ssafy.groute.config.ApplicationClass
import com.ssafy.groute.config.BaseFragment
import com.ssafy.groute.databinding.FragmentSharedMemberBinding
import com.ssafy.groute.src.dto.SharedUser
import com.ssafy.groute.src.dto.User
import com.ssafy.groute.src.main.MainActivity
import com.ssafy.groute.src.service.UserService
import com.ssafy.groute.src.viewmodel.PlanViewModel
import com.ssafy.groute.util.RetrofitCallback
import kotlinx.coroutines.runBlocking

private const val TAG = "SharedMemberF_Groute"
class SharedMemberFragment: BaseFragment<FragmentSharedMemberBinding>(FragmentSharedMemberBinding::bind, R.layout.fragment_shared_member)  {
    private lateinit var mainActivity : MainActivity
    private val planViewModel: PlanViewModel by activityViewModels()
    private var planId = -1
    private lateinit var sharedMemberAdapter: SharedMemberAdapter
    val member = arrayListOf<User>()
    val ids = arrayListOf<String>()
    private var checkFlag=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideMainProfileBar(true)
        mainActivity.hideBottomNav(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
        arguments?.let {
            planId = it.getInt("planId",-1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = planViewModel

        runBlocking {
            planViewModel.getShareUserbyPlanId(planId)
        }

        initAdapter()

        var flag = false

        planViewModel.planList.observe(viewLifecycleOwner, Observer {
            if( ApplicationClass.sharedPreferencesUtil.getUser().id == it.userId ){
                binding.shareMemberBtnLayout.isVisible = true
                checkFlag = true

                binding.shareMemberBtnLayout.setOnClickListener {
                    val plus = binding.memeberLottiePlus
                    val animator = ValueAnimator.ofFloat(0f,1f).setDuration(1000)
                    animator.addUpdateListener { animation ->
                        plus.progress = animation.animatedValue as Float
                    }
                    animator.start()

                    if(!flag){
                        binding.sharedMemberEtUserId.isVisible = true
                        flag = true
                    }else{
                        binding.sharedMemberEtUserId.isVisible = false
                        flag = false
                    }

                }
                binding.memeberLottiePlus.setOnClickListener {
                    checkUser()
                    binding.sharedMemberEtUserId.setText("")
                }
            }else{
                binding.shareMemberBtnLayout.isVisible = false
                checkFlag = false
            }
        })


        binding.shareMemberBtnBack.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction().remove(this).commit()
            mainActivity.supportFragmentManager.popBackStack()
        }

    }

    private fun checkUser() {
        val userId = binding.sharedMemberEtUserId.text.toString()
        UserService().isUsedId(userId, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                if(responseData){
                    for(i in 0 until planViewModel.shareUserList.value!!.size){
                        if(planViewModel.shareUserList.value!![i].id.equals(userId)){
                            showCustomToast("?????? ????????? ???????????????.")
                        }else{
                            insertUser(userId)
                            runBlocking {
                                planViewModel.getShareUserbyPlanId(planId)
                            }
                        }
                    }
                }else{
                    showCustomToast("???????????? ????????? ????????????.")
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    private fun initAdapter(){
        planViewModel.shareUserList.observe(viewLifecycleOwner, Observer {
            sharedMemberAdapter = SharedMemberAdapter()
            sharedMemberAdapter.list = it as ArrayList<User>
            binding.sharedMemberRvList.apply {
                layoutManager = GridLayoutManager(requireContext(),3)
                adapter = sharedMemberAdapter
            }
            sharedMemberAdapter.setItemClickListener(object: SharedMemberAdapter.ItemClickListener{
                override fun onClick(view: View, position: Int, id: String) {
                    if(checkFlag){
                        showDeleteDialog(id)
                    }
                }

            })
            binding.sharedMemberTvSize.text = "???????????? ???????????? ??? ${it.size}???"
        })
    }

    private fun showDeleteDialog(userId:String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("${userId}??? ????????? ?????????????????????????")
        builder.setPositiveButton("??????",DialogInterface.OnClickListener { dialog, which ->
            deleteUser(userId)
        })
        builder.setNeutralButton("??????",null)
        val dialog = builder.create()
        dialog.show()

    }

    private fun deleteUser(userId:String){
        val shareUser = SharedUser(
            planId = planId,
            userId = userId
        )
        UserService().deleteSharedUser(shareUser, object: RetrofitCallback<Boolean>{
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                showCustomToast("?????????????????????.")
                runBlocking {
                    planViewModel.getShareUserbyPlanId(planId)
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    private fun insertUser(userId:String){

        val shareUser = SharedUser(
            planId= planId,
            userId = userId
        )
        UserService().insertSharedUser(shareUser, object : RetrofitCallback<Boolean> {
            override fun onError(t: Throwable) {
                Log.d(TAG, "onError: ")
            }

            override fun onSuccess(code: Int, responseData: Boolean) {
                runBlocking {
                    planViewModel.getShareUserbyPlanId(planId)
                }
            }

            override fun onFailure(code: Int) {
                Log.d(TAG, "onFailure: ")
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(key: String, value: Int) =
            SharedMemberFragment().apply {
                arguments = Bundle().apply {
                    putInt(key, value)
                }
            }
    }
}