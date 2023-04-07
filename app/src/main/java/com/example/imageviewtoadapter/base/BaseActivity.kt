package com.base.view
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity(),
    CoroutineScope {
    companion object{
        var TAG = this::class.simpleName
    }


    abstract fun binding(): VB
    abstract fun getViewModelClass(): Class<VM>

    open fun getData(){}
    abstract fun initView()
    open fun initOnClickListener(){}
    open fun registerObserve(){}

    lateinit var binding: VB

    lateinit var viewModel: VM

    private var job= Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO +job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
//        )
        viewModel = ViewModelProvider(this).get(getViewModelClass())
        binding = binding()
        setContentView(binding.root)
        getData()
        registerObserve()
        initView()
        initOnClickListener()
        initOtherView()
//        registerEvent()

    }

    open fun initOtherView() {

    }
    fun startActivityWithAnimation(intent: Intent) {
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        startActivity(intent, bundle)
    }

//    fun showSnackbar(view: View, message: String) {
//        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
//            .setDuration(2000)
//            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorBlack75))
//            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
//            .setAction("Action", null).show()
//    }

//    fun showDialogNotificationSimple(content: String){
//        showDialogNotificationSimple(content,3500){
//
//        }
//    }
//
//
//    fun showDialogNotificationSimple(content: String, duration: Long){
//        showDialogNotificationSimple(content,duration){
//
//        }
//    }
//
//    fun showDialogNotificationSimple(content: String,duration:Long, onAction: (Boolean) -> Unit) {
//        val builder = AlertDialog.Builder(this, R.style.TransparentBackground)
//        val binding = DialogNotificationBinding.inflate(layoutInflater)
//        builder.setView(binding.root)
//        val dialog = builder.create()
//
//        binding.content.text = content
//
//        binding.btnIUnderstand.setOnClickListener {
//            onAction(true)
//            dialog.dismiss()
//        }
//
//        dialog.show()
//        Handler(Looper.myLooper()!!).postDelayed(Runnable {
//            dialog.dismiss()
//        },duration)
//
//    }

    fun replaceFragment(frame_container: FrameLayout, frag: Fragment) {
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(frame_container.id, frag)
        fragTransaction.commit()
    }
    fun replaceFragment(frame_container: FrameLayout, frag: Fragment, isAddToBackStack:Boolean) {
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.replace(frame_container.id, frag)
        if(isAddToBackStack){
            fragTransaction.addToBackStack(null)
        }
        fragTransaction.commit()
    }

    fun addFragment(frame_container: FrameLayout, frag: Fragment) {
        val fragTransaction = supportFragmentManager.beginTransaction()
        fragTransaction.add(frame_container.id, frag)
        fragTransaction.addToBackStack(null)
        fragTransaction.commit()
    }

    override fun onDestroy() {
        onDestroyView()
//        unregisterEvent()
        viewModel.detachView()
//        viewModel=null
        job.cancel()
        super.onDestroy()
    }

    open fun onDestroyView() {

    }

    fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        if (((getSystemService(INPUT_METHOD_SERVICE)) as InputMethodManager).isAcceptingText) {
            Log.e(TAG, "Software Keyboard was shown");
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
        } else {
            Log.e(TAG, "Software Keyboard was not shown");
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

    }

//    fun registerEvent() {
//        try {
//            if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    fun unregisterEvent() {
//        try {
//            EventBus.getDefault().unregister(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
////        EventBus.getDefault().unregister(this)
//    }


    open fun onActivityForResult(intent: Intent?) {

    }

    open fun onActivityForResultCancel(intent: Intent?) {

    }

    open fun onActivityForResultRequestPermission(isGranted: Boolean) {

    }

    var launchSomeActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.e(TAG, "ActivityResultContracts result= $result")
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                // your operation...
                onActivityForResult(data)
            } else {
                onActivityForResultCancel(result.data)
            }
        }
    var launchSomePermissionActivity =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onActivityForResultRequestPermission(isGranted)
        }

    fun startActivityForResult(clazz: Class<*>) {
        val intent = Intent(this, clazz)
        launchSomeActivity.launch(intent)
    }

    fun startActivityForResult(intent: Intent) {
        launchSomeActivity.launch(intent)
    }

    fun startActivityForResultPermission(permission: String) {
        launchSomePermissionActivity.launch(permission)
    }

    fun setResult(bundle: Bundle) {
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(RESULT_OK, intent)
        finish()
    }
//     fun transparentStatusBar() {
//        val decorView = window.decorView
//        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//    }
//
//
//    private fun setFullScreen() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.setDecorFitsSystemWindows(false)
//            val controller = window.insetsController
//            if (controller != null) {
////                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
//                controller.hide(WindowInsets.Type.statusBars())
//                controller.systemBarsBehavior =
//                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        } else {
//            window.decorView.systemUiVisibility = (
//                     View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                  )
//        }
//    }


//    private fun hideSystemUI() {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        WindowInsetsControllerCompat(window, mainContainer).let { controller ->
//            controller.hide(WindowInsetsCompat.Type.systemBars())
//            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//        }
//    }
//
//    private fun showSystemUI() {
//        WindowCompat.setDecorFitsSystemWindows(window, true)
//        WindowInsetsControllerCompat(window, mainContainer).show(WindowInsetsCompat.Type.systemBars())
//    }

}